
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yosswan.vecino
 */
public class Escritor {
    private ArrayList<Factor> arrayList1;

    public Escritor() {
        arrayList1 = new ArrayList<Factor>();
    }
    
    public void AñadirFactor(Factor f)
    {
        arrayList1.add(f);
    }
    
    public void GuardarFactores()
    {
        try
        {
            ObjectOutputStream escribirFichero = new ObjectOutputStream( 
            new FileOutputStream("objetos.dat") );
            escribirFichero.writeObject(arrayList1);
            escribirFichero.close();
        }
        catch(Exception e)
        {
                    
        }
    }
}
