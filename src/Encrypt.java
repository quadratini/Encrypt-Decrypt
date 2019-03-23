/*
* This program asks the user the name 
* of a file they want encrypted or decrypted.
* It then prompts them to enter the output file.
* Then asks them if they want the file
* they inputted to be encrypted/decrypted.
* outputs file to the outputfile named.
*
* @author Ronny Ritprasert
* @verison 03/22/2019
*
*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Encrypt {
    public static final String FILES_DIR = "files\\";
    public static Scanner fileIn;
    public static PrintStream out;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Scanner fileIn;
        char answer = ' ';

        // full gui program version performed here.
        gui();
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

    // Duplicate code, could probably simplify later.
    public static void gui() {

        JFrame frame = new JFrame("Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,200);

        JFileChooser inFileChooser = new JFileChooser(FILES_DIR);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton encrypt = new JButton("Encrypt");
        JButton decrypt = new JButton("Decrypt");

        JLabel whichFile = new JLabel("Input File:");
        whichFile.setBounds(50,50,100,30);
        JLabel outputFile = new JLabel("Output Filename:");
        outputFile.setBounds(50,50,100,30);

        JTextField inFileTextField = new JTextField(10);
        inFileTextField.setBounds(50,50,100,30);
        JTextField outFileTextField = new JTextField(10);
        outFileTextField.setBounds(50,50,100,30);

        inFileTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int returnVal = inFileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    inFileTextField.setText(inFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        panel.add(whichFile);
        panel.add(inFileTextField);
        panel.add(outputFile);
        panel.add(outFileTextField);
        panel.add(encrypt);
        panel.add(decrypt);

        encrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = false;
                String fileName = "";

                while (!flag) {
                    try {
                        String inFileTextBox = inFileTextField.getText();
                        fileName = inFileTextBox;
                        File file = new File(fileName);
                        if (!file.exists()) {
                            throw new FileNotFoundException();
                        }
                        fileIn = new Scanner(file);
                        break;
                    } catch (Exception ex) {
                        System.out.println("Can't find file.");
                        flag = true;
                    }
                }
                while (!flag) {
                    try {
                        String outFile = outFileTextField.getText();
                        out = new PrintStream(new FileOutputStream(FILES_DIR + outFile));
                        break;
                    } catch (Exception ex) {
                        System.out.println("Cannot create output file.");
                        flag = true;
                    }
                }

                if (!flag) {
                    String encrypted = "";
                    while (fileIn.hasNextLine()) {
                        String line = fileIn.nextLine();
                        String newWord = "";
                        newWord = encrypt(line);
                        encrypted += newWord;
                    }
                    System.out.println(fileName + " encrypted.");
                    System.setOut(out);
                    System.out.println(encrypted);
                }
            }
        });

        decrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean flag = false;
                String fileName = "";

                while (!flag) {
                    try {
                        String inFileTextBox = inFileTextField.getText();
                        fileName = inFileTextBox;
                        File file = new File(FILES_DIR + fileName);
                        if (!file.exists()) {
                            throw new FileNotFoundException();
                        }
                        fileIn = new Scanner(file);
                        break;
                    } catch (Exception ex) {
                        System.out.println("Can't find file.");
                        flag = true;
                    }
                }
                while (!flag) {
                    try {
                        String outFile = outFileTextField.getText();
                        out = new PrintStream(new FileOutputStream(FILES_DIR + outFile));
                        break;
                    } catch (Exception ex) {
                        System.out.println("Cannot create output file.");
                        flag = true;
                    }
                }
                if (!flag) {
                    String decrypted = "";
                    while (fileIn.hasNextLine()) {
                        String line = fileIn.nextLine();
                        String newWord = "";
                        newWord = decrypt(line);
                        decrypted += newWord;
                    }
                    System.out.println(fileName + " decrypted.");
                    System.setOut(out);
                    System.out.println(decrypted);
                }
            }
        });

        //Adding Components to the frame.
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
