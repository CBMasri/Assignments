// Program: EncoderDecoder

// This program uses command line arguments to encode, decode or crack
// a file that is chosen by the user. The encoded/decoded or cracked file
// is then written to a new file under the filename provided.

import java.util.*;		// for Scanner
import java.io.*;		// for File, FileNotFoundException, PrintStream

public class EncoderDecoder {

	public static void main(String[] args) {
	
		String action = args[0];		// User inputs Encode, Decode, or Crack
		
		File file = new File(args[2]);			// Creates output file
		PrintStream output = null;				// Creates PrintStream
		
		// Catches FileNotFoundException
		try {
			output = new PrintStream(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error creating file." + e);
		}
		
		if (action.equalsIgnoreCase("encode")) {				// Encodes input file
			encode(args[1], output, args[3]);
			System.out.println("Your file has been encoded.");
		} else if (action.equalsIgnoreCase("decode")) {			// Decodes input file
			decode(args[1], output, args[3]);
			System.out.println("Your file has been decoded.");
		} else if (action.equalsIgnoreCase("crack")) {			// Cracks input file
			String crackedkey = crack(args[1], output);
			decode(args[1], output, crackedkey);
			System.out.println("Cracked! The key is: " + crackedkey);
		} else {
			System.out.println("Sorry, your action could not be understood.");
			System.out.println("Please type encode, decode, or crack, followed by the input");
			System.out.println("file, the output file, and the key (if known).");
		}
	}
	
	// This method takes an input file and key, encodes the file using a substitution cipher,
	// and writes the encoded text to an output file designated by the user
	public static void encode(String input, PrintStream output, String thekey) {
		Scanner file = null;			// Creates Scanner
		
		int key = Integer.parseInt(thekey);		// Turns string thekey into int

		while (key < 1 || key > 25) {			// Ensures key is valid
			System.out.println("Your key must be between 1-25.");
			System.exit(0);
		}
		
		// Catches FileNotFoundException
		try {
		file = new Scanner(new File(input));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!" + e);
		}
		
		String readToken = "";			// Creates string from input file text
		char c;							// Creates character from letters in string
		
		while (file.hasNext()) {
			readToken = file.next();
			
			for (int i = 0; i < readToken.length(); i++) {		// Reads input file token by token
				c = readToken.charAt(i);
				c = (char)(c - 97);			// Subtract 97 to get Unicode values of alphabet (1-26)
				c += key;					// Add key to char value
				c %= 26;
				c += 97;					// Return to original value (shifted by key)
				output.print((char)(c));	// Prints encoded char to output file
			}
			output.print(" ");				// Prints spaces
		}
	}

	// This method takes an encoded input file and key, decodes the file with the key,
	// and writes the decoded text to an output file designated by the user
	public static void decode(String input, PrintStream output, String thekey) {
		Scanner file = null;
		
		int key = Integer.parseInt(thekey);		// Turns string thekey into int

		while (key < 1 || key > 25) {			// Ensures key is valid
			System.out.println("Your key must be between 1-25.");
			System.exit(0);
		}
		
		// Catches FileNotFoundException
		try {
		file = new Scanner(new File(input));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!" + e);
		}
		
		String readToken = "";			// Creates string from input file text
		char c;
		
		while (file.hasNext()) {
			readToken = file.next();
			
			for (int i = 0; i < readToken.length(); i++) {		// Reads input file token by token
				c = readToken.charAt(i);
				c = (char)(c - 97);				// Subtract 97 to get Unicode values of alphabet (1-26)
				c += (26 - key);				// Subtract key from char value, wraps around if less than 0
				c %= 26;
				c += 97;						// Return to original value (shifted by key)
				output.print((char)(c));		// Prints decoded char to output file
			}
			output.print(" ");					// Prints spaces
		}
	}

	// This method takes an encoded input file, figures out the key necessary
	// for decoding, and returns the key to main to be used by the decode method
	public static String crack(String input, PrintStream output) {
		Scanner file = null;
		
		// Catches FileNotFoundException
		try {
		file = new Scanner(new File(input));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!" + e);
		}

		int[] alpha = new int[26];			// Creates int array for every letter in the alphabet
		String readToken = "";				// Creates string
		char c;								// Creates char from letters in string
		
		while (file.hasNext()) {
			readToken = file.next();
			
			// Reads file token by token, fills array with int
			// values 0-25 (one for each letter of the alphabet)
			for (int i = 0; i < readToken.length(); i++) {
				c = readToken.charAt(i);
				alpha[c - 97]++;
			}		
		}
		
		int mostCommon = alpha[0];			// Creates int for most common letter in array
		int index = 0;						// Index of most common letter (for key)
		
		// Stores most commonly occurring value into mostCommon and its index
		for (int i = 0; i < alpha.length; i++) {
			if (alpha[i] > mostCommon) {
				mostCommon = alpha[i];
				index = i;
			}
		}
		index = index - 4;			// Gets key
		String key = Integer.toString(index);		// Turns int into string for decode method
		return key;				// Returns key
	}
}