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
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Encrypt {
    public static final String FILES_DIR = "files\\";
    public static Scanner fileIn;
    public static PrintStream out;
    public static String newOutLabel = "";
    public static JLabel outputLabel;
    public static int click = 1;

    public static void main(String[] args) {
        gui();
        AudioInputStream audioIn = null;

        Scanner in = new Scanner(System.in);
        Scanner fileIn;
        PrintStream out;
        char answer = ' ';

        // Tries to find an input file
        playSound(audioIn, "wav/start.wav");

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
            } catch(Exception e) {
                playSound(audioIn, "wav/fail.wav");
                if (e instanceof FileNotFoundException) {
                    System.out.println("Can't find file.");
                }
            }
        }

        // Tries to make an output file
        playSound(audioIn, "wav/start2.wav");
        while (true) {
            try {
                System.out.println("Output File:");
                String outFile = FILES_DIR + in.next();
                out = new PrintStream(new FileOutputStream(outFile));
                break;
            } catch (Exception e) {
                playSound(audioIn, "wav/fail.wav");
                System.out.println("Cannot create output file.");
            }
        }

        playSound(audioIn, "wav/start3.wav");
        // Asks if user wants to encrypt or decrypt
        while (answer != 'd' && answer != 'e') {
            System.out.println("Encrypt or decrypt?");
            String nextIn = in.next();
            answer = nextIn.charAt(0);
        }

        String res = endecryptFile(fileIn, answer);
        
        System.setOut(out);
        System.out.println(res);
        playSound(audioIn, "wav/finish.wav");
        try {
            Thread.sleep(2500);
        } catch(Exception e) {
            System.out.println(e);
        }
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

    public static void playSound(AudioInputStream audioIn, String fileName) {
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30f);
            clip.start();
        } catch(Exception e){
            System.out.println(e);
            System.exit(1);
        }
    }

    public static String getContentsOfFile(File file) {
        //have u seen bohemian rhapsody, i think its good i heard
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

    // Duplicate code, could probably simplify later.
    public static void gui() {

        newOutLabel = "Output Filename: ";

        JFrame frame = new JFrame("Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 250);

        JFileChooser inFileChooser = new JFileChooser(FILES_DIR);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton encrypt = new JButton("Encrypt");
        JButton decrypt = new JButton("Decrypt");

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
                    String contents = getContentsOfFile(file);
                    bigBox.setText(contents);
                }
            }
        });

        outputLabel.addMouseListener(new MouseAdapter() {
            // anotehr way could have done it with # of clicks and using %.
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                outFileTextField.setVisible(true);
                if (click % 4 == 0) {
                    outputLabel.setText("You only have to click ONCE, FOOL");
                    outFileTextField.setVisible(false);
                } else if (outputLabel.getText().equals("Output Filename: ")) {
                    outputLabel.setText("Output Pilename: ");
                } else {
                    outputLabel.setText("Output Filename: ");
                }
                click++;
            }
        });

        panel.add(inputLabel);
        panel.add(inFileTextField);
        panel.add(outputLabel);
        panel.add(outFileTextField);
        panel.add(encrypt);
        panel.add(decrypt);
        panel.add(scroll); //TAKE THIS SCROLL

        encrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = "";

                try {
                    fileName = inFileTextField.getText();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        throw new FileNotFoundException();
                    }
                    fileIn = new Scanner(file);
                } catch (Exception ex) {
                    System.out.println("Can't find file.");
                    return;
                }
                try {
                    String outFile = outFileTextField.getText();
                    out = new PrintStream(new FileOutputStream(FILES_DIR + outFile));
                } catch (Exception ex) {
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
            }
        });

        decrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fileName = "";

                try {
                    fileName = inFileTextField.getText();
                    File file = new File(fileName);
                    if (!file.exists()) {
                        throw new FileNotFoundException();
                    }
                    fileIn = new Scanner(file);
                } catch (Exception ex) {
                    System.out.println("Can't find file.");
                    return;
                }
                try {
                    String outFile = outFileTextField.getText();
                    out = new PrintStream(new FileOutputStream(FILES_DIR + outFile));
                } catch (Exception ex) {
                    System.out.println("Cannot create output file.");
                    return;
                }
                String decrypted = "";
                while (fileIn.hasNextLine()) {
                    String line = fileIn.nextLine();
                    String newWord;
                    newWord = decrypt(line);
                    decrypted += newWord;
                }
                System.out.println(fileName + " decrypted.");
                System.setOut(out);
                System.out.println(decrypted);
            }
        });

        //Adding Components to the frame.
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}

}
// milf sex