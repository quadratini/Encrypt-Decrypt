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

public class Encrypt {
    public static final String FILES_DIR = "files/";

    public static void main(String[] args) {

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
}