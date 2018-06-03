package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Hash;
import silva.relacaotabelas.bd.model.Pessoa;

public class AlterarSenhaUsuario_Actitivy extends AppCompatActivity {
    private Pessoa user;
    private Button resetar;
    private EditText txtSenhaAtual, txtAlterarSenha, txtAlterarCSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha_usuario__actitivy);
        Bundle id = getIntent().getExtras();
        resetar = (Button) findViewById(R.id.btnAlterarSenha_NovaSenha);
        txtSenhaAtual = (EditText) findViewById(R.id.txtAlterar_Senha);
        txtAlterarSenha = (EditText) findViewById(R.id.txtAlterar_NovaSenha);
        txtAlterarCSenha = (EditText) findViewById(R.id.txtAlterar_ConfirmarSenha);
        if(id.isEmpty()){
            Toast.makeText(getApplicationContext(),"Mano, deu ruim",Toast.LENGTH_SHORT).show();
            finish();
        }
        user = MainActivity.usersDAO.buscarUsuario(id.getLong(Pessoa.colunas[0]));
    }
    private boolean finalizar(String novaSenha){
        if(MainActivity.usersDAO.editarSenha(user.getID(), novaSenha)){
            Toast.makeText(getApplicationContext(),"Senha alterada com sucesso",Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return false;
    }
    public boolean confirmarAlteracaoSenha(View view){
        if (camposNull())
            return false;
        String senhaAtual;
        senhaAtual = Long.toString( Hash.geraCodigo(txtSenhaAtual.getText().toString() ) );
        if(!senhaAtual.equals(user.getPassword())){
            Toast.makeText(getApplicationContext(),"Senha inserida não bate com a cadastrada",Toast.LENGTH_SHORT).show();
            return false;
        }
        String novaSenha, confirmar;
        novaSenha = txtAlterarSenha.getText().toString();
        confirmar = txtAlterarCSenha.getText().toString();
        if(novaSenha.equals(confirmar))
            return finalizar(novaSenha);
        else {
            Toast.makeText(getApplicationContext(),"As senhas (nova e confirmar) não estão iguais! Ajeita aí",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean camposNull(){
        boolean auxS, auxNS, auxCS;
        auxS = txtSenhaAtual.getText().toString().isEmpty();
        auxNS = txtAlterarSenha.getText().toString().isEmpty();
        auxCS = txtAlterarCSenha.getText().toString().isEmpty();
        if(auxS || auxNS || auxCS){
            Toast.makeText(getApplicationContext(),"Preencha tudo, cotoco!",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}