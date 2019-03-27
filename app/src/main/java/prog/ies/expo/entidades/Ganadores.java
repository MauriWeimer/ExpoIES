package prog.ies.expo.entidades;

import java.util.Date;

public class Ganadores {
    private String id_interesado;

    public Ganadores(String id_interesado){
        this.id_interesado = id_interesado;
    }

    public String getGanador() { return id_interesado; }

    public void setGanador(String id_interesado) { this.id_interesado = id_interesado; }
}
