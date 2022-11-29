import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 *  This program encrypt or decrypt given text based on algorithm and key.
 *  Parameters are passed through command line arguments.
 *  -mode enc/dec       -   to specify encryption or decryption
 *  -key  #             -   to specify key value
 *  -algo shift/unicode -   to specify algorithm to use
 *  -data "text value"  -   to specify text
 *  -in "filename"      -   to specify input file name
 *  -out "filename"     -   to specify output filename
 *
 *  If none of arguments are given it encrypt "Hello World!!!" using Shift algorithm and key = 0.
 *
 *  @author   - Trapti Tiwari
 *  @email    - traptit1@yahoo.com
 *  @linkedin - https://www.linkedin.com/in/tiwari-trapti/
 */

public class Main {
    private static final int lastAlphabet = 26;
    public static final int lastValue = 127;    // Last ascii value in unicode
    private static final String smallCaseAlphabets = "abcdefghijklmnopqrstuvwxyz";
    public static final String capitalCaseAlphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        //  Default values
        String operation = "enc";
        String input = "Hello World!!!";
        int key = 0;
        String outputFileName ="";
        String algorithm = "shift";
        StringBuilder output = new StringBuilder();

        //  Check for command line arguments to take input
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode" -> operation = args[++i];
                case "-key" -> key = Integer.parseInt(args[++i]);
                case "-data" -> input = args[++i];
                case "-in" -> input = readFromFile(args[++i]);
                case "-out" -> outputFileName = args[++i];
                case "-alg" -> algorithm = args[++i];
            }
        }

        //  Call different function based on algorithm and operation
        switch (algorithm) {
            case "unicode" -> {
                switch (operation) {
                    case "enc" -> encodeUnicode(input.toCharArray(), key, output);
                    case "dec" -> decodeUnicode(input.toCharArray(), key, output);
                }
            }
            case "shift" -> {
                switch (operation) {
                    case "enc" -> encodeShift(input.toCharArray(), key, output);
                    case "dec" -> decodeShift(input.toCharArray(), key, output);
                }
            }
        }

        //  If output file name is final result is saved to file otherwise shown to user.
        if (outputFileName.equals("")) {
            System.out.println(output);
        } else {
            writeToFile(output.toString(), outputFileName);
        }
    }

    /*
        Decode given text by replacing each alphabet with previous alphabet and return result.
        Previous alphabet is calculated based on given key in round manner (a to z then from a again).
        Only upper case and lower case alphabets are decoded, rest of the symbols remains same.
     */
    private static void decodeShift(char[] input, int key, StringBuilder output) {
        for (char ch : input) {
            if (smallCaseAlphabets.indexOf(ch) != -1) {
                output.append(smallCaseAlphabets.charAt((lastAlphabet + smallCaseAlphabets.indexOf(ch) - key) % lastAlphabet));
            } else if (capitalCaseAlphabets.indexOf(ch) != -1) {
                output.append(capitalCaseAlphabets.charAt((lastAlphabet + capitalCaseAlphabets.indexOf(ch) - key) % lastAlphabet));
            } else {
                output.append(ch);
            }
        }
    }

    /*
        Encode given text by replacing each alphabet with next alphabet and return result.
        Next alphabet is calculated based on given key in round manner (a to z then from a again).
        Only upper case and lower case alphabets are encoded, rest of the symbols remains same.
     */
    private static void encodeShift(char[] input, int key, StringBuilder output) {
        for (char ch : input) {
            if (smallCaseAlphabets.indexOf(ch) != -1) {
                output.append(smallCaseAlphabets.charAt((smallCaseAlphabets.indexOf(ch) + key) % lastAlphabet));
            } else if (capitalCaseAlphabets.indexOf(ch) != -1) {
                output.append(capitalCaseAlphabets.charAt((capitalCaseAlphabets.indexOf(ch) + key) % lastAlphabet));
            } else {
                output.append(ch);
            }
        }
    }

    /*
        Decode given text by replacing each symbol with previous symbol and return result.
        Previous symbol is calculated based on given key in round manner (using ascii table).
     */
    private static void decodeUnicode(char[] input, int key, StringBuilder output) {
        for (char ch : input) {
            output.append((char)(((int) ch - key) % lastValue));
        }
    }

    /*
        Encode given text by replacing each symbol with next symbol and return result.
        Next symbol is calculated based on given key in round manner (using ascii table).
     */
    private static void encodeUnicode(char[] input, int key, StringBuilder output) {
        for (char ch : input) {
            output.append((char)(((int) ch + key) % lastValue));
        }
    }

    /*
        Based on given input file name, data is loaded from file.
     */
    private static String readFromFile(String fileName) {

        StringBuilder input = new StringBuilder();
        File file = new File(fileName);

        try (Scanner readFile = new Scanner(file)) {
            while (readFile.hasNextLine()) {
                input.append(readFile.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error: File not found");
        }
        return input.toString();
    }

    /*
        Result is written to file based on given file name.
     */
    private static void writeToFile(String output, String fileName) {
        File file = new File(fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) {

            printWriter.println(output);
        } catch (IOException e) {
            System.out.println("Error: Can't write output to file");
        }
    }
}

