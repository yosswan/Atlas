
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JESUS REYES
 */
public class JasparConnection {
    boolean internet;
    
    public JasparConnection() {
        this.internet = false;
    }
    
    ArrayList<Jaspar> getTFactors(String matriz) throws FileNotFoundException {
        ArrayList<Jaspar> tfactors = new ArrayList<Jaspar>();
        
        boolean updateYearResult = updateYear();
        
        System.out.println( ( updateYearResult ) ? "Se actualizó la información de versión de las matrices." : "No se pudo actualizar la información de versión de las matrices." );
        
        Scanner lector;
        String cabecera = null, cad1 = null, cad2 = null, cad3 = null, cad4;
            int c=0;
            URL url;
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
                                    //System.out.println(String.format("It's a mystery"));
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
                            //System.out.println(String.format("It's a mystery"));
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
            
            return tfactors;
    }
    
    boolean updateYear(){
        System.out.println("Comprobando la versión de las matrices locales con respecto al servidor.");
        Scanner lector;
        URL url;
        try {
            url = new URL("http://jaspar.genereg.net/api/v1/releases/?format=json");

                // Realizando la petición GET
                URLConnection con = url.openConnection();

                // Leyendo el resultado
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String linea;
                linea = in.readLine();
                //System.out.println(linea);
                
                JsonParser parser = new JsonParser();

                // Obtain Array
                JsonObject gsonObj = parser.parse(linea).getAsJsonObject();

                // List of primitive elements
                JsonArray results = gsonObj.get("results").getAsJsonArray();
                int year = results.get(0).getAsJsonObject().get("year").getAsInt();
                //System.out.println(year);
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
                
                return true;
        } catch( IOException e ) {
            System.out.println(e.getMessage());
            this.internet = false;
            return false;
        }
    }
}           