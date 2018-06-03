package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Pessoa;

public class AlterarDadosUsuario_Activity extends AppCompatActivity {
    private EditText userName, userNick, userEmail, userPassword;
    private ImageButton saveUser, cancelUpdate;
    private Pessoa user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_usuario_);
        userName = (EditText) findViewById(R.id.txtUpdateUserName);
        userNick = (EditText) findViewById(R.id.txtUpdateUserNickName);
        userEmail = (EditText) findViewById(R.id.txtUpdateUserEmail);
        userPassword = (EditText) findViewById(R.id.txtUpdateUserPassword);
        saveUser = (ImageButton) findViewById(R.id.btnUpdate);
        cancelUpdate = (ImageButton) findViewById(R.id.btnCancelarUpdateUsuario);

        Bundle id = getIntent().getExtras();
        if(!id.isEmpty()) {
            user = MainActivity.usersDAO.buscarUsuario(id.getLong(Pessoa.colunas[0]));
            userName.setText(user.getName());
            userNick.setText(user.getnickName());
            userEmail.setText(user.getEmail());
        }
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camposNull())
                    return;
                user.setName(userName.getText().toString());
                user.setnickName(userNick.getText().toString());
                user.setEmail(userEmail.getText().toString());
                user.setPassword(userPassword.getText().toString());

                int res = MainActivity.usersDAO.editarDados(user);
                if(res ==0){
                    mostrarConsole(user);
                    Toast.makeText(getApplicationContext(),"Seus dados foram editados com" +
                            " sucesso", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    finish();
                }
                else if(res == 1) {
                    userNick.setText(user.getnickName());
                    Toast.makeText(getApplicationContext(), "Apelido " + user.getnickName() +
                            " já está em uso!", Toast.LENGTH_SHORT).show();
                }

                else if(res ==2) {
                    userEmail.setText(user.getEmail());
                    Toast.makeText(getApplicationContext(), "Email " + user.getEmail() +
                            " já está em uso!", Toast.LENGTH_SHORT).show();
                }
                else if(res == -2)
                    Toast.makeText(getApplicationContext(),"Senha errada",Toast.LENGTH_SHORT).show();
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
