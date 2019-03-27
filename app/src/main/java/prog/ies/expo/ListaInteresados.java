package prog.ies.expo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Random;

import prog.ies.expo.entidades.ConexionSQLiteHelper;
import prog.ies.expo.entidades.Ganadores;
import prog.ies.expo.entidades.GanadoresContract;
import prog.ies.expo.entidades.Interesados;
import prog.ies.expo.entidades.InteresadosContract;

public class ListaInteresados extends AppCompatActivity {
    private ListView lista;
    private Toolbar toolbar;
    private ArrayAdapter<String> arrayAdapterInteresados;
    private ArrayAdapter<String> arrayAdapterGanadores;
    private Button sortear, gano, ganadores;
    public String id;
    private String visible = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_interesados);

        lista = (ListView) findViewById(R.id.lista);
        sortear = (Button) findViewById(R.id.btSortear);
        ganadores = (Button) findViewById(R.id.btGanadores);
        gano = (Button) findViewById(R.id.btGanador);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        configToolbar();

        arrayAdapterInteresados = new ArrayAdapter<String>(ListaInteresados.this,
                android.R.layout.simple_expandable_list_item_1);
        arrayAdapterGanadores = new ArrayAdapter<String>(ListaInteresados.this,
                android.R.layout.simple_expandable_list_item_1);

        visible = getIntent().getStringExtra("visible");

        cargarLista();
        eventos();
    }

    private void cargarLista() {
        lista.setAdapter(null);

        agregarPersonasLista();

        lista.setAdapter(arrayAdapterInteresados);
        registerForContextMenu(lista);
    }

    private void agregarPersonasLista() {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        arrayAdapterInteresados.clear();

        String [] columnas = {
                BaseColumns._ID,
                InteresadosContract.InteresadosEntry.NOMBRE,
                InteresadosContract.InteresadosEntry.APELLIDO
        };
        Cursor cursor = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                columnas,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()){
            arrayAdapterInteresados.add(cursor.getString(0) + " | " + cursor.getString(1) + " " + cursor.getString(2));
        }
    }

    private void eventos() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String datosarray = arrayAdapterInteresados.getItem(i);
                String[] partes = datosarray.split(" | ");
                String id = partes[0];
                cargarDatosPersona(id);
            }
        });
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String datosarray = arrayAdapterInteresados.getItem(i);
                String[] partes = datosarray.split(" | ");
                id = partes[0];
                return false;
            }
        });
        sortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorteo();
            }
        });
        ganadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarGanadores();
            }
        });
        gano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gano.getText().toString().length() > 0){
                    String datosarray = gano.getText().toString();
                    String[] partes = datosarray.split(" | ");
                    String id = partes[0];
                    cargarDatosPersona(id);
                }
            }
        });
    }

    private void cargarGanadores() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ListaInteresados.this);
        builderSingle.setTitle("Ganadores:");

        final ArrayAdapter<String> arrayAdapterInteresados = new ArrayAdapter<String>(ListaInteresados.this,
                android.R.layout.simple_expandable_list_item_1);
        agregarGanadoresLista();

        builderSingle.setNegativeButton("volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        if (arrayAdapterGanadores.getCount() > 0){
            builderSingle.setAdapter(arrayAdapterGanadores, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    String datosarray = arrayAdapterGanadores.getItem(i);
                    String[] partes = datosarray.split(" | ");
                    id = partes[0];
                    cargarDatosPersona(id);
                }
            });
        }else{
            builderSingle.setMessage("No hay ganadores");
        }
        builderSingle.show();
    }

    private void agregarGanadoresLista() {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        arrayAdapterGanadores.clear();

        String [] columnas = {
                BaseColumns._ID,
                GanadoresContract.GanadoresEntry.ID_INTERESADO
        };
        Cursor cursor = db.query(GanadoresContract.GanadoresEntry.NOMBRE_TABLA,
                columnas,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()){
            String [] columnas2 = {
                    BaseColumns._ID,
                    InteresadosContract.InteresadosEntry.NOMBRE,
                    InteresadosContract.InteresadosEntry.APELLIDO
            };

            String select = InteresadosContract.InteresadosEntry._ID + "= ?";
            String selectArgs[] = { cursor.getString(1) };

            Cursor cursor2 = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                    columnas2,
                    select,
                    selectArgs,
                    null,
                    null,
                    null);

            if (cursor2.moveToFirst()){
                arrayAdapterGanadores.add(cursor2.getString(0) + " | " + cursor2.getString(1) + " " + cursor2.getString(2));
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.borrar, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.borrar:
                alertaBorrar();
                return true;
            case R.id.editar:
                editarInteresado();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void editarInteresado() {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String [] columnas = {
                BaseColumns._ID,
                InteresadosContract.InteresadosEntry.NOMBRE,
                InteresadosContract.InteresadosEntry.APELLIDO,
                InteresadosContract.InteresadosEntry.DNI,
                InteresadosContract.InteresadosEntry.PROVINCIA,
                InteresadosContract.InteresadosEntry.CIUDAD,
                InteresadosContract.InteresadosEntry.CEL,
                InteresadosContract.InteresadosEntry.CORREOELECTRONICO,
                InteresadosContract.InteresadosEntry.CARRERA,
                InteresadosContract.InteresadosEntry.COMOSEENTERO,
                InteresadosContract.InteresadosEntry.NOTIFICARME,
                InteresadosContract.InteresadosEntry.OBSERVACIONES
        };

        String select = InteresadosContract.InteresadosEntry._ID + "= ?";
        String[] selectArgs = { id };

        Cursor cursor = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                columnas,
                select,
                selectArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            Intent i = new Intent(ListaInteresados.this, IngresarDatos.class);
            i.putExtra("eId", cursor.getString(0));
            i.putExtra("eNombre", cursor.getString(1));
            i.putExtra("eApellido", cursor.getString(2));
            i.putExtra("eDNI", cursor.getString(3));
            i.putExtra("eProvincia", cursor.getString(4));
            i.putExtra("eCiudad", cursor.getString(5));
            i.putExtra("eCel", cursor.getString(6));
            i.putExtra("eEmail", cursor.getString(7));
            i.putExtra("eCarrera", cursor.getString(8));
            i.putExtra("eEntero", cursor.getString(9));
            i.putExtra("eNotificar", cursor.getString(10));
            i.putExtra("eObs", cursor.getString(11));
            i.putExtra("visible", visible);
            startActivity(i);
        }
    }

    private void alertaBorrar(){
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setIcon(android.R.drawable.ic_dialog_alert);
        alerta.setTitle("  Seguro que desea eliminar?");
        alerta.setMessage("Se eliminará la persona seleccionada");
        alerta.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (borrarInteresado(id)){
                    Toast.makeText(getApplicationContext(), "Eliminado correctamente!", Toast.LENGTH_LONG).show();
                };
                borrarGanador(id);
                backupBD();
                cargarLista();
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = alerta.create();
        dialog.show();
        ImageView icono = dialog.findViewById(android.R.id.icon);
        icono.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void borrarGanador(String id) {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String select = GanadoresContract.GanadoresEntry.ID_INTERESADO + "= ?";
        String[] selectArgs = { id };

        db.delete(GanadoresContract.GanadoresEntry.NOMBRE_TABLA,
                select,
                selectArgs);
    }

    private boolean borrarInteresado(String id){
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String select = InteresadosContract.InteresadosEntry._ID + "= ?";
        String[] selectArgs = { id };

        return db.delete(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                select,
                selectArgs) > 0;
    }

    private void sorteo() {
        if (verificarIgualdadGanadorInteresado()){
            gano.setText("No hay personas interesadas");
            return;
        }

        String ganador = arrayAdapterInteresados.getItem(new Random().nextInt(arrayAdapterInteresados.getCount())).toString();
        String[] partes = ganador.split(" | ");
        String idGanador = partes[0];

        while (verificarGanador(idGanador)){
            ganador = arrayAdapterInteresados.getItem(new Random().nextInt(arrayAdapterInteresados.getCount())).toString();
            partes = ganador.split(" | ");
            idGanador = partes[0];
        }

        agregarGanador(idGanador);
        backupBD();
        gano.setText(ganador);
    }

    private void agregarGanador(String idGanador) {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);

        Ganadores persona = new Ganadores(idGanador);

        sql.agregarGanador(persona);
    }

    private boolean verificarIgualdadGanadorInteresado() {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String [] columnas = {
                BaseColumns._ID
        };

        Cursor cursor = db.query(GanadoresContract.GanadoresEntry.NOMBRE_TABLA,
                columnas,
                null,
                null,
                null,
                null,
                null);
        int ganador = cursor.getCount();

        String [] columnas2 = {
                BaseColumns._ID
        };

        Cursor cursor2 = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                columnas,
                null,
                null,
                null,
                null,
                null);
        int interesado = cursor2.getCount();

        if (ganador == interesado){
            return true;
        }else{
            return false;
        }
    }

    private boolean verificarGanador(String idGanador) {
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String [] columnas = {
                BaseColumns._ID,
                GanadoresContract.GanadoresEntry.ID_INTERESADO
        };
        String select = GanadoresContract.GanadoresEntry.ID_INTERESADO + "= ?";
        String[] selectArgs = { idGanador };

        Cursor cursor = db.query(GanadoresContract.GanadoresEntry.NOMBRE_TABLA,
                columnas,
                select,
                selectArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()){
            return true;
        }else{
            return  false;
        }
    }

    private void cargarDatosPersona(String id){
        ConexionSQLiteHelper sql = new ConexionSQLiteHelper(this);
        SQLiteDatabase db = sql.getWritableDatabase();

        String [] columnas = {
                BaseColumns._ID,
                InteresadosContract.InteresadosEntry.NOMBRE,
                InteresadosContract.InteresadosEntry.APELLIDO,
                InteresadosContract.InteresadosEntry.DNI,
                InteresadosContract.InteresadosEntry.PROVINCIA,
                InteresadosContract.InteresadosEntry.CIUDAD,
                InteresadosContract.InteresadosEntry.CEL,
                InteresadosContract.InteresadosEntry.CORREOELECTRONICO,
                InteresadosContract.InteresadosEntry.CARRERA,
                InteresadosContract.InteresadosEntry.COMOSEENTERO,
                InteresadosContract.InteresadosEntry.NOTIFICARME,
                InteresadosContract.InteresadosEntry.OBSERVACIONES
        };

        String select = InteresadosContract.InteresadosEntry._ID + "= ?";
        String[] selectArgs = { id };

        Cursor cursor = db.query(InteresadosContract.InteresadosEntry.NOMBRE_TABLA,
                columnas,
                select,
                selectArgs,
                null,
                null,
                null);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ListaInteresados.this);

        builderSingle.setNegativeButton("volver", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        if (cursor.moveToFirst()){
            builderSingle.setTitle("Datos:");

            builderSingle.setMessage(
                    "ID: " + cursor.getString(0) + "\n" +
                            "NOMBRE: " + cursor.getString(1) + "\n" +
                            "APELLIDO: " + cursor.getString(2) + "\n" +
                            "DNI: " + cursor.getString(3) + "\n" +
                            "PROVINCIA: " + cursor.getString(4) + "\n" +
                            "CIUDAD: " + cursor.getString(5) + "\n" +
                            "CELULAR: " + cursor.getString(6) + "\n" +
                            "EMAIL: " + cursor.getString(7) + "\n" +
                            "CARRERA: " + cursor.getString(8) + "\n" +
                            "CÓMO SE ENTERÓ: " + cursor.getString(9) + "\n" +
                            "NOTIFICAR: " + cursor.getString(10) + "\n" +
                            "OBSERVACIÓN: " + cursor.getString(11) + "\n"
            );
            builderSingle.show();
        }

    }

    private void configToolbar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
