package silva.relacaotabelas.bd.criar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG="DB Dunno";
    private String[] scriptSQLCreate;
    private String[] scriptSQLDelete;

    SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String[] scriptSQLCreate,
                 String[] scriptSQLDelete){
        super(context, nomeBanco, null, versaoBanco);
        this.scriptSQLCreate = scriptSQLCreate;
        this.scriptSQLDelete = scriptSQLDelete;
    }
    //criacao do banco
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Criando banco com sql");
        int qtdeScripts = scriptSQLCreate.length;
        // Executa cada sql passado como parametro
        for (int i = 0; i < qtdeScripts; i++) {
            String sql = scriptSQLCreate[i];
            Log.i(TAG, sql);
            // Cria o banco de dados executando o script de criacao
            db.execSQL(sql);
        }
    }
    // Mudou a versao...
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        Log.w(TAG, "Atualizando da versao " + versaoAntiga + " para " + novaVersao +
                "Todos os registros serao deletados.");
        int qtdScripts = scriptSQLDelete.length;
        for(int i=0; i < qtdScripts;i++) {
            Log.i(TAG, scriptSQLDelete[i]);
            // Deleta as tabelas...
            db.execSQL(scriptSQLDelete[i]);
        }
        // Cria novamente...
        onCreate(db);
    }
}
