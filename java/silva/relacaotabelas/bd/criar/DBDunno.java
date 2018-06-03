package silva.relacaotabelas.bd.criar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import silva.relacaotabelas.bd.model.Hash;
import silva.relacaotabelas.bd.model.Livro;
import silva.relacaotabelas.bd.model.Pessoa;

public class DBDunno {
    public static final String NOME_BANCO ="dbDunno";
    private static final int VERSAO_BANCO = 1;
    //nome das tabelas

    public static final String[] NOME_TABELAS = new String[]{"Users","Books"};
    private static final String[] SCRIPT_DATABASE_DELETE = new String[]{"DROP TABLE IF EXISTS Users",
            "DROP TABLE IF EXISTS Books"};

    private static final String tbUsers =
            "CREATE TABLE "+ NOME_TABELAS[0]+
            "("+Pessoa.colunas[0]+" integer primary key autoincrement,"+
            Pessoa.colunas[1]+" text NOT NULL," + Pessoa.colunas[2]+" text NOT NULL,"+
            Pessoa.colunas[3]+" text NOT NULL,"+ Pessoa.colunas[4]+" text NOT NULL" +
            ");";

    private static final String tbBooks=
            "CREATE TABLE "+ NOME_TABELAS[1]+
            "("+Livro.colunas[0]+" integer primary key autoincrement,"+
            Livro.colunas[1]+ " integer, "+Livro.colunas[2]+" text not null,"+
            Livro.colunas[3] +" text not null,"+Livro.colunas[4]+" text not null,"+
            Livro.colunas[5]+" integer, FOREIGN KEY("+Livro.colunas[1]+")"+
            " REFERENCES "+NOME_TABELAS[0] +"("+Pessoa.colunas[0]+")" +
            ");";
    //Inserir um valor em cada tabela para testar
    private static final String senhaTeste = Long.toString(Hash.geraCodigo("funfou"));

    private static String[] testandoInsercao = new String[]{
            "INSERT INTO Users(UserName, UserNickname, UserEmail, UserPassword)\n" +
            "VALUES ('Felipe Silva', 'Hyoryu', 'silva_fellipe@outlook.com','"+senhaTeste+"');",

            "INSERT INTO Users(UserName, UserNickname, UserEmail, UserPassword)\n" +
            "VALUES ('Felipe', 'Zihark', 'felipe_keyblade@hotmail.com','"+senhaTeste+"');",

            "INSERT INTO Books(UserID, BookName, BookAuthor, BookPublisher, BookYear)\n" +
            "VALUES (1, 'Morte: edição definitiva','Neil Gaiman', 'Panini', 2017);",

            "INSERT INTO Books(UserID, BookName, BookAuthor, BookPublisher, BookYear)\n" +
            "VALUES (1, 'Zelda: Ocarina of Time','Akira Himekawa', 'Panini', 2018);",

            "INSERT INTO Books(UserID, BookName, BookAuthor, BookPublisher, BookYear)\n" +
            "VALUES (2, 'Morte: edição definitiva','Neil Gaiman', 'Panini', 2017);",

            "INSERT INTO Books(UserID, BookName, BookAuthor, BookPublisher, BookYear)\n" +
            "VALUES (2, 'Zelda: Ocarina of Time','Akira Himekawa', 'Panini', 2018);"
    };
    /*  Script que criará as tabelas
     *  Colunas das tabelas
     *  Users: Pessoa.colunas{PK: "UserID","UserName","UserNickname","UserEmail","UserPassword"};
     *  Books: Livro.colunas {PK: "BookID",FK: "UserID", "BookName","BookAuthor","BookPublisher","BookYear"};
     */
    private static final String[] SCRIPT_CRIACAO_TABELAS = new String[]{
        tbUsers, tbBooks,testandoInsercao[0], testandoInsercao[1], testandoInsercao[2],
        testandoInsercao[3], testandoInsercao[4], testandoInsercao[5]
    };

    //fim das variáveis auxiliares
    protected SQLiteDatabase db;
    private SQLiteHelper dbHelper;

    public DBDunno(Context ctx){
        dbHelper = new SQLiteHelper(ctx, NOME_BANCO, VERSAO_BANCO, SCRIPT_CRIACAO_TABELAS, SCRIPT_DATABASE_DELETE);
        db = dbHelper.getWritableDatabase();
    }

    public void fechar(){
        if(db != null)
            db.close();
        if(dbHelper != null)
            dbHelper.close();
    }
}