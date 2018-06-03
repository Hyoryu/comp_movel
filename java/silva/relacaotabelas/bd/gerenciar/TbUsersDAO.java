package silva.relacaotabelas.bd.gerenciar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import silva.relacaotabelas.bd.criar.DBDunno;
import silva.relacaotabelas.bd.model.Hash;

import silva.relacaotabelas.bd.model.Pessoa;

public class TbUsersDAO {
    private static final String TAG = "Tabela Users";
    public SQLiteDatabase db;

     /*  Colunas da tabela
     *  Users: Pessoa.colunas{PK: "UserID","UserName","UserNickname","UserEmail","UserPassword"};*/
    public TbUsersDAO(Context ctx){
        db = ctx.openOrCreateDatabase(DBDunno.NOME_BANCO, Context.MODE_PRIVATE, null);
    }
    public int cadastrarUsuario(Pessoa user){
        int apelido, email;

        apelido = contarApelido(user, 0);
        if(apelido != 0)
            return apelido;
        email = contarEmail(user, 0);
        if(email != 0)
            return email;

        ContentValues userValues = setarValoresUsuario(user);
        try {
            db.insert(DBDunno.NOME_TABELAS[0], "", userValues);
            return 0;
        }catch (Exception e){
            Log.e("Salvar usuário",e.getMessage());
            return -1;
        }
    }
    public int editarDados(Pessoa user){
        int possoEditar = condEditarDados(user);
        if(possoEditar != 0)
            return possoEditar;

        ContentValues userValues = setarValoresUsuario(user);

        String whereClause = Pessoa.colunas[0]+"=? AND "+Pessoa.colunas[4]+"=?";
        try {
            Pessoa aux = buscarUsuario(user.getID());
            //verificar se a senha inserida é igual a senha no bd
            if(!aux.getPassword().equals(""+Hash.geraCodigo(user.getPassword())) )
                return -2;
            db.update(DBDunno.NOME_TABELAS[0], userValues, whereClause, new String[]{
                    "" + user.getID(), "" + Hash.geraCodigo(user.getPassword())}
            );
            return 0;
        }catch (Exception e){
            Log.e("Update usuario",e.getMessage());
            return -1;
        }
    }
    public boolean editarSenha(long userID, String novaSenha){
        ContentValues senha = new ContentValues();
        senha.put(Pessoa.colunas[4], Long.toString(Hash.geraCodigo(novaSenha)));

        String whereClause = Pessoa.colunas[0] + "= ?";
        String[] whereArgs = new String[]{Long.toString(userID)};
        try{
            db.update(DBDunno.NOME_TABELAS[0],senha, whereClause, whereArgs);
        }catch (Exception e){
            Log.e("Editar Senha", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean esqueciSenha(String usuario, String newPassword){
        Pessoa user = logar(usuario);
        if (user == null)
            return false;
        String whereClause = Pessoa.colunas[0] + "=?";
        String[] whereArg = new String[]{Long.toString(user.getID())};
        ContentValues senha = new ContentValues();
        senha.put(Pessoa.colunas[4],Long.toString(Hash.geraCodigo(newPassword)));
        try{
            db.update(DBDunno.NOME_TABELAS[0],senha,whereClause,whereArg);
            return true;
        }catch (Exception e){
            Log.e("Esqueci Senha", e.getMessage());
            return false;
        }

    }
    //1º deletar os livros relacionados ao usuário e depois deletar o usuário
    public boolean deletarUsuario(Pessoa usuario){

        String whereClause = Pessoa.colunas[0] +"=?";
        try {
            //DELETE FROM Books WHERE UserID = usuario.getID
            db.delete(DBDunno.NOME_TABELAS[1],whereClause, new String[]{""+usuario.getID()});
            //DELETE FROM Users WHERE UserID = usuario.getID
            db.delete(DBDunno.NOME_TABELAS[0], whereClause, new String[]{"" + usuario.getID()});
            return true;
        }catch (Exception e){
            Log.e("Deletar",e.getMessage());
            return false;
        }
    }

    private int condEditarDados(Pessoa usuario){
        int apelido, email;
        apelido = contarApelido(usuario, 1);
        if(apelido != 0)
            return apelido;
        email = contarEmail(usuario, 1);
        if(email !=0)
            return email;
        //não há uso do email e apelido inseridos
        return 0;
    }

    //retorna 0 se apelido não for achado. Se for achado, retorna 1
    //flag =0 indica "cadastrar" e flag = 1 indica "editar"

    private int contarApelido(Pessoa user, int flag){
        try{
            String sql;
            Cursor c;
            if(flag == 0){
                sql="SELECT COUNT("+Pessoa.colunas[2]+") FROM "+DBDunno.NOME_TABELAS[0]+
                    " WHERE "+Pessoa.colunas[2]+"=?";
                c = db.rawQuery(sql,new String[]{user.getnickName()});
            }
            else if(flag ==1){
                sql="SELECT COUNT("+Pessoa.colunas[2]+") FROM "+DBDunno.NOME_TABELAS[0]+
                    " WHERE NOT "+Pessoa.colunas[0]+"=? AND "+Pessoa.colunas[2]+"=?";
                c = db.rawQuery(sql, new String[]{Long.toString(user.getID()), user.getnickName()});
            }
            else //só por garantia...
                return -1;

            if(c.moveToNext()){
                int aux = c.getInt(c.getColumnIndex("COUNT("+Pessoa.colunas[2]+")"));
                System.out.printf("%nAchou %d pessoas com o apelido %s%n",aux,user.getnickName());
                if (aux > 0)
                    return 1;
                else
                    return 0;
            }
            else
                return -1;
        }catch(Exception e){
            Log.e("Contar apelidos",e.getMessage());
            return -1;
        }
    }
    //retorna 0 se email não for achado. Se for achado, retorna 2
    //flag =0 indica "cadastrar" e flag = 1 indica "editar"

    private int contarEmail(Pessoa user,int flag){
        try{
            String sql;
            Cursor c;
            if(flag == 0) {
                sql = "SELECT COUNT(" + Pessoa.colunas[3] + ") FROM " + DBDunno.NOME_TABELAS[0] +
                        " WHERE " + Pessoa.colunas[3] + "=?";
                c = db.rawQuery(sql, new String[]{user.getEmail()});
            }
            else if(flag ==1){
                sql="SELECT COUNT("+Pessoa.colunas[3]+") FROM "+DBDunno.NOME_TABELAS[0]+
                        " WHERE NOT "+Pessoa.colunas[0]+"=? AND "+Pessoa.colunas[3]+"=?";
                c = db.rawQuery(sql, new String[]{Long.toString(user.getID()), user.getEmail()});
            }
            else //só por garantia...
                return -1;

            if(c.moveToNext()){
                int aux= c.getInt(c.getColumnIndex("COUNT("+Pessoa.colunas[3]+")"));
                System.out.printf("%nAchou %d pessoas com o apelido %s%n",aux,user.getEmail());
                if (aux > 0)
                    return 2;
                else
                    return 0;
            }//Dunno az | cotoco kj
            else
                return -1;
        }catch(Exception e){
            Log.e("Contar emails",e.getMessage());
            return -1;
        }
    }

    //retorna: 0=sucesso | 1 = apelido já existente | 2= email já existente

    public Pessoa logar(String usuario){
        String comando ="SELECT * FROM "+ DBDunno.NOME_TABELAS[0] +
                " WHERE "+Pessoa.colunas[2]+"=? OR "+Pessoa.colunas[3]+" =?";
        Pessoa p = null;
        try {
            Cursor c = db.rawQuery(comando, new String[]{usuario, usuario});
            if (c.moveToNext()) 
                p = setarUsuario(c, 0);
        }catch (Exception e){
            Log.e("Logar", e.getMessage());
            return null;
        }
        return p;
    }
    public Pessoa buscarUsuario(long idUsuario){
        Pessoa usuario = null;
        String comando = "SELECT * FROM " +DBDunno.NOME_TABELAS[0]+
                " WHERE "+Pessoa.colunas[0]+"=?";
        try{
            Cursor c = db.rawQuery(comando, new String[]{""+idUsuario});
            if(c.moveToNext())
                usuario = setarUsuario(c, 0);
        }catch (Exception e){
            Log.e("Buscar UsuarioID",e.getMessage());
        }
        return usuario;
    }
    public List<Pessoa> listarUsuarios(){
        String sql= "SELECT * FROM Users";
        List<Pessoa> usuarios = new ArrayList<>();
        try{
            Cursor c = db.rawQuery(sql, null);
            if(c.moveToFirst()){
                Pessoa u;
                do{
                    u = setarUsuario(c, 0);
                    usuarios.add(u);
                }while(c.moveToNext());
            }
        }catch (Exception e){
            Log.e("DAO", e.getMessage());
        }
        return usuarios;
    }

    private ContentValues setarValoresUsuario(Pessoa user){
        ContentValues userValues = new ContentValues();
        userValues.put(Pessoa.colunas[1], user.getName());
        userValues.put(Pessoa.colunas[2], user.getnickName());
        userValues.put(Pessoa.colunas[3], user.getEmail());
        userValues.put(Pessoa.colunas[4],""+Hash.geraCodigo(user.getPassword()));

        return userValues;
    }
    private Pessoa setarUsuario(Cursor c, int ondeComecar){
        if(ondeComecar < 0)
            ondeComecar = 0;
        else if(ondeComecar >1)
            ondeComecar = 1;
        Pessoa p = new Pessoa();

        if(ondeComecar == 0)
            p.setId(c.getLong(c.getColumnIndex(Pessoa.colunas[ondeComecar++])));

        p.setName(c.getString(c.getColumnIndex(Pessoa.colunas[ondeComecar++])));
        p.setnickName(c.getString(c.getColumnIndex(Pessoa.colunas[ondeComecar++])));
        p.setEmail(c.getString(c.getColumnIndex(Pessoa.colunas[ondeComecar++])));
        p.setPassword(c.getString(c.getColumnIndex(Pessoa.colunas[ondeComecar])));
        return p;
    }
}
