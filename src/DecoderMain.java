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
        // 6 bits
        if((instructionWord & 0xFC000000) == 0x14000000){
            // Branch
            System.out.println("Branch");
        } else if ((instructionWord & 0xFC000000) == 0x94000000){
            System.out.println("Branch Link");
            // Branch Link
        }
        // 8 bits
        else if ((instructionWord & 0xFF000000) == 0x54000000) {
            System.out.println("Branch Conditional");
            // Branch Conditional
        } else if ((instructionWord & 0xFF000000) == 0xB5000000) {
            System.out.println("Compare Not Zero");
            // Compare Not Zero
        } else if ((instructionWord & 0xFF000000) == 0xB4000000) {
            System.out.println("Compare Zero");
            // Compare Zero
        }
        // 10 bits
        else if ((instructionWord & 0xFFC00000) == 0x91000000) {
            System.out.println("Add I");
            // Add Immediate
        } else if ((instructionWord & 0xFFC00000) == 0x92000000) {
            System.out.println("And I");
            // And Immediate
        } else if ((instructionWord & 0xFFC00000) == 0xD2000000) {
            System.out.println("Exclusive Or I");
            // Exclusive Or Immediate
        } else if ((instructionWord & 0xFFC00000) == 0xB2000000) {
            System.out.println("Orr I");
            // Orr Immediate
        } else if ((instructionWord & 0xFFC00000) == 0xD1000000) {
            System.out.println("Subtract I");
            // Sub Immediate
        }


        // 11 bits
        else if ((instructionWord & 0xFFE00000) == 0x8B000000) {
            System.out.println("Add");
            // Add
        } else if ((instructionWord & 0xFFE00000) == 0x8A000000) {
            System.out.println("And");
            // And
        } else if ((instructionWord & 0xFFE00000) == 0xD6000000) {
            System.out.println("Branch Register");
            // Branch to register
        }  else if ((instructionWord & 0xFFE00000) == 0xCA000000) {
            System.out.println("Exclusive Or");
            // Exclusive Or
        } else if ((instructionWord & 0xFFE00000) == 0xF8400000) {
            System.out.println("Load Register");
            // Load Register
        } else if ((instructionWord & 0xFFE00000) == 0xF8000000) {
            System.out.println("Store Register");
            // Store
        } else if ((instructionWord & 0xFFE00000) == 0xD3600000) {
            System.out.println("Logical Shift Left");
            // Logical Shift left
        } else if ((instructionWord & 0xFFE00000) == 0xD3400000) {
            System.out.println("Logical Shift Right");
            // Logical Shift left
        } else if ((instructionWord & 0xFFE00000) == 0xAA000000) {
            System.out.println("Orr");
            // Orr
        } else if ((instructionWord & 0xFFE00000) == 0xCB000000) {
            System.out.println("Subtract");
            // Subtract
        } else if ((instructionWord & 0xFFE00000) == 0xF1000000) {
            System.out.println("Subtract Immediate and Set");
            // Subtract Immediate and Set
        } else if ((instructionWord & 0xFFE00000) == 0xEB000000) {
            System.out.println("Subtract and Set");
            // Subtract and Set
        } else if ((instructionWord & 0xFFE00000) == 0x9B000000) {
            System.out.println("Multiply");
            // Multiply
        } else if ((instructionWord & 0xFFE00000) == 0xFFC00000) {
            System.out.println("DUMP");
            // Dump
        } else if ((instructionWord & 0xFFE00000) == 0xFFE00000) {
            System.out.println("HALT");
            // Halt
        } else if ((instructionWord & 0xFFE00000) == 0xFF800000) {
            System.out.println("Prnl");
            // Prnl
        } else if ((instructionWord & 0xFFE00000) == 0xFFA00000) {
            System.out.println("Prnt");
            // Prnt
        } else {
            System.out.println("N/A");
        }
    }
}

