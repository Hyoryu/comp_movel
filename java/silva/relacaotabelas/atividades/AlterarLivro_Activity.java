package silva.relacaotabelas.atividades;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Livro;

public class AlterarLivro_Activity extends AppCompatActivity {
    private long bookUserID;
    private EditText bookSearch, bookTitle, bookAuthor, bookPublisher, bookYear;
    private Button buscar, editar;
    private Livro book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_livro_);
        bookSearch = (EditText) findViewById(R.id.txtEditarLivro_Buscar);
        bookTitle = (EditText) findViewById(R.id.txtEditarLivro_Title);
        bookAuthor = (EditText) findViewById(R.id.txtEditarLivro_Author);
        bookPublisher = (EditText) findViewById(R.id.txtEditarLivro_Publisher);
        bookYear = (EditText) findViewById(R.id.txtEditarLivro_Year);
        buscar = (Button) findViewById(R.id.btnEditarLivro_Buscar);
        editar = (Button) findViewById(R.id.btnEditarLivro_Editar);

        Bundle userID = getIntent().getExtras();
        if(!userID.isEmpty())
            bookUserID = userID.getLong(Livro.colunas[1]);

    }
    public void EditarLivro_Buscar(View view){
        if(bookSearch.getText().toString().isEmpty())
            return;
        book = MainActivity.booksDAO.buscarLivro(bookUserID ,bookSearch.getText().toString());
        if(book == null)
            return;
        setarVisibilidade(true);
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookPublisher.setText(book.getPublisher());
        bookYear.setText(String.valueOf(book.getYear()));
    }
    public void EditarLivro_Editar(View view){
        boolean [] aux = new boolean[]{bookSearch.getText().toString().isEmpty(),bookTitle.getText().toString().isEmpty(),
                bookAuthor.getText().toString().isEmpty(),bookPublisher.getText().toString().isEmpty(),bookYear.getText().toString().isEmpty()
        };
        if(aux[0] || aux[1] || aux[2] || aux[3] || aux[4])
            return;
        setarLivro();
        mostrarAlerta(book);
    }

    public void setarLivro(){
        book.setTitle(bookTitle.getText().toString());
        book.setAuthor(bookAuthor.getText().toString());
        book.setPublisher(bookPublisher.getText().toString());
        book.setYear(Integer.parseInt( bookYear.getText().toString() ));
    }
    public void mostrarAlerta(final Livro livro){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("OBJECTION!!!");
        alerta.setMessage("Deseja mesmo editar os dados desse livro?");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(MainActivity.booksDAO.editarLivro(livro))
                    Toast.makeText(getApplicationContext(),"Livro editado",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),"Mano, deu ruim",Toast.LENGTH_SHORT).show();
                setarVisibilidade(false);
            }
        });
        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                limparCampos();
                setarVisibilidade(false);
            }
        });
        alerta.show();
    }
    public void setarVisibilidade(boolean cond){
        if(cond){
            bookTitle.setVisibility(View.VISIBLE);
            bookAuthor.setVisibility(View.VISIBLE);
            bookPublisher.setVisibility(View.VISIBLE);
            bookYear.setVisibility(View.VISIBLE);
            editar.setVisibility(View.VISIBLE);
        }
        else{
            bookTitle.setVisibility(View.INVISIBLE);
            bookAuthor.setVisibility(View.INVISIBLE);
            bookPublisher.setVisibility(View.INVISIBLE);
            bookYear.setVisibility(View.INVISIBLE);
            editar.setVisibility(View.INVISIBLE);
        }
    }
    public void limparCampos(){
        bookSearch.setText("");
        bookTitle.setText("");
        bookAuthor.setText("");
        bookPublisher.setText("");
        bookYear.setText("");
    }
}

