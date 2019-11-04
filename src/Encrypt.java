/*
 * This program asks the user the name
 * of a file they want encrypted or decrypted.
 * It then prompts them to enter the output file.
 * Then asks them if they want the file
 * they inputted to be encrypted/decrypted.
 * outputs file to the outputfile named.
 *
 * @author Ronny Ritprasert
 * @verison 03/24/2019
 *
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Encrypt {
    private static final String FILES_DIR = "files\\";
    private static Scanner fileIn;
    private static PrintStream out;
    private static String newOutLabel = "";
    private static JLabel outputLabel;
    private static int click = 1;
    private static AudioInputStream audioIn;
    private static String fileName = "";

    public static void main(String[] args) {
        gui();

        // Tries to find an input file
        playSound(audioIn, "wav/start.wav");
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

    private static String decrypt(String line) {
        String decrypted = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') {
                decrypted += (char) c;    // CAST TO CHAR or else "lossy conversion"
            } else {
                decrypted += (char) (c - 1);
            }
        }
        return decrypted + "\n";
    }

    private static String encrypt(String line) {
        String encrypted = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') {
                encrypted += (char) c;    // CAST TO CHAR or else "lossy conversion"
            } else {
                encrypted += (char) (c + 1);
            }
        }
        return encrypted + "\n";
    }

    private static void playSound(AudioInputStream audioIn, String fileName) {
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30f);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    private static String getContentsOfFile(File file) {
        String allOfIt = "";

        try {
            Scanner sex = new Scanner(file);
            while (sex.hasNextLine()) {
                allOfIt += sex.nextLine() + "\n";
            }
        } catch (Exception e) {
            return "";
        }
        return allOfIt;
    }

    private static ActionListener getL(JButton encryptButton, JButton decryptButton, JTextField inFileTextField,
                                       JTextField outFileTextField, JTextArea bigBox) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    fileName = inFileTextField.getText();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        throw new FileNotFoundException();
                    }
                    fileIn = new Scanner(file);
                } catch (Exception ex) {
                    playSound(audioIn, "wav/fail.wav");
                    System.out.println("Can't find file.");
                    return;
                }
                try {
                    String outFile = outFileTextField.getText();
                    out = new PrintStream(new FileOutputStream(FILES_DIR + outFile));
                } catch (Exception ex) {
                    playSound(audioIn, "wav/fail.wav");
                    System.out.println("Cannot create output file.");
                    return;
                }

                String encrypted = "";
                while (fileIn.hasNextLine()) {
                    String line = fileIn.nextLine();
                    String newWord;
                    newWord = encrypt(line);
                    encrypted += newWord;
                }
                System.out.println(fileName + " encrypted.");
                System.setOut(out);
                System.out.println(encrypted);
                playSound(audioIn, "wav/finish.wav");
                bigBox.setText("");
                try {
                    decryptButton.setEnabled(false);
                    encryptButton.setEnabled(false);
                    Thread.sleep(2500);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                encryptButton.setEnabled(true);
                decryptButton.setEnabled(true);
                inFileTextField.setText("");
                outFileTextField.setText("");
            }
        };
    }

    private static void gui() {
        newOutLabel = "Output Filename: ";

        JFrame frame = new JFrame("Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 250);
        JFileChooser inFileChooser = new JFileChooser(FILES_DIR);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton encryptButton = new JButton("Encrypt");
        JButton decryptButton = new JButton("Decrypt");

        JLabel inputLabel = new JLabel("Input File:");
        inputLabel.setBounds(50, 50, 100, 30);
        outputLabel = new JLabel(newOutLabel);
        outputLabel.setBounds(50, 50, 100, 30);

        JTextField inFileTextField = new JTextField(10);
        inFileTextField.setBounds(50, 50, 100, 30);
        JTextField outFileTextField = new JTextField(10);
        outFileTextField.setBounds(50, 50, 100, 30);

        JTextArea bigBox = new JTextArea(9, 40);
        bigBox.setEditable(false);
        bigBox.setBackground(new Color(220, 220, 220));
        JScrollPane scroll = new JScrollPane(bigBox);
        frame.setResizable(false);

        inFileTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int returnVal = inFileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    inFileTextField.setText(inFileChooser.getSelectedFile().getAbsolutePath());
                    //chop on head
                    File file = inFileChooser.getSelectedFile();
                    if (file.exists()) {
                        String contents = getContentsOfFile(file);
                        bigBox.setText(contents);
                        playSound(audioIn, "wav/start2.wav");
                    } else {
                        playSound(audioIn, "wav/fail.wav");
                    }
                }
            }
        });

        outputLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                outFileTextField.setVisible(true);
                if (click % 4 == 0) {
                    outputLabel.setText("You only need to click ONCE, FOOL");
                    outFileTextField.setVisible(false);
                    playSound(audioIn, "wav/morde.wav");
                } else if (outputLabel.getText().equals("Output Filename: ")) {
                    outputLabel.setText("Output Pilename: ");
                } else {
                    outputLabel.setText("Output Filename: ");
                }
                click++;
            }
        });

        encryptButton.addActionListener(getL(encryptButton, decryptButton, inFileTextField, outFileTextField, bigBox));
        decryptButton.addActionListener(getL(encryptButton, decryptButton, inFileTextField, outFileTextField, bigBox));

        panel.add(inputLabel);
        panel.add(inFileTextField);
        panel.add(outputLabel);
        panel.add(outFileTextField);
        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(scroll); //TAKE THIS SCROLL

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
// milf sex