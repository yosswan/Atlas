public class Jaspar {
    public String Matriz, Etiqueta, Senal;
    public float[] A, C, G, T;

    public Jaspar() {
        Matriz = ""; 
        Etiqueta = "";
        Senal = "" ;
        A=null;
        C=null;
        G=null;
        T=null;
    }

        public Jaspar(String cabecera, String StrA, String StrC, String StrG, String StrT) {
            String[] line = cabecera.split("\\s+");
            Matriz = line[0].replace(">", ""); 
            Etiqueta = line[1];
            Senal = "";

            line = StrA.split("\\s+");
            A = new float[line.length - 3];
                for (int i = 2 ; i < line.length-1; i++) {
                    String aux = line[i];
                    A[i-2] = Integer.parseInt(aux); 
                }   

            line = StrC.split("\\s+");
            C = new float[line.length - 3];
                for (int i = 2 ; i < line.length-1; i++) {
                    String aux = line[i];
                    C[i-2] = Integer.parseInt(aux); 
                }   

            line = StrG.split("\\s+");
            G = new float[line.length - 3];
                for (int i = 2 ; i < line.length-1; i++) {
                    String aux = line[i];
                    G[i-2] = Integer.parseInt(aux); 
                }   

            line = StrT.split("\\s+");
            T = new float[line.length - 3];
                for (int i = 2 ; i < line.length-1; i++) {
                    String aux = line[i];
                    T[i-2] = Integer.parseInt(aux); 
                }

                for (int i = 0; i < A.length; i++) {
                    //S = C or G, 
                    if( (A[i] == 0) && (C[i] != 0) && (G[i] != 0) && (T[i] == 0) ){
                        Senal = Senal + "S";
                    }else{
                        //W = A or T, 
                        if( (A[i] != 0) && (C[i] == 0) && (G[i] == 0) && (T[i] != 0) ){
                            Senal = Senal + "W";
                        }else{
                            //R = A or G, 
                            if( (A[i] != 0) && (C[i] == 0) && (G[i] != 0) && (T[i] == 0) ){
                                Senal = Senal + "R";
                            }else{
                                //Y = C or T, 
                                if( (A[i] == 0) && (C[i] != 0) && (G[i] == 0) && (T[i] != 0) ){
                                    Senal = Senal + "Y";
                                }else{
                                    //K = G or T, 
                                    if( (A[i] == 0) && (C[i] == 0) && (G[i] != 0) && (T[i] != 0) ){
                                        Senal = Senal + "K";
                                    }else{
                                        //M = A or C,
                                        if( (A[i] != 0) && (C[i] != 0) && (G[i] == 0) && (T[i] == 0) ){
                                            Senal = Senal + "M";
                                        }else{
                                            // A
                                            if( (C[i] + G[i] + T[i]) == 0 ){
                                                Senal = Senal + "A";
                                            }else{
                                                // C
                                                if( (A[i] + G[i] + T[i]) == 0 ){
                                                    Senal = Senal + "C";
                                                }else{
                                                    // G
                                                    if( (A[i] + C[i] + T[i]) == 0 ){
                                                        Senal = Senal + "G";
                                                    }else{
                                                        // T
                                                        if( (A[i] + C[i] + G[i]) == 0 ){
                                                            Senal = Senal + "T";
                                                        }else{
                                                            //N = any
                                                            Senal = Senal + "N";
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
        
        public void ToPPM(){
            for (int i = 0; i < this.A.length; i++) {
                float De = this.A[i] + this.C[i] + this.G[i] + this.T[i];
                this.A[i] = this.A[i] / De;
                this.C[i] = this.C[i] / De;
                this.G[i] = this.G[i] / De;
                this.T[i] = this.T[i] / De;
            }
        }
        public void PrintMatrix(){
            System.out.println(this.Matriz + " : "+ this.Etiqueta + " -> " + this.Senal);
            for (int i = 0; i < this.A.length; i++) {
                System.out.print(this.A[i]+"  ");
            }
            System.out.print(System.lineSeparator());
            for (int i = 0; i < this.C.length; i++) {
                System.out.print(this.C[i]+"  ");
            }
            System.out.print(System.lineSeparator());
            for (int i = 0; i < this.G.length; i++) {
                System.out.print(this.G[i]+"  ");
            }
            System.out.print(System.lineSeparator());
            for (int i = 0; i < this.T.length; i++) {
                System.out.print(this.T[i]+"  ");
            }
            System.out.print(System.lineSeparator());
        }
        
        public void PrintHead(){
            System.out.println( this.Matriz + " : "+ this.Etiqueta + " -> " + this.Senal);
        }
}
