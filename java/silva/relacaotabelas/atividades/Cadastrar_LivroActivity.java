package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Livro;

public class Cadastrar_LivroActivity extends AppCompatActivity {
    private long bookUserID;
    private EditText bookTitle, bookAuthor, bookPublisher, bookYear;
    private ImageButton saveBook, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_livro);
        bookTitle = (EditText) findViewById(R.id.txtBookTitle);
        bookAuthor = (EditText) findViewById(R.id.txtBookAuthor);
        bookPublisher = (EditText) findViewById(R.id.txtBookPublisher);
        bookYear = (EditText) findViewById(R.id.txtBookYear);

        saveBook = (ImageButton) findViewById(R.id.btnBookSave);
        cancelar = (ImageButton) findViewById(R.id.btnBookCancel);

        Bundle userID = getIntent().getExtras();
        if(!userID.isEmpty()) {
            bookUserID = userID.getLong(Livro.colunas[1]);
            System.out.printf("%nID do usuário: %d%n",bookUserID);
        }

        saveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camposNull())
                    return;
                Livro book = new Livro();
                book.setUserId(bookUserID);
                book.setTitle(bookTitle.getText().toString());
                book.setAuthor(bookAuthor.getText().toString());
                book.setPublisher(bookPublisher.getText().toString());
                book.setYear(Integer.parseInt(bookYear.getText().toString()));
                limparCampos();
                if(MainActivity.booksDAO.salvarLivro(book)) {
                    System.out.printf("%nCadastrando livro:  %d | %s | %s | %s | %d %n",book.getUserId(),
                            book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear());
                    Toast.makeText(getApplicationContext(), book.getTitle() + " cadastrado!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void limparCampos(){
        bookTitle.setText("");
        bookAuthor.setText("");
        bookPublisher.setText("");
        bookYear.setText("");
    }
    private boolean camposNull() {
        boolean titulo, autor, editora, ano;
        titulo = bookTitle.getText().toString().isEmpty();
        autor = bookAuthor.getText().toString().isEmpty();
        editora = bookPublisher.getText().toString().isEmpty();
        ano = bookYear.getText().toString().isEmpty();
        //campos vazios
        if (titulo || autor || editora || ano){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos!",Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }
}