package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Livro;


public class InnerJoinActivity extends AppCompatActivity {
    private EditText bookTitle;
    private ImageButton searchBook, cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_join);
        bookTitle = (EditText) findViewById(R.id.txtSearchBookTitle);
        searchBook = (ImageButton) findViewById(R.id.btnSearchBook);
        cancelar = (ImageButton) findViewById(R.id.btnBookSearchCancel);

        searchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Preencha o campo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Livro> achou = MainActivity.booksDAO.buscarLivros(bookTitle.getText().toString());
                if (achou.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Livro não encontrado",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(),"Achou o livro de título: "+bookTitle.getText().toString()+
                                "\na lista de está no console...", Toast.LENGTH_SHORT).show();
                for(Livro lista: achou){
                    System.out.printf("%n %s | %s | %s | %s | %s | %d %n",lista.getOwner(),lista.getEmail(),
                            lista.getTitle(), lista.getAuthor(), lista.getPublisher(), lista.getYear());
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}