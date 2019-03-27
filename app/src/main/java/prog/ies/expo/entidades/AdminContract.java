package prog.ies.expo.entidades;

import android.provider.BaseColumns;

public final class AdminContract {
    private AdminContract(){}

    public static abstract class AdminEntry implements BaseColumns{
        public static final String NOMBRE_TABLA = "admin";

        public static final String USUARIO = "usuario";
        public static final String CONTRA = "contra";
    }
}
