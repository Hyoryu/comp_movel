package silva.relacaotabelas.bd.gerenciar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import silva.relacaotabelas.bd.criar.DBDunno;
import silva.relacaotabelas.bd.model.*;

public class TbBooksDAO{
    private static final String TAG = "Tabela Book";
    public SQLiteDatabase db;

    /*  Colunas da tabela
     *  Books: Livro.colunas {PK: "BookID",FK: "UserID", "BookName","BookAuthor","BookPublisher","BookYear"};*/
    public TbBooksDAO(Context ctx){
        db = ctx.openOrCreateDatabase(DBDunno.NOME_BANCO, Context.MODE_PRIVATE, null);
    }
    public boolean salvarLivro(Livro book){
        ContentValues valoresLivro = new ContentValues();
        valoresLivro.put(Livro.colunas[1], book.getUserId());
        valoresLivro.put(Livro.colunas[2], book.getTitle());
        valoresLivro.put(Livro.colunas[3], book.getAuthor());
        valoresLivro.put(Livro.colunas[4], book.getPublisher());
        valoresLivro.put(Livro.colunas[5], book.getYear());
        try{
            db.insert(DBDunno.NOME_TABELAS[1], "", valoresLivro);
        }catch (Exception e){
            Log.e("Erro ao salvar livro",e.getMessage());
            return false;
        }
        return true;
    }
    public Livro buscarLivro(long userID, String titulo){
        Livro livro = null;
        String sql = "SELECT * FROM "+DBDunno.NOME_TABELAS[1]+" WHERE "
                +Livro.colunas[1]+"="+userID+" AND "+ Livro.colunas[2]+" LIKE '%"+titulo+"%'";
        Cursor c = db.rawQuery(sql, null);
        if(c.moveToNext()){
            livro = new Livro();
            livro.setId(c.getLong(c.getColumnIndex(Livro.colunas[0])));
            livro.setUserId(c.getLong(c.getColumnIndex(Livro.colunas[1])));
            livro.setTitle(c.getString(c.getColumnIndex(Livro.colunas[2])));
            livro.setAuthor(c.getString(c.getColumnIndex(Livro.colunas[3])));
            livro.setPublisher(c.getString(c.getColumnIndex(Livro.colunas[4])));
            livro.setYear(c.getInt(c.getColumnIndex(Livro.colunas[5])));
        }
        if(livro == null)
            System.out.printf("%nBuscar Livro: Não achou um livro. Por isso, livro = null!%n");
        return livro;
    }
    public boolean editarLivro(Livro book){
        if(book == null)
            return false;
        ContentValues valoresLivro = new ContentValues();
        //editar apenas título, autor, editora e ano
        valoresLivro.put(Livro.colunas[2], book.getTitle());
        valoresLivro.put(Livro.colunas[3], book.getAuthor());
        valoresLivro.put(Livro.colunas[4], book.getPublisher());
        valoresLivro.put(Livro.colunas[5], book.getYear());
        String whereClause = Livro.colunas[0]+"=? AND "+Livro.colunas[1]+"=?";
        String[] whereArgs ={Long.toString(book.getId()),Long.toString(book.getUserId())};
        try {
            db.update(DBDunno.NOME_TABELAS[1], valoresLivro, whereClause, whereArgs);
        }catch (Exception e){
            Log.e("Erro ao editar",e.getMessage());
            return false;
        }
        return true;
    }

    public boolean apagarLivro(long userID, String titulo){
        Livro book = buscarLivro(userID,titulo);
        if(book == null)
            return false;
        String whereClause=Livro.colunas[0]+"=? AND "+Livro.colunas[1]+" =? AND "+
            Livro.colunas[2]+" LIKE '%"+ book.getTitle()+"%'";
        String[] whereArgs = new String[]{Long.toString(book.getId()),
            Long.toString(book.getUserId())};
        try{
            db.delete(DBDunno.NOME_TABELAS[1],whereClause, whereArgs);
            return true;
        }catch(Exception e){
            Log.e("Apagar Livro", e.getMessage());
            return false;
        }
    }
    public List<Livro> listarLivros(){
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM Books";
        try {
            Cursor c = db.rawQuery(sql, null);
            if (c.moveToFirst()) {
               //antes: Livro b = new Livro();

                do {
                    Livro b = new Livro();
                    b.setId(c.getLong(c.getColumnIndex(Livro.colunas[0])));
                    b.setUserId(c.getLong(c.getColumnIndex(Livro.colunas[1])));
                    b.setTitle(c.getString(c.getColumnIndex(Livro.colunas[2])));
                    b.setAuthor(c.getString(c.getColumnIndex(Livro.colunas[3])));
                    b.setPublisher(c.getString(c.getColumnIndex(Livro.colunas[4])));
                    b.setYear(c.getInt(c.getColumnIndex(Livro.colunas[5])));

                    livros.add(b);
                }while(c.moveToNext());
            }
        }catch (Exception e){
            Log.e("Listar Livros", e.getMessage());
        }
        return livros;
    }

    public List<Livro> buscarLivros(String nomeLivro){
        List<Livro> livros = new ArrayList<>();
        try {
            int index = setarIndex(nomeLivro);

            String comando ="SELECT "+Pessoa.Ijoin[1]+","+Pessoa.Ijoin[2]+","+Livro.Ijoin[1]+","+Livro.Ijoin[2]+","+
                    Livro.Ijoin[3]+","+Livro.Ijoin[4] +" FROM "+DBDunno.NOME_TABELAS[1]+" INNER JOIN "+DBDunno.NOME_TABELAS[0]+
                    " ON "+Pessoa.Ijoin[0]+"="+Livro.Ijoin[0]+ " WHERE "+Livro.Ijoin[1]+" LIKE '%"+nomeLivro+"%' AND "+
                    Pessoa.Ijoin[0]+"="+Livro.Ijoin[0];
            Cursor c = db.rawQuery(comando, null);
            if (c.moveToFirst()) {
                do {
                    Livro b = new Livro();
                    b.setOwner(c.getString(c.getColumnIndex(Pessoa.colunas[2])));
                    b.setEmail(c.getString(c.getColumnIndex(Pessoa.colunas[3])));
                    b.setTitle(c.getString(c.getColumnIndex(Livro.colunas[2])));
                    b.setAuthor(c.getString(c.getColumnIndex(Livro.colunas[3])));
                    b.setPublisher(c.getString(c.getColumnIndex(Livro.colunas[4])));
                    b.setYear(c.getInt(c.getColumnIndex(Livro.colunas[5])));
                    livros.add(b);
                } while (c.moveToNext());
            }
        }catch (Exception e){
            Log.e("JOIN", e.getMessage());
        }
        return livros;
    }

    private int setarIndex(String filtro){
        int idx;
        if(filtro.equals("Título"))
            idx = 1;
        else if(filtro.equals("Autor"))
            idx = 2;
        else if(filtro.equals("Editora"))
            idx = 3;
        else
            idx = 1;
        return idx;
    }
    public void fechar(){
        if(db!= null)
            db.close();
    }
}