package silva.relacaotabelas.bd.model;

public class Hash {
    private static final int a = 80849;
    private static final int b = 30259;

    private static long string2int(String paraInt){
        char[] aux = paraInt.toCharArray();
        long codigo = 0;
        for(int i=0;i< paraInt.length();i++)
            codigo += (long) aux[i];
        return codigo;
    }

    public static long geraCodigo(String y){
        long x = string2int(y);
        return (a*x) +b;
    }
}