package prog.ies.expo.entidades;

import android.provider.BaseColumns;

public final class GanadoresContract {
    private GanadoresContract(){}

    public static abstract class GanadoresEntry implements BaseColumns {
        public static final String NOMBRE_TABLA = "ganadores";

        public static final String ID_INTERESADO = "id_interesado";
    }
}
