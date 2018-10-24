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
        
        //Scanner lector = new Scanner(new File("Atlas_configuracion.txt"));
        Scanner lector = (new Scanner( new java.io.FileInputStream("./data/Atlas_configuracion.txt") ));
            lector.nextLine();
            String secuencia = "./data/"+lector.nextLine();
            lector.nextLine();
            String matriz = "./data/"+lector.nextLine();
            lector.nextLine();
            float probabilidad = Float.parseFloat(lector.nextLine());
            showMessageDialog(null, "Atlas usará "+probabilidad+" como probabilidad mínima");
            //System.out.println(secuencia);
            

        //String matriz = "JASPAR2018_CORE_plants_non-redundant_pfms_jaspar.txt";
        //String matriz = "JASPAR2018_CORE_non-redundant_pfms_jaspar.txt";
        
        ArrayList<Jaspar> tfactors = new ArrayList<Jaspar>();

            //lector = new Scanner(new File(secuencia));
            lector = (new Scanner( new java.io.FileInputStream(secuencia) ));
            while(lector.hasNext()){
            secuencia = lector.nextLine();
            //System.out.println(secuencia);
            }
            
            URL url;
            boolean internet = true;
            try {
                // Creando un objeto URL
                url = new URL("http://jaspar.genereg.net/api/v1/releases/?format=json");

                // Realizando la petición GET
                URLConnection con = url.openConnection();

                // Leyendo el resultado
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String linea;
                linea = in.readLine();
                System.out.println(linea);
                
                JsonParser parser = new JsonParser();

                // Obtain Array
                JsonObject gsonObj = parser.parse(linea).getAsJsonObject();

                // List of primitive elements
                JsonArray results = gsonObj.get("results").getAsJsonArray();
                int year = results.get(0).getAsJsonObject().get("year").getAsInt();
                System.out.println(year);
                lector = (new Scanner( new java.io.FileInputStream("./data/year.txt")));
                int yearActual = lector.nextInt();
                
                if(year == yearActual)
                    internet = false;
                else
                {
                    lector.close();
                    Writer output = new BufferedWriter(new FileWriter("./data/year.txt"));
                    output.append(Integer.toString(year));
                    output.close();
                }
            }catch (IOException e) {
                System.out.println(e.getMessage());
                internet = false;
            }
            
            String cabecera = null, cad1 = null, cad2 = null, cad3 = null, cad4;
            int c=0;
            if(internet)
            {                            
                try {
                    url = new URL("http://jaspar.genereg.net/api/v1/matrix?format=json");
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String linea = in.readLine();
                    System.out.println(linea);

                    JsonParser parser = new JsonParser();
                    JsonObject gsonObj = parser.parse(linea).getAsJsonObject();
                    int count = gsonObj.get("count").getAsInt();
                    System.out.println(count);
                    Writer output = new BufferedWriter(new FileWriter("./data/jaspar.txt"));
                    int n = count/1000;
                    int d = count%1000 > 0?1:0;
                    
                    for(int i=0; i<=n+d; i++)
                    {
                        url = new URL("http://jaspar.genereg.net/api/v1/matrix?format=jaspar&page_size=1000&page=" + (i+1));

                        // Realizando la petición GET
                        con = url.openConnection();

                        // Leyendo el resultado
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        c=0;
                        
                        while((linea = in.readLine()) != null){
                            System.out.println(linea);
                            output.append(linea + "\n");
                            switch(c) {
                                case 0:
                                    cabecera = linea;
                                    c=c+1;
                                    break;
                                case 1:
                                    //System.out.println("A");
                                    cad1 = linea;
                                    c=c+1;
                                    break;
                                case 2:
                                    //System.out.println("B");
                                    cad2 = linea;
                                    c=c+1;
                                    break;
                                case 3:
                                    //System.out.println("C");
                                    cad3 = linea;
                                    c=c+1;
                                    break;
                                case 4:
                                    //System.out.println("D");
                                    cad4 = linea;

                                    tfactors.add(new Jaspar(cabecera,cad1,cad2,cad3,cad4));
                                    //System.out.println(tfactors.get(a).tfMatriz);
                                    c=0;
                                    break;
                                default:
                                    System.out.println(String.format("It's a mystery"));
                                    break;
                            }
                        }
                    }
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    internet = false;
                }
            }

            if(!internet)
            {
                //lector = new Scanner(new File(matriz));
                lector = (new Scanner( new java.io.FileInputStream(matriz) ));
                int a = 0 ;
                String last;
                while(lector.hasNext()){
                    switch(c) {
                        case 0:
                            cabecera = lector.nextLine();
                            c=c+1;
                            break;
                        case 1:
                            //System.out.println("A");
                            cad1 = lector.nextLine();
                            c=c+1;
                            break;
                        case 2:
                            //System.out.println("B");
                            cad2 = lector.nextLine();
                            c=c+1;
                            break;
                        case 3:
                            //System.out.println("C");
                            cad3 = lector.nextLine();
                            c=c+1;
                            break;
                        case 4:
                            //System.out.println("D");
                            cad4 = lector.nextLine();

                            tfactors.add(new Jaspar(cabecera,cad1,cad2,cad3,cad4));
                            //System.out.println(tfactors.get(a).tfMatriz);
                            c=0;
                            a=a+1;
                            break;
                        default:
                            System.out.println(String.format("It's a mystery"));
                            last = lector.nextLine();
                            break;
                    }
                }
            }
            
            //System.out.println( tfactors.size()+" = "+ a );
            for (int i = 0; i < tfactors.size(); i++) {
                tfactors.get(i).ToPPM();
            //tfactors.get(i).PrintMatrix();
               //System.out.print(i + " = ");
           // tfactors.get(i).PrintHead();
            }
           //System.out.println(secuencia);
            TfBind(tfactors, secuencia, probabilidad);
    }
}
