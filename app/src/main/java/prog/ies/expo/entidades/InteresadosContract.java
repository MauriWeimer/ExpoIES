package prog.ies.expo.entidades;

import android.provider.BaseColumns;

public final class InteresadosContract {
    private InteresadosContract(){}

    public static abstract class InteresadosEntry implements BaseColumns{
        public static final String NOMBRE_TABLA = "interesados";

        public static final String NOMBRE = "nombre";
        public static final String APELLIDO = "apellido";
        public static final String DNI = "dni";
        public static final String PROVINCIA = "provincia";
        public static final String CIUDAD = "ciudad";
        public static final String CEL = "cel";
        public static final String CORREOELECTRONICO = "correoelectronico";
        public static final String CARRERA = "carrera";
        public static final String COMOSEENTERO = "comoseentero";
        public static final String NOTIFICARME = "notificarme";
        public static final String OBSERVACIONES = "observaciones";
    }
}
