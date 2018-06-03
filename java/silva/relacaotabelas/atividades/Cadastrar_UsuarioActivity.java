package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Pessoa;

public class Cadastrar_UsuarioActivity extends AppCompatActivity {
    private EditText userName, userNick, userEmail, userPassword;
    private ImageButton saveUser, cancelarCadastroUsario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar__usuario);
        userName = (EditText) findViewById(R.id.txtUserName);
        userNick = (EditText) findViewById(R.id.txtUserNickName);
        userEmail = (EditText) findViewById(R.id.txtUserEmail);
        userPassword = (EditText) findViewById(R.id.txtUserPassword);
        saveUser = (ImageButton) findViewById(R.id.btnSalvar);
        cancelarCadastroUsario = (ImageButton) findViewById(R.id.btnCancelarCadastroUsuario);

        cancelarCadastroUsario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pessoa usuario = new Pessoa();
                if (camposNull())
                    return;
                usuario.setName(userName.getText().toString());
                usuario.setnickName(userNick.getText().toString());
                usuario.setEmail(userEmail.getText().toString());
                usuario.setPassword(userPassword.getText().toString());
                int res = MainActivity.usersDAO.cadastrarUsuario(usuario);
                if (res == 0) {
                    mostrarConsole(usuario);
                    Toast.makeText(getApplicationContext(), usuario.getnickName() +
                            " cadastrado", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    finish();
                } else if (res == 1) {
                    userNick.setText("");
                    Toast.makeText(getApplicationContext(), "Apelido " + usuario.getnickName() +
                            " já está em uso!", Toast.LENGTH_SHORT).show();
                } else if (res == 2){
                    userEmail.setText("");
                    Toast.makeText(getApplicationContext(), "Email " + usuario.getEmail() +
                            " já está em uso!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Deu ruim para inserir." +
                            "\nProblema no banco de dados",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean camposNull() {
        boolean nome, nick, email, senha;
        nome = userName.getText().toString().isEmpty();
        nick = userNick.getText().toString().isEmpty();
        email = userEmail.getText().toString().isEmpty();
        senha = userPassword.getText().toString().isEmpty();
        //campos vazios
        if (nome || nick || email || senha){
            Toast.makeText(getApplicationContext(),"Preencha todos os campos!",Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }
    private void mostrarConsole(Pessoa usuario){
        System.out.printf("%n %d | %s | %s | %s | %s%n",usuario.getID(),
                usuario.getName(),usuario.getnickName(), usuario.getEmail(),
                usuario.getPassword()
        );
    }
    private void limparCampos(){
        userName.setText("");
        userNick.setText("");
        userEmail.setText("");
        userPassword.setText("");
    }
}
