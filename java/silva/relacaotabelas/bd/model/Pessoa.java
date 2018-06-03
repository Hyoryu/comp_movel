package silva.relacaotabelas.bd.model;

import silva.relacaotabelas.bd.criar.DBDunno;

public class Pessoa {
    public static final String[] colunas = new String[]{"UserID","UserName", "UserNickname","UserEmail","UserPassword"};

    //Ijoin é usada para criar a relação entre as tabelas Users e Books
    public static final String[] Ijoin = new String[]{DBDunno.NOME_TABELAS[0]+"."+colunas[0],
    DBDunno.NOME_TABELAS[0]+"."+colunas[2],DBDunno.NOME_TABELAS[0]+"."+colunas[3]
    };

    private long _id;
    private String name, nickName, email, password;

    public Pessoa(){}
    //fazer login
    public Pessoa(String apelido, String senha){
        nickName = apelido;
        password = senha;
    }
    //salvar no bd
    public Pessoa(long id, String nome, String apelido, String _email, String senha){
        _id =id;
        name = nome;
        nickName = apelido;
        email = _email;
        password = senha;
    }

    //getters e setters
    public void setId(long id){_id = id;}
    public void setName(String nome){ name = nome;};
    public void setnickName(String apelido){nickName = apelido;}
    public void setEmail(String _email){email = _email;}
    public void setPassword(String senha){password = senha;}

    public long getID(){return _id;}
    public String getName(){return name;}
    public String getnickName(){return nickName;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
}
