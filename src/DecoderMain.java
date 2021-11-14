import java.io.*;

public class DecoderMain {
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Please provide input file");
            System.exit(0);
        }

        System.out.println("Hello?");
        // We'll need to create an outfile of some sort
        String inFile = args[0];

        try(InputStream inputStream = new FileInputStream(inFile);)
        {
            int byteRead = inputStream.read();
            String s1 = String.format("%8s", Integer.toBinaryString(byteRead & 0xFF)).replace(' ', '0');
            System.out.println(s1); // 10000001
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

