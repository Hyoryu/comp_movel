package silva.relacaotabelas.atividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.criar.DBDunno;
import silva.relacaotabelas.bd.gerenciar.TbBooksDAO;
import silva.relacaotabelas.bd.gerenciar.TbUsersDAO;
import silva.relacaotabelas.bd.model.Hash;
import silva.relacaotabelas.bd.model.Pessoa;

public class MainActivity extends AppCompatActivity {
    //banco de dados
    private DBDunno criarBancoDados;
    public static TbBooksDAO booksDAO;
    public static TbUsersDAO usersDAO;
    private EditText txtUsuario, txtSenha;
    private Button btnLogar, btnCadastrar;
    private Pessoa usuario;
    //logou = true para login com sucesso senão logou = false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarBancoDados = new DBDunno(this);
        booksDAO = new TbBooksDAO(this);
        usersDAO = new TbUsersDAO(this);
        txtUsuario = (EditText) findViewById(R.id.txtNome);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnLogar = (Button) findViewById(R.id.btnLogin);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
    }
    public void logar(View view){
        if(!fazerLogin())
            return;
        Intent it = new Intent(this, EscolherActivity.class);
        it.putExtra(Pessoa.colunas[0], usuario.getID());
        startActivity(it);
    }
    public void cadastrar(View view){
        Intent it = new Intent(this, Cadastrar_UsuarioActivity.class);
        startActivity(it);
    }
    public void resetarSenha(View view){
        Intent it = new Intent(this, ResetarSenha_Activity.class);
        startActivity(it);
    }
    private boolean isTxtCampsEmpty(){
        if(txtUsuario.getText().toString().isEmpty() || txtSenha.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Preencha os campos!!!",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    private boolean fazerLogin() {
        if (isTxtCampsEmpty())
            return false;
        //buscar usuario de acordo com o apelido ou o email
        String cliente = txtUsuario.getText().toString();
        String senha = "" + Hash.geraCodigo(txtSenha.getText().toString());
        usuario = usersDAO.logar(cliente);
        txtUsuario.setText("");
        txtSenha.setText("");
        if (usuario != null && (cliente.equals(usuario.getnickName()) || cliente.equals(usuario.getEmail())) &&
                senha.equals(usuario.getPassword()))
            return true;
        else{
            Toast.makeText(getApplicationContext(),"Usuário/Senha inválido",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}