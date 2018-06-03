package silva.relacaotabelas.atividades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import silva.relacaotabelas.R;

public class ResetarSenha_Activity extends AppCompatActivity {

    private Button resetar;
    private EditText txtResetUser, txtResetPassword, txtResetConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha_);
        resetar = (Button) findViewById(R.id.btnResetarSenha_NovaSenha);
        txtResetUser = (EditText) findViewById(R.id.txtResetarSenhaLogin);
        txtResetPassword = (EditText) findViewById(R.id.txtResetarNovaSenha);
        txtResetConfirmPassword = (EditText) findViewById(R.id.txtResetarConfirmarSenha);
    }
    private boolean finalizar(String usuario, String novaSenha){
        if(MainActivity.usersDAO.esqueciSenha(usuario, novaSenha)){
            Toast.makeText(getApplicationContext(),"Senha resetada!",Toast.LENGTH_SHORT).show();
            txtResetConfirmPassword.setText("");
            txtResetPassword.setText("");
            txtResetUser.setText("");
            finish();
            return true;
        }
        else {
            Toast.makeText(getApplicationContext(), "Deu ruim", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean confirmarReset_Senha(View view){
        String usuario, novaSenha, confirmarSenha;
        if( camposNull() )
            return false;
        usuario = txtResetUser.getText().toString();
        novaSenha = txtResetPassword.getText().toString();
        confirmarSenha = txtResetConfirmPassword.getText().toString();
        if(novaSenha.equals(confirmarSenha))
            return finalizar(usuario, novaSenha);
        Toast.makeText(getApplicationContext(),"As senhas não estão iguais! Ajeita aí",Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean camposNull() {
        boolean auxU, auxS, auxCS;
        auxU = txtResetUser.getText().toString().isEmpty();
        auxS = txtResetPassword.getText().toString().isEmpty();
        auxCS = txtResetConfirmPassword.getText().toString().isEmpty();
        if (auxU || auxS || auxCS){
            Toast.makeText(getApplicationContext(),"Preencha os campos!",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
