import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

		ArrayList<String> list = new ArrayList<>();
		int length = fileContents.length();
		int reminder = length % SYMBOL_LENGTH;  //just in case input is not / 8

		for(int i = 0; i < fileContents.length()-reminder; i = i + SYMBOL_LENGTH){

			list.add(fileContents.substring(i,i+SYMBOL_LENGTH)); //add 8 char substring to list
		}

		// create Hashmap for frequency counts
		Map<String, Integer> map = countFrequencies(list);

		//While loop used to look at hashmap.  Not a necessary loop
		/*Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {   ///used to print the hashmap
			Map.Entry me2 = (Map.Entry) iterator.next();
			System.out.println("Binary value: "+me2.getKey() + " & Frequency: " + me2.getValue());
		}*/

		System.out.println("Map size is " + map.size());

		Queue<Map.Entry<String,Integer>> queue = new PriorityQueue<>((a, b)-> {return a.getValue()- b.getValue();});

		for(Map.Entry<String,Integer> e: map.entrySet()){
			//System.out.println("Binary value: "+e.getKey() + " & Frequency: " + e.getValue());
			System.out.println(e);
			queue.add(e);
		}

		System.out.println(" ");
		for(int i = 0; i < map.size(); i++){
			System.out.println(queue.poll());
		}


		// Build code tree
		
		// Create encoding map
		
		
	}

	//Hashmap function to create the hashmap for the ArrayList
	public static Map<String, Integer> countFrequencies(ArrayList<String> list)
	{
		// hashmap to store the frequency of element
		Map<String, Integer> map = new HashMap<>();
		//symbolFrequencies

		for (String sub : list) {
			Integer j = map.get(sub);
			map.put(sub, (j == null) ? 1 : j + 1);
		}

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

