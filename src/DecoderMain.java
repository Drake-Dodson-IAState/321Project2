import java.io.*;
import java.util.ArrayList;

public class DecoderMain {
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Please provide input file");
            System.exit(0);
        }

        String inFile = args[0];
        ArrayList<InstructionContainer> instructionList;

        // We may have to do something different for the outfile, not fully sure
        try {
            File createFile = new File("assembly.legv8asm");
            createFile.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try (
            // This is actually really hand because it closes the objects when done
            InputStream inputStream = new FileInputStream(inFile);
            FileWriter outFile = new FileWriter("assembly.legv8asm");
        ) {

            int byteRead;

            // I believe this is assuming it's big endian, I have no idea if we have to check to see if the system is big endian or not
            while((byteRead = inputStream.read()) != -1) {
                String instructionWord = "";
                int instructionBits = 0x0000;
                int i = 4;

                // Iterate 4 times to read each byte in the 32 bit (4 byte) data word
                // There may also be a more efficient way of doing this that reads each word of 4 bytes but it works for now
                // Do loop is necessary for error checking
                do {
                    i--;
                    byteRead = byteRead << (i * 8);
                    instructionBits = byteRead | instructionBits;
                } while(i > 0 && ((byteRead = inputStream.read()) != -1));

                String byteString = String.format("%32s", Integer.toBinaryString(instructionBits)).replace(' ', '0');

                // This may not even be need but just to be safe
                if(i != 0){
                    throw new Exception("Uneven number of bytes detected");
                }

                outFile.write(byteString + " \n");
                determineOpCode(instructionBits);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void determineOpCode(int instructionWord) {
        if((instructionWord & 0xFC000000) == 0x14000000){
            // Branch
            System.out.println("Branch");
        } else if ((instructionWord & 0xFC000000) == 0x94000000){
            System.out.println("Branch Link");
            // Branch Link
        } else if ((instructionWord & 0xFF000000) == 0x54000000) {
            System.out.println("Branch Conditional");
            // Branch Conditional
        } else if ((instructionWord & 0xFF000000) == 0xB5000000) {
            System.out.println("Compare Not Zero");
            // Compare Not Zero
        } else if ((instructionWord & 0xFF000000) == 0xB4000000) {
            System.out.println("Compare Zero");
            // Compare Not Zero
        } else if ((instructionWord & 0xFFE00000) == 0x8B000000) {
            System.out.println("Add");
            // Compare Not Zero
        } else {
            System.out.println("N/A");
        }
    }
}

