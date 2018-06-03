package silva.relacaotabelas.bd.model;

import silva.relacaotabelas.bd.criar.DBDunno;

public class Livro{
    public static final String[] colunas = new String[]{"BookID","UserID", "BookName","BookAuthor",
            "BookPublisher","BookYear"
    };
    public static final String[] Ijoin = new String[]{DBDunno.NOME_TABELAS[1]+"."+colunas[1],
            DBDunno.NOME_TABELAS[1]+"."+colunas[2],DBDunno.NOME_TABELAS[1]+"."+colunas[3],
            DBDunno.NOME_TABELAS[1]+"."+colunas[4],DBDunno.NOME_TABELAS[1]+"."+colunas[5]
    };
    private String title, author, publisher, owner, email;
    private long _id, _userId;
    //_id: chave primarica | _userID: chave estrangeira. Referencia Pessoa._id
    private int year;

    public Livro(){}
    public Livro(String titulo, String autor, String editora, int ano){
        setTitle(titulo);
        setAuthor(autor);
        setPublisher(editora);
        setYear(ano);
    }
    //salvar no bd: se o txtAnoPublicacao = null, ano=0
    public Livro(long id, long userId, String titulo, String autor, String editora, int ano){
        setId(id);
        setUserId(userId);
        setTitle(titulo);
        setAuthor(autor);
        setPublisher(editora);
        setYear(ano);
        /*se ano.isEmpty() -> Year = 0; senao -> Year = ano
        if(ano.isEmpty())
            setYear(0);
        else
            setYear(Integer.parseInt(ano));*/
    }
    //getters e setters
    public void setId(long id){_id = id;}
    public void setUserId(long userID){_userId = userID;}
    public void setTitle(String titulo){title = titulo;}
    public void setAuthor(String autor){author = autor;}
    public void setPublisher(String editora){publisher = editora;}
    public void setYear(int ano){year = ano;}
    public void setOwner(String dono){owner = dono;}
    public void setEmail(String _email){email = _email;}

    public long getId(){return _id;}
    public long getUserId(){return _userId;}
    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public String getPublisher(){return publisher;}
    public int getYear(){return year;}
    public String getOwner(){return owner;}
    public String getEmail(){return email;}
}
