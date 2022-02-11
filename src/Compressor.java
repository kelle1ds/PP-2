import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;

public class Compressor {

	public static void main(String[] args) {
		String fileContents = "";
		
		try {
			Scanner scnr = new Scanner(new File("File.txt"));
			fileContents = scnr.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Compressor c = new Compressor(fileContents);
		c.printSymbolCodeMap();
		
		String compressedFileContents = c.compressFileContents(fileContents);
		
	}
	
	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap;
	HashMap<String, Double> symbolFrequencies;
	
	// The constructor should build a symbol to code map based on the 
	//  symbol frequencies in the fileContents provided.
	// Chunk through the file win lengths of SYMBOL_LENGTH;
	Compressor(String fileContents) {

		PriorityQueue<HashMap> pQueue = new PriorityQueue<>();
		ArrayList<String> list = new ArrayList<>();

		int length = fileContents.length();
		int reminder = length % SYMBOL_LENGTH;  //just in case input is not / 8

		for(int i = 0; i < fileContents.length()-reminder; i = i + SYMBOL_LENGTH){

			//System.out.println(fileContents.substring(i,i+SYMBOL_LENGTH));  //test print
			list.add(fileContents.substring(i,i+SYMBOL_LENGTH)); //add 8 char substring to list
		}

		// create Hashmap for frequency counts
		HashMap map = countFrequencies(list);

		//map.entrySet();

		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry me2 = (Map.Entry) iterator.next();
			System.out.println("Binary value: "+me2.getKey() + " & Frequency: " + me2.getValue());
		}

		// Build code tree
		
		// Create encoding map
		
		
	}

	//Hashmap function to create the hashmap for the ArrayList
	public static HashMap countFrequencies(ArrayList<String> list)
	{
		// hashmap to store the frequency of element
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		for (String sub : list) {
			Integer j = map.get(sub);
			map.put(sub, (j == null) ? 1 : j + 1);
		}

		// Display frequency while creating.  Remove before submission
		/*for (HashMap.Entry<String, Integer> val : map.entrySet()) {
			System.out.println("Element " + val.getKey() + " "
					+ "occurs"
					+ ": " + val.getValue() + " times");
		}*/
		return map;
	}
	
	//  Prints out each symbol with its code
	public void printSymbolCodeMap() {
		
	}

	// 
	public String compressFileContents(String fileContents) {
		
		return null;
	}
	
	// Using the frequencies of the symbols and lengths of their associated
	//  codeword, calculate the expected code length per symbol in fileContents
 	public double expectedCodeLengthPerSymbol() {
 		
 		return 0;
 	}
 	
}

