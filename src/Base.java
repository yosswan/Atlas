import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import static javax.swing.JOptionPane.showMessageDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
public class Base {
    static final JasparConnection j_connection = new JasparConnection();
    
    public static void TfBind(ArrayList<Jaspar> PPM, String secuencia, float MinProb) throws FileNotFoundException, IOException{
            char[] ArregloSec = secuencia.toCharArray();
            Writer output = new BufferedWriter(new FileWriter("Salida.txt"));
            output.append("Matriz\tEtiqueta\tProbabilidad\tPosicion\tSeñal\tSecuencia");
            for (int j = 0; j < PPM.size(); j++) {
            
                for (int pos = 0; pos < (ArregloSec.length - PPM.get(j).A.length + 1); pos++) {
                    String SubCadena = "";
                    float Prob = 1;
                    for (int i = pos; i < (PPM.get(j).A.length)+pos; i++) {
                    SubCadena = SubCadena + ArregloSec[i];
                    }
                    char[] ArregloCadena = SubCadena.toCharArray();
                    for (int tracer = 0; tracer < ArregloCadena.length; tracer++) {
                       // System.out.print(ArregloCadena[tracer] + ">");
                            switch(ArregloCadena[tracer]) {
                            case 'A':
                                //System.out.print("+A");
                                Prob = Prob * PPM.get(j).A[tracer];
                                break;
                            case 'C':
                                //System.out.print("+C");
                                Prob = Prob * PPM.get(j).C[tracer];
                                break;
                            case 'G':
                                //System.out.print("+G");
                                Prob = Prob * PPM.get(j).G[tracer];
                                break;
                            case 'T':
                                //System.out.print("+T");   
                                Prob = Prob * PPM.get(j).T[tracer];
                                break;
                            default:
                                System.out.println(String.format("It's a mystery"));
                                break;
                            }
                    }
                    if(Prob >= MinProb ){
                        output.append("\n");
                        output.append(PPM.get(j).Matriz + "\t" + PPM.get(j).Etiqueta + "\t"+ String.format("%f" , Prob) +"\t"  + (pos+1) + "\t" + PPM.get(j).Senal + "\t\t"+  SubCadena );
                        System.out.println(PPM.get(j).Matriz + "\t" + PPM.get(j).Etiqueta + "\t"+ String.format("%f" , Prob) +"\t"  + (pos+1) + "\t\t" + PPM.get(j).Senal + "\t"+  SubCadena );   
                    }
                    //System.out.println("subcadena " + pos);
                }               
                //11986
                //System.out.println("factor " + j);
            }
            output.close ();
            showMessageDialog(null, "Atlas ha terminado!");
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Scanner lector = (new Scanner( new java.io.FileInputStream("./data/Atlas_configuracion.txt") ));
            lector.nextLine();
            String secuencia = "./data/"+lector.nextLine();
            lector.nextLine();
            String matriz = "./data/"+lector.nextLine();
            lector.nextLine();
            float probabilidad = Float.parseFloat(lector.nextLine());
            showMessageDialog(null, "Atlas usará "+probabilidad+" como probabilidad mínima");

        //String matriz = "JASPAR2018_CORE_plants_non-redundant_pfms_jaspar.txt";
        //String matriz = "JASPAR2018_CORE_non-redundant_pfms_jaspar.txt";
        
        ArrayList<Jaspar> tfactors;
        tfactors = j_connection.getTFactors(matriz);
            for (int i = 0; i < tfactors.size(); i++) {
                tfactors.get(i).ToPPM();
            }
           
            TfBind(tfactors, secuencia, probabilidad);
    }
}
