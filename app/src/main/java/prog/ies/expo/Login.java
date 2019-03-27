package prog.ies.expo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import prog.ies.expo.entidades.ConexionSQLiteHelper;
import prog.ies.expo.entidades.AdminContract;

public class Login extends AppCompatActivity {
    private EditText etUsuario, etContra;
    private Button ingresar;
    private final static int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etUsuario.setText("");
        etContra = (EditText) findViewById(R.id.etContra);
        etContra.setText("");
        ingresar = (Button) findViewById(R.id.bIngresar);

        permisos();
        eventos();
    }

    private void permisos() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void eventos() {
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });
    }

    private void ingresar(){
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String usuario = etUsuario.getText().toString();
        String contra = etContra.getText().toString();

        String [] columnas = {
                BaseColumns._ID,
                AdminContract.AdminEntry.USUARIO
        };
        String select = AdminContract.AdminEntry.USUARIO + "= ?";
        String[] selectArgs = { usuario };
        Cursor cursor = db.query(AdminContract.AdminEntry.NOMBRE_TABLA,
                columnas,
                select,
                selectArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst() == true) {
            String u = cursor.getString(1);

            String [] columnas1 = {
                    BaseColumns._ID,
                    AdminContract.AdminEntry.USUARIO,
                    AdminContract.AdminEntry.CONTRA
            };
            String select1 = AdminContract.AdminEntry.USUARIO + "= ? AND " + AdminContract.AdminEntry.CONTRA + "= ?";
            String[] selectArgs1 = { u, contra };
            Cursor cursor1 = db.query(AdminContract.AdminEntry.NOMBRE_TABLA,
                    columnas1,
                    select1,
                    selectArgs1,
                    null,
                    null,
                    null);

            if (cursor1.moveToFirst() == true) {
                if (cursor1.getString(1).equals("ies") && cursor1.getString(2).equals("admin")){
                    Intent interesados = new Intent(Login.this, IngresarDatos.class);
                    interesados.putExtra("visible", "Si");
                    startActivityForResult(interesados, 0);
                    finish();
                } else {
                    Intent interesados = new Intent(Login.this, IngresarDatos.class);
                    interesados.putExtra("visible", "No");
                    startActivityForResult(interesados, 0);
                    finish();
                }
            }else{
                etContra.requestFocus();
                etContra.setError("Contraseña incorrecta");
            }
        }else{
            etUsuario.requestFocus();
            etUsuario.setError("Usuario incorrecto");
        }
    }
}
