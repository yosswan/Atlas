
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yosswan.vecino
 */
public class Factor implements Serializable {
    private String id, factor, signo, cadena;
    private float porcentaje;
    private int numero;

    public Factor(String id, String factor, String signo, String cadena, float porcentaje, int numero) {
        this.id = id;
        this.factor = factor;
        this.signo = signo;
        this.cadena = cadena;
        this.porcentaje = porcentaje;
        this.numero = numero;
    }

    public String getId() {
        return id;
    }

    public String getFactor() {
        return factor;
    }

    public String getSigno() {
        return signo;
    }

    public String getCadena() {
        return cadena;
    }

    public float getPorcentaje() {
        return porcentaje;
    }

    public int getNumero() {
        return numero;
    }
    
    
}
