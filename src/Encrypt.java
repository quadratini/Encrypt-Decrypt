/*
* This program asks the user the name 
* of a file they want encrypted or decrypted.
* It then prompts them to enter the output file.
* Then asks them if they want the file
* they inputted to be encrypted/decrypted.
* outputs file to the outputfile named.
*
* @author Ronny Ritprasert
* @verison 03/17/2019
*
*/
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Encrypt {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Scanner fileIn;
        PrintStream out;
        String answer = "";

        // Tries to find an input file
        while (true) {
            try {
                System.out.println("File to encrypt/decrypt:");
                String fileName = in.next();
                fileIn = new Scanner(new File(fileName).getAbsolutePath());
                break;
            } catch (Exception e) {
                System.out.println("Can't find file.");
            }
        }

        // Tries to make an output file
        while (true) {
            try {
                System.out.println("Output File:");
                String outFile = in.next();
                out = new PrintStream(new FileOutputStream(outFile));
                break;
            } catch (Exception e) {
                System.out.println("Cannot create output file.");
            }
        }

        while (!answer.equalsIgnoreCase("encrypt") && !answer.equalsIgnoreCase("decrypt")) {
            System.out.println("Encrypt or decrypt?");
            String ende = in.next();
            in.nextLine(); // consumes the rest of the line if the input is invalid.
            answer = ende;
        }
        
        String s = encryptFile(fileIn, answer);
        
        System.setOut(out);
        System.out.println(s);
    }
    
    public static String encryptFile(Scanner fileIn, String ende) {
        String encrypted = "";
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            String newWord = encrypt(line, ende);
            encrypted += newWord;
        }
        return encrypted;
    }
   
    public static String encrypt(String line, String ende) {
        String encrypted = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') { 
                encrypted += (char)c;    // CAST TO CHAR or else "lossy conversion"
            } else if (ende.equalsIgnoreCase("encrypt")) {
                encrypted += (char)(c + 1);
            } else if (ende.equalsIgnoreCase("decrypt")){
                encrypted += (char)(c - 1);
            }             
        }
        return encrypted + "\n";
    }  
}