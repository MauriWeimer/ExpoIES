package prog.ies.expo.entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public static final String DB_NAME = "IES.db";

    private static final String CREAR_TABLA_ADMIN = "CREATE TABLE " + AdminContract.AdminEntry.NOMBRE_TABLA + "(" +
            AdminContract.AdminEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AdminContract.AdminEntry.USUARIO + " TEXT, " +
            AdminContract.AdminEntry.CONTRA + " TEXT" + ")";

    private static final String CREAR_TABLA_GANADOR = "CREATE TABLE " + GanadoresContract.GanadoresEntry.NOMBRE_TABLA + "(" +
            GanadoresContract.GanadoresEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GanadoresContract.GanadoresEntry.ID_INTERESADO + ")";

    private static final String CREAR_TABLA_INTERESADO = "CREATE TABLE " + InteresadosContract.InteresadosEntry.NOMBRE_TABLA + "(" +
            InteresadosContract.InteresadosEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            InteresadosContract.InteresadosEntry.NOMBRE + " TEXT, " +
            InteresadosContract.InteresadosEntry.APELLIDO + " TEXT, " +
            InteresadosContract.InteresadosEntry.DNI + " TEXT, " +
            InteresadosContract.InteresadosEntry.PROVINCIA + " TEXT, " +
            InteresadosContract.InteresadosEntry.CIUDAD + " TEXT, " +
            InteresadosContract.InteresadosEntry.CEL + " TEXT, " +
            InteresadosContract.InteresadosEntry.CORREOELECTRONICO + " TEXT, " +
            InteresadosContract.InteresadosEntry.CARRERA + " TEXT, " +
            InteresadosContract.InteresadosEntry.COMOSEENTERO + " TEXT, " +
            InteresadosContract.InteresadosEntry.NOTIFICARME + " TEXT, " +
            InteresadosContract.InteresadosEntry.OBSERVACIONES + " TEXT" +")";

    private String DROP_TABLA_ADMIN = "DROP TABLE IF EXISTS " + AdminContract.AdminEntry.NOMBRE_TABLA;
    private String DROP_TABLA_GANADOR = "DROP TABLE IF EXISTS " + GanadoresContract.GanadoresEntry.NOMBRE_TABLA;
    private String DROP_TABLA_INTERESADO = "DROP TABLE IF EXISTS " + InteresadosContract.InteresadosEntry.NOMBRE_TABLA;

    public ConexionSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_ADMIN);

        ContentValues usuario = new ContentValues();
        usuario.put(AdminContract.AdminEntry.USUARIO, "ies");
        usuario.put(AdminContract.AdminEntry.CONTRA, "expo");

        db.insert(AdminContract.AdminEntry.NOMBRE_TABLA, null, usuario);

        ContentValues admin = new ContentValues();
        admin.put(AdminContract.AdminEntry.USUARIO, "ies");
        admin.put(AdminContract.AdminEntry.CONTRA, "admin");

        db.insert(AdminContract.AdminEntry.NOMBRE_TABLA, null, admin);

        db.execSQL(CREAR_TABLA_GANADOR);

        db.execSQL(CREAR_TABLA_INTERESADO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL(DROP_TABLA_ADMIN);
        db.execSQL(DROP_TABLA_GANADOR);
        db.execSQL(DROP_TABLA_INTERESADO);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        onUpgrade(db, versionAntigua, versionNueva);
    }

    public void agregarInteresado(Interesados persona){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues interesado = new ContentValues();
        interesado.put(InteresadosContract.InteresadosEntry.NOMBRE, persona.getNombre());
        interesado.put(InteresadosContract.InteresadosEntry.APELLIDO, persona.getApellido());
        interesado.put(InteresadosContract.InteresadosEntry.DNI, persona.getDNI());
        interesado.put(InteresadosContract.InteresadosEntry.PROVINCIA, persona.getProvincia());
        interesado.put(InteresadosContract.InteresadosEntry.CIUDAD, persona.getCiudad());
        interesado.put(InteresadosContract.InteresadosEntry.CEL, persona.getCel());
        interesado.put(InteresadosContract.InteresadosEntry.CORREOELECTRONICO, persona.getCorreoElectronico());
        interesado.put(InteresadosContract.InteresadosEntry.CARRERA, persona.getCarrera());
        interesado.put(InteresadosContract.InteresadosEntry.COMOSEENTERO, persona.getComoSeEntero());
        interesado.put(InteresadosContract.InteresadosEntry.NOTIFICARME, persona.getNotificarme());
        interesado.put(InteresadosContract.InteresadosEntry.OBSERVACIONES, persona.getObservaciones());

        db.insert(InteresadosContract.InteresadosEntry.NOMBRE_TABLA, null, interesado);
    }

    public void agregarGanador(Ganadores persona){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ganador = new ContentValues();
        ganador.put(GanadoresContract.GanadoresEntry.ID_INTERESADO, persona.getGanador());

        db.insert(GanadoresContract.GanadoresEntry.NOMBRE_TABLA, null, ganador);
    }
}
