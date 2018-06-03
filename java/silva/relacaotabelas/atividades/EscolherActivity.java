package silva.relacaotabelas.atividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.net.Inet4Address;
import java.util.List;

import silva.relacaotabelas.R;
import silva.relacaotabelas.bd.model.Livro;
import silva.relacaotabelas.bd.model.Pessoa;

public class EscolherActivity extends AppCompatActivity {
    private Pessoa usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher);
        //receber o ID do usuário logado
        Bundle gettingUserID = getIntent().getExtras();
        if(!gettingUserID.isEmpty())
            usuario = MainActivity.usersDAO.buscarUsuario(gettingUserID.getLong(Pessoa.colunas[0]));
        if(usuario != null)
            mostrarUsuario(usuario);
    }

    public void escolherOpcao(View view){
        if(view.getId() == R.id.tbUsers)
            mostrarUsuariosCadastrados();
        else if(view.getId() == R.id.btnAlterarDados){
            Intent it = new Intent(this, AlterarDadosUsuario_Activity.class);
            it.putExtra(Pessoa.colunas[0], usuario.getID());
            startActivity(it);
        }
        else if(view.getId() == R.id.btnDeletarUsuario){
            if(MainActivity.usersDAO.deletarUsuario(usuario)) {
                Toast.makeText(getApplicationContext(), "Deletou", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(),"Deu ruim",Toast.LENGTH_SHORT).show();
        }
        else if(view.getId() == R.id.btnAlterarSenha_UsuarioLogado){
            Intent it = new Intent(this, AlterarSenhaUsuario_Actitivy.class);
            it.putExtra(Pessoa.colunas[0], usuario.getID());
            startActivity(it);
        }
        else if(view.getId() == R.id.btnEditarLivro){
            if(usuario == null) {
                Toast.makeText(getApplicationContext(), "Deu ruim :/\nNão foi possível pegar a ID do usuário.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent it = new Intent(this, AlterarLivro_Activity.class);
            it.putExtra(Livro.colunas[1], usuario.getID());
            startActivity(it);
        }
        else if(view.getId() == R.id.btnDeletarLivro){
            if(usuario == null) {
                Toast.makeText(getApplicationContext(), "Deu ruim :/\nNão foi possível pegar a ID do usuário.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent it = new Intent(this, DeletarLivro_Activity.class);
            it.putExtra(Livro.colunas[1], usuario.getID());
            startActivity(it);
        }
        else if(view.getId() == R.id.tbBooks)
            mostrarLivrosCadastrados();
        else if(view.getId() == R.id.btnCadastrarLivro){
            if(usuario == null) {
                Toast.makeText(getApplicationContext(), "Deu ruim :/\nNão foi possível pegar a ID do usuário.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent it = new Intent(this, Cadastrar_LivroActivity.class);
            it.putExtra(Livro.colunas[1], usuario.getID());
            startActivity(it);
        }
        else if(view.getId() == R.id.btnInnerJoin){
            Intent intent = new Intent(this, InnerJoinActivity.class);
            startActivity(intent);
        }
    }

    private void mostrarInnerJoin(){
        List<Livro> achou = MainActivity.booksDAO.buscarLivros("Morte");
        if(achou.isEmpty())
            return;
        for(Livro lista: achou){
            System.out.printf("%n %s | %s | %s | %s | %s | %d %n",lista.getOwner(),lista.getEmail(),
                    lista.getTitle(), lista.getAuthor(), lista.getPublisher(), lista.getYear());
        }
    }

    private void mostrarLivrosCadastrados(){
        List<Livro> livrosCadastrados = MainActivity.booksDAO.listarLivros();
        if(livrosCadastrados.isEmpty())
            return;
        for(Livro i: livrosCadastrados){
            System.out.printf("%n %d | %d | %s | %s | %s | %d %n",i.getId(),i.getUserId(),
                    i.getTitle(), i.getAuthor(), i.getPublisher(), i.getYear());
        }
    }

    private void mostrarUsuario(Pessoa usuario){
        if(usuario == null)
            return;
        System.out.printf("%n %d | %s | %s | %s | %s%n",usuario.getID(),
                usuario.getName(),usuario.getnickName(), usuario.getEmail(),
                usuario.getPassword()
        );
    }
    private boolean mostrarUsuariosCadastrados(){
        List<Pessoa> usuariosCadastrados = MainActivity.usersDAO.listarUsuarios();
        if(usuariosCadastrados.isEmpty())
            return false;
        for(Pessoa i:usuariosCadastrados) {
            mostrarUsuario(i);
        }
        return true;
    }
}
