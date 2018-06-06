package com.company;

/* Author: Abdul El Badaoui
 * Student Number: 5745716
 * Description: This programs sort the single, two character, and three character occurrences and launch a website to
 * assist the user in the decrypting a secret message using substitution cipher.
 * */

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Main {

    //parameters
    static int[] charCount;
    static String [] fileInput;
    static String cipherText, plainText;
    static Map<String, Integer> commonLetter, twoCommonSubstring, threeCommonSubstring;
    static List<Map.Entry<String, Integer>> singleLetterList, twoLetterList, threeLetterList;
    static Scanner userInput;
    static int count;

    public static void main(String[] args) {
        //methods that runs the whole program
        initialize();
        getFrequency();
        sortFrequencies();
        launchLookUpFrequencySite();
        decryptSubstitutionCipher();
        printOut();
    }

    //method that initialize all the initial parameters
    public static void initialize(){
        charCount = new int[127];

        commonLetter = new HashMap<>();
        twoCommonSubstring = new HashMap<>();
        threeCommonSubstring = new HashMap<>();

        try {
            fileInput = Files.readAllLines(Paths.get("a3q1in.txt")).toArray(new String[0]);
        }  catch (IOException e) {
            e.printStackTrace();
        }

        cipherText = "";
        for (int i = 0; i<fileInput.length; i++){
            cipherText+=fileInput[i];
        }

        plainText = "";
        for (int i = 0; i<cipherText.length(); i++){
            plainText+="-";
        }

    }

    //method that gathers the single, two, and three letter frequencies
    public static void getFrequency(){
        char [] cipherCharArray = cipherText.toCharArray();

        for (int i = 0; i < cipherCharArray.length; i++){
            charCount[cipherCharArray[i]]++;
        }

        for (int i = 65; i < 91; i++){
            if (charCount[i] != 0) commonLetter.put(""+((char) i), charCount[i]);
        }

        twoCommonSubstring = lettersFrequencies(2);
        threeCommonSubstring = lettersFrequencies(3);
    }

    //method that handles gathering common substring frequencies
    public static Map<String, Integer> lettersFrequencies(int subLength){
        ArrayList<String> lettersCheck = new ArrayList<>();
        Map<String, Integer> commonSubstring = new HashMap<>();

        for (int i = 0; i <cipherText.length()-(subLength-1); i++){
            count  = 1;
            String commonLetters = cipherText.substring(i, i+subLength);
            if (!lettersCheck.contains(commonLetters)){
                for (int j = i+1; j<cipherText.length()-(subLength-1); j++){
                    String comparedSubString = cipherText.substring(j, j+subLength);
                    if (commonLetters.equals(comparedSubString)){
                        count++;
                    }
                }
                if (count>1){
                    lettersCheck.add(commonLetters);
                    commonSubstring.put(commonLetters, count);
                }
            }

        }

        return commonSubstring;


    }

    //method that sort the single, two, and three letter frequencies in descending order
    public static void sortFrequencies(){
        singleLetterList = sortFrequenciesList(commonLetter);
        twoLetterList = sortFrequenciesList(twoCommonSubstring);
        threeLetterList = sortFrequenciesList(threeCommonSubstring);
    }

    //method code for sorting the frequencies for the sortFrequencies() method
    public static List<Map.Entry<String, Integer>> sortFrequenciesList(Map<String, Integer> entryList){
        List<Map.Entry<String, Integer>> orderedList = new ArrayList<> (entryList.entrySet());
        Collections.sort(orderedList, (a, b) -> b.getValue().compareTo(a.getValue()));
        return orderedList;
    }

    //method that launches the site for assisting the use in decrypting the message using substitution cipher
    public static void launchLookUpFrequencySite(){
        Desktop d = Desktop.getDesktop();
        try {
            d.browse(new URI("https://www3.nd.edu/~busiforc/handouts/cryptography/cryptography%20hints.html"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //method that runs a loop that asks the user what letters to substitute to decipher the message
    // the one, two, and three letter frequencies of the cipher text to assist the user.
    public static void decryptSubstitutionCipher(){
        boolean end = false;
        userInput = new Scanner(System.in);
        count = 0;

        while(!end){
            //display the current plaintext and ciphertext for the user can see current plaintext after each change
            //the ciphertext will always be below plaintext to distinguish the two and see what letters are still needed
            // to substitute
            System.out.println(plainText);
            System.out.println(cipherText);

            //print the frequency lists
            System.out.println();
            System.out.println("The single letter common list is :");
            for (Map.Entry<String, Integer> item : singleLetterList){
                System.out.print("("+item.getKey()+", "+item.getValue()+"), ");
            }
            System.out.println();
            System.out.println("The two letter common list is :");

            for (Map.Entry<String, Integer> item : twoLetterList){
                System.out.print("("+item.getKey()+", "+item.getValue()+"), ");
            }
            System.out.println();

            System.out.println("The three letter common list is :");

            for (Map.Entry<String, Integer> item : threeLetterList){
                System.out.print("("+item.getKey()+", "+item.getValue()+"), ");
            }
            System.out.println();

            //asks the user for the letter substitutions
            System.out.println("Which Letters you would like to change (select up to three): ");
            String selectedcipherSubstring = userInput.next();
            System.out.println("Which Letters you want to change it to (select up to three): ");
            String selectedplainSubstring = userInput.next();
            //checks if the substitution is valid (1 to 1 character substitution)
            while (selectedcipherSubstring.length()!=selectedplainSubstring.length() && selectedcipherSubstring.length()<4){
                System.out.println("mismatch substring");
                System.out.println("Which Letters you would like to change (select up to three): ");
                selectedcipherSubstring = userInput.next();
                System.out.println("Which Letters you want to change it to (select up to three): ");
                selectedplainSubstring = userInput.next();
            }

            int upToValue = selectedplainSubstring.length();
            char [] cipherChar = selectedcipherSubstring.toCharArray();
            char [] plainChar = selectedplainSubstring.toCharArray();

            for (int i = 0; i< upToValue; i++){
                for (int j = 0; j<cipherText.length(); j++){
                    if (cipherText.charAt(j) == cipherChar[i]){
                        plainText = plainText.substring(0, j)+plainChar[i]+plainText.substring(j+1, plainText.length());
                    }
                }
                count++;
            }
            //checks if the message is decrypted and asks the user if he/she likes to exit.
            if (count>=singleLetterList.size()){
                System.out.println(plainText);
                System.out.println(cipherText);
                System.out.println("please enter 'EXIT' to end the program");
                String finish = userInput.next();
                if (finish.equals("EXIT")) end = true;
            }


        }
    }

    //method that prints out the decrypted message in a text file
    public static void printOut(){
        try {
            PrintWriter fileOutput = new PrintWriter("a3q1out.txt", "UTF-8");// output file creation
            fileOutput.println("The secret message (plaintext) is: ");
            fileOutput.println();
            fileOutput.println(plainText);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
