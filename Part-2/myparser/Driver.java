import java_cup.runtime.*;
import java.io.*;

class Driver {
    public static void main(String[] argv) throws Exception{
        System.err.println("START TYPING CODE (Ctrl-D when finished to generate java code)");
        Parser p = new Parser(new Scanner(new InputStreamReader(System.in)));
        p.parse();
    }
}
