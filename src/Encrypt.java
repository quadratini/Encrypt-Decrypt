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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Encrypt {
    public static final String FILES_DIR = "";

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Scanner fileIn;
        PrintStream out;
        char answer = ' ';

        gui();
        // Tries to find an input file
        while (true) {
            try {
                System.out.println("File to encrypt/decrypt:");
                String fileName = FILES_DIR + in.next();
                File file = new File(fileName);
                if (!file.exists()) {
                    throw new FileNotFoundException();
                }
                fileIn = new Scanner(new File(fileName));
                break;
            } catch (Exception e) {
                System.out.println("Can't find file.");
            }
        }

        // Tries to make an output file
        while (true) {
            try {
                System.out.println("Output File:");
                String outFile = FILES_DIR + in.next();
                out = new PrintStream(new FileOutputStream(outFile));
                break;
            } catch (Exception e) {
                System.out.println("Cannot create output file.");
            }
        }

        // Asks if user wants to encrypt or decrypt
        while (answer != 'd' && answer != 'e') {
            System.out.println("Encrypt or decrypt?");
            String nextIn = in.next();
            answer = nextIn.charAt(0);
        }

        String res = endecryptFile(fileIn, answer);

        System.setOut(out);
        System.out.println(res);
    }

    public static String endecryptFile(Scanner fileIn, char answer) {
        String encrypted = "";
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            String newWord;
            if (answer == 'e') {
                newWord = encrypt(line);
            } else {
                newWord = decrypt(line);
            }

            encrypted += newWord;
        }
        return encrypted;
    }

    public static String decrypt(String line) {
        String decrypted = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') {
                decrypted += (char) c;    // CAST TO CHAR or else "lossy conversion"
            } else {
                decrypted += (char)(c - 1);
            }
        }
        return decrypted + "\n";
    }

    public static String encrypt(String line) {
        String encrypted = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') {
                encrypted += (char) c;    // CAST TO CHAR or else "lossy conversion"
            } else {
                encrypted += (char)(c + 1);
            }
        }
        return encrypted + "\n";
    }

    public static void gui() {
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton send = new JButton("Encrypt");
        JButton reset = new JButton("Decrypt");
        panel.add(send);
        panel.add(reset);


        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.setVisible(true);
    }
}
