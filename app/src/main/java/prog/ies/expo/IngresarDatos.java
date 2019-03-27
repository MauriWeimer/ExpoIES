package prog.ies.expo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import prog.ies.expo.entidades.Interesados;
import prog.ies.expo.entidades.ConexionSQLiteHelper;
import prog.ies.expo.entidades.InteresadosContract;

public class IngresarDatos extends AppCompatActivity {

    private EditText etNombre, etApellido, etDNI, etCiudad, etCelular, etEmail, etObservacion;
    private Spinner spProvincia, spCarrera, spComoSeEntero, spEmail;
    private CheckBox chbNotificar;
    private Button guardar, limpiar, listado, cerrarSesion, actualizardb;
    private int id;
    private String visible = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos);

        id = 0;

        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellido = (EditText) findViewById(R.id.etApellido);
        etDNI = (EditText) findViewById(R.id.etDNI);
        etCiudad = (EditText) findViewById(R.id.etCiudad);
        etCelular = (EditText) findViewById(R.id.etCelular);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etObservacion = (EditText) findViewById(R.id.etObservacion);
        spProvincia = (Spinner) findViewById(R.id.spProvincia);
        spCarrera = (Spinner) findViewById(R.id.spCarrera);;
        spComoSeEntero = (Spinner) findViewById(R.id.spComoSeEntero);
        chbNotificar = (CheckBox) findViewById(R.id.chbNotificar);
        guardar = (Button) findViewById(R.id.btGuardar);
        limpiar = (Button) findViewById(R.id.btLimpiar);
        listado = (Button) findViewById(R.id.btListado);
        cerrarSesion = (Button) findViewById(R.id.btCerrarSesion);
        actualizardb = (Button) findViewById(R.id.btActualizarDB);

        cerrarSesion.setPaintFlags(cerrarSesion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        actualizardb.setPaintFlags(actualizardb.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String[] email = {"-", "@gmail.com", "@hotmail.com", "@yahoo.com.ar"};
        spEmail = (Spinner) findViewById(R.id.spEmail);
        spEmail.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, email));

        eventos();
        recibirDatos();
    }

    private void recibirDatos() {
        String eId = getIntent().getStringExtra("eId");

        if (eId == null){
            id = 0;
        } else {
            id = Integer.parseInt(getIntent().getStringExtra("eId"));
            etNombre.setText(getIntent().getStringExtra("eNombre"));
            etApellido.setText(getIntent().getStringExtra("eApellido"));
            etDNI.setText(getIntent().getStringExtra("eDNI"));
            spProvincia.setSelection(getIndex(spProvincia, getIntent().getStringExtra("eProvincia")));
            etCiudad.setText(getIntent().getStringExtra("eCiudad"));
            etCelular.setText(getIntent().getStringExtra("eCel"));
            if (getIntent().getStringExtra("eEmail").length() > 1){
                String[] partes = getIntent().getStringExtra("eEmail").split("@");
                etEmail.setText(partes[0]);
                spEmail.setSelection(getIndex(spEmail, "@"+partes[1]));
            } else {
                etEmail.setText("");
                spEmail.setSelection(0);
            }
            spCarrera.setSelection(getIndex(spCarrera, getIntent().getStringExtra("eCarrera")));
            spComoSeEntero.setSelection(getIndex(spComoSeEntero, getIntent().getStringExtra("eEntero")));
            if (getIntent().getStringExtra("eNotificar").equals("Si")){
                chbNotificar.setChecked(true);
            }else{
                chbNotificar.setChecked(false);
            }
            etObservacion.setText(getIntent().getStringExtra("eObs"));
            etNombre.requestFocus();
        }


        visible = getIntent().getStringExtra("visible");

        if (visible != null){
            if (visible.equals("Si")){
                actualizardb.setVisibility(View.VISIBLE);
            } else {
                actualizardb.setVisibility(View.GONE);
            }
        }
    }

    private int getIndex(Spinner spinner, String valor){
        for (int i=0; i<spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(valor)){
                return i;
            }
        }
        return 0;
    }

    private void eventos(){
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });
        listado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listado();
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrar();
            }
        });
        actualizardb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });

        etCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                etEmail.setError(null);
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                    etCelular.setError(null);
            }
        });
    }

    private void actualizar() {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase dbi = sql.getWritableDatabase();

        File file = new File("/sdcard/IES/backupIES.db");
        if (file.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/IES/backupIES.db",
                    null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            dbi.execSQL("DELETE FROM " + InteresadosContract.InteresadosEntry.NOMBRE_TABLA);
            dbi.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + InteresadosContract.InteresadosEntry.NOMBRE_TABLA + "'");

            while (cursor.moveToNext()){
                Interesados persona = new Interesados(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9),
                        cursor.getString(10), cursor.getString(11));
                sql.agregarInteresado(persona);
            }

            Toast.makeText(getApplicationContext(), "Base de Datos actualizada!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No hay nada que actualzar", Toast.LENGTH_LONG).show();
        }
    }

    private void cerrar(){
        Intent login = new Intent(IngresarDatos.this, Login.class);
        startActivity(login);
        finish();
    }

    private void listado(){
        Intent lista = new Intent(IngresarDatos.this, ListaInteresados.class);
        lista.putExtra("visible", visible);
        startActivity(lista);
    }

    private void limpiar(){
        id = 0;
        etNombre.setText("");
        etApellido.setText("");
        etDNI.setText("");
        etCiudad.setText("");
        etCelular.setText("");
        etEmail.setText("");
        spEmail.setSelection(0);
        etObservacion.setText("");
        spProvincia.setSelection(0);
        spCarrera.setSelection(0);
        spComoSeEntero.setSelection(0);
        chbNotificar.setChecked(false);
    }
    private void guardar(){
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);

        String nombre, apellido, dni, correo, observacion, provincia, ciudad, cel, carrera, comoseentero, notificarme;

        if (verificarCampos()) {

            nombre = etNombre.getText().toString();
            apellido = etApellido.getText().toString();
            dni = etDNI.getText().toString();
            ciudad = etCiudad.getText().toString();
            cel = etCelular.getText().toString();
            if (etEmail.getText().length() == 0){
                correo = "";
            }else{
                correo = etEmail.getText().toString() + spEmail.getSelectedItem().toString();
            }
            provincia = spProvincia.getSelectedItem().toString();
            carrera = spCarrera.getSelectedItem().toString();
            comoseentero = spComoSeEntero.getSelectedItem().toString();
            observacion = etObservacion.getText().toString();

            if (chbNotificar.isChecked()) {
                notificarme = "Si";
            } else {
                notificarme = "No";
            }

            if (id > 0){
                modificar(nombre, apellido, dni, provincia, ciudad, cel, correo, carrera, comoseentero, notificarme, observacion);
                id = 0;
            } else {
                Interesados persona = new Interesados(nombre, apellido, dni, provincia, ciudad, cel,
                        correo, carrera, comoseentero, notificarme, observacion);

                sql.agregarInteresado(persona);

                Toast.makeText(getApplicationContext(), "Agregado correctamente!", Toast.LENGTH_LONG).show();
            }

            backupBD();
            limpiar();
            etNombre.requestFocus();
        }
    }

    private void modificar(String nombre, String apellido, String dni, String provincia, String ciudad, String cel, String email,
                           String carrera, String entero, String notificar, String obs){
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(InteresadosContract.InteresadosEntry.NOMBRE, nombre);
        valores.put(InteresadosContract.InteresadosEntry.APELLIDO, apellido);
        valores.put(InteresadosContract.InteresadosEntry.DNI, dni);
        valores.put(InteresadosContract.InteresadosEntry.PROVINCIA, provincia);
        valores.put(InteresadosContract.InteresadosEntry.CIUDAD, ciudad);
        valores.put(InteresadosContract.InteresadosEntry.CEL, cel);
        valores.put(InteresadosContract.InteresadosEntry.CORREOELECTRONICO, email);
        valores.put(InteresadosContract.InteresadosEntry.CARRERA, carrera);
        valores.put(InteresadosContract.InteresadosEntry.COMOSEENTERO, entero);
        valores.put(InteresadosContract.InteresadosEntry.NOTIFICARME, notificar);
        valores.put(InteresadosContract.InteresadosEntry.OBSERVACIONES, obs);

        db.update(InteresadosContract.InteresadosEntry.NOMBRE_TABLA, valores,
                InteresadosContract.InteresadosEntry._ID + "=" + id, null);

        Toast.makeText(getApplicationContext(), "Modificado correctamente!", Toast.LENGTH_LONG).show();
    }

    private boolean verificarCampos(){
        boolean verificador = true;

        if (spCarrera.getSelectedItemPosition() == 0){
            spCarrera.requestFocusFromTouch();
            ((TextView)spCarrera.getSelectedView()).setError("Elija una opción");
            verificador = false;
        }

        if (etEmail.getText().toString().length() > 0){
            if (spEmail.getSelectedItemPosition() == 0){
                spEmail.requestFocusFromTouch();
                ((TextView)spEmail.getSelectedView()).setError("Elija una opción");
                verificador = false;
            }
        }

        if (etEmail.getText().toString().length() == 0 || etCelular.getText().toString().length() < 8){
            if (etEmail.getText().toString().length() > 0){
                if (spEmail.getSelectedItemPosition() == 0){
                    spEmail.requestFocusFromTouch();
                    ((TextView)spEmail.getSelectedView()).setError("Elija una opción");
                    verificador = false;
                }
            } else {
               if (etCelular.getText().toString().length() < 8){
                   etCelular.requestFocus();
                   etCelular.setError("El campo debe contener al menos 8 dígitos");
                   verificador = false;
               }
            }
        }

        if (etEmail.getText().toString().length() == 0 && etCelular.getText().toString().length() == 0){
            etEmail.setError("Agregue un email");
            etCelular.setError("Agregue un n°");
            verificador = false;
        }

        if (etDNI.getText().toString().length() > 0){
            if (etDNI.getText().toString().length() < 8){
                etDNI.requestFocus();
                etDNI.setError("El campo debe contener 8 dígitos");
                verificador = false;
            }
        }

        if (etApellido.getText().toString().length() == 0){
            etApellido.requestFocus();
            etApellido.setError("Revisar campo");
            verificador = false;
        }

        if (etNombre.getText().toString().length() == 0){
            etNombre.requestFocus();
            etNombre.setError("Revisar campo");
            verificador = false;
        }

        return verificador;
    }

    private void backupBD(){
        try {
            File sd = new File("/sdcard/IES/");
            sd.mkdirs();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/" + ConexionSQLiteHelper.DB_NAME;
                String backupDBPath = "backupIES.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }
}
