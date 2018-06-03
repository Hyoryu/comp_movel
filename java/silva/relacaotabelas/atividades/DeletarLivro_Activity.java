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

public class DeletarLivro_Activity extends AppCompatActivity {
    private long bookUserID;
    private EditText bookSearch, bookTitle, bookAuthor, bookPublisher, bookYear;
    private Button buscar, deletar;
    private Livro book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletar_livro_);
        bookSearch = (EditText) findViewById(R.id.txtDeletarLivro_Buscar);

        bookTitle = (EditText) findViewById(R.id.txtDeletarLivro_Title);
        bookAuthor = (EditText) findViewById(R.id.txtDeletarLivro_Author);
        bookPublisher = (EditText) findViewById(R.id.txtDeletarLivro_Publisher);
        bookYear = (EditText) findViewById(R.id.txtDeletarLivro_Year);
        buscar = (Button) findViewById(R.id.btnDeletarLivro_Buscar);
        deletar = (Button) findViewById(R.id.btnDeletarLivro_Deletar);

        Bundle userID = getIntent().getExtras();
        if(!userID.isEmpty())
            bookUserID = userID.getLong(Livro.colunas[1]);

    }
    public void deletarLivro_Buscar(View view){
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
    public void deletarLivro_Deletar(View view){
        boolean [] aux = new boolean[]{bookSearch.getText().toString().isEmpty(),bookTitle.getText().toString().isEmpty(),
            bookAuthor.getText().toString().isEmpty(),bookPublisher.getText().toString().isEmpty(),bookYear.getText().toString().isEmpty()
        };
        if(aux[0] || aux[1] || aux[2] || aux[3] || aux[4])
            return;
        else
            mostrarAlerta(bookUserID, book.getTitle());
    }
    public void mostrarAlerta(final long userID,final String titulo){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("OBJECTION!!!");
        alerta.setMessage("Deseja mesmo apagar esse livro?");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if(MainActivity.booksDAO.apagarLivro(userID ,titulo))
                   Toast.makeText(getApplicationContext(),"Livro deletado",Toast.LENGTH_SHORT).show();
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
            deletar.setVisibility(View.VISIBLE);
        }
        else{
            bookTitle.setVisibility(View.INVISIBLE);
            bookAuthor.setVisibility(View.INVISIBLE);
            bookPublisher.setVisibility(View.INVISIBLE);
            bookYear.setVisibility(View.INVISIBLE);
            deletar.setVisibility(View.INVISIBLE);
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
