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
	//HashMap<String, Double> symbolFrequencies;
	HashMap<String, Integer> symbolFrequencies;
	String finish = "";
	
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
		HashMap<String, Integer> map = countFrequencies(list);
		//countFrequencies(list);

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

		/*System.out.println(" ");
		for(int i = 0; i < map.size(); i++){
			System.out.println(queue.poll());
		}*/

		System.out.println("Queue size is " + queue.size());


		Map<String, String> code = symbolTable(queue);
		//symbolTable(queue);
		System.out.println("code size is " + code.size());


		for(Map.Entry<String,String> e: code.entrySet()){
			//System.out.println("Binary value: "+e.getKey() + " & Frequency: " + e.getValue());
			System.out.println(e);
			finish = finish + e.getValue();
		}

		printSymbolCodeMap();



		// Build code tree
		
		// Create encoding map
		
		
	}

	//Hashmap function to create the hashmap for the ArrayList
	//public void countFrequencies(ArrayList<String> list)

	public static HashMap<String, Integer> countFrequencies(ArrayList<String> list)
	{
		// hashmap to store the frequency of element
		HashMap<String, Integer> map = new HashMap<>();
		//symbolFrequencies

		for (String sub : list) {
			Integer j = map.get(sub);
			map.put(sub, (j == null) ? 1 : j + 1);
		}

		return map;
	}
	
	//  Prints out each symbol with its code
	public void printSymbolCodeMap() {
		System.out.println(finish);
	}

	public static Map<String, String> symbolTable(Queue<Map.Entry<String,Integer>> queue){
		//public void symbolTable(Queue<Map.Entry<String,String>> queue){

		Map<String, String> code = new HashMap<>();
		System.out.println("Queue size is " + queue.size());

		for(int j = 1; j <= queue.size(); j++){
			int i = j;
			System.out.println(i + " " + "Queue size is " + queue.size());

			String s = Integer.toBinaryString(i);

			code.put(queue.poll().getKey(),s);
			//System.out.println(queue.poll());

		}
		return code;
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

