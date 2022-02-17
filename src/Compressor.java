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
		//c.printSymbolCodeMap();

		//String compressedFileContents = c.compressFileContents(fileContents);
	}

	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap;
	HashMap<String, Double> symbolFrequencies;

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
		countFrequencies(list);

		//PriorityQueue use for creating codeMap
		//This queue will be exhausted to create the tree Priority Queue
		PriorityQueue<Map.Entry<String,Double>> queue = new PriorityQueue<Map.Entry<String,Double>>((a, b)-> {return Double.compare(a.getValue(), b.getValue());});

		//PriorityQueue which will serve as the Huffman code
		PriorityQueue<Tree> treeTree = new PriorityQueue<>();

		//Loop that creates a tree node for the treeTree PQ and also loads each PQ
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			//System.out.println(e);
			Tree tree = new Tree(e.getKey(),e.getValue());  //Node placed in Priority Queue
			queue.add(e);  //Priority queue used to create symbol table
			treeTree.add(tree);  //Priority queue used for compression
		}

		//System.out.println("Treetree size is " + treeTree.size());

		// Build code tree
		symbolTable(queue);

		PriorityQueue<Tree> freeTree = buildHuffman(treeTree);
		//testing only.  Commit out please
		printQueue(freeTree);


		//System.out.println("Expected value is " + expectedCodeLengthPerSymbol());
		// Create encoding map

	}

	public PriorityQueue<Tree> buildHuffman(PriorityQueue<Tree> t){
		PriorityQueue<Tree> treeTree3 = new PriorityQueue<>();
		int size = t.size();
		while(t.size()>1){
			Tree left = t.poll();  //The first node to merge.  It will have a string value of "0"
			left.setValue("0");  //set string value
			System.out.println("T is size " + t.size());
			if(t.isEmpty()){  //Just in case there is an odd number of nodes
				System.out.println("left " + left.getFrequency() + " right is null");
				Tree newNode = new Tree(left.getFrequency());
				newNode.setLeft(newNode);
				newNode.setValue(left.getValue());
				t.add(newNode);
			} else {    //For an even number of nodes with a left and a right merge to form a new node
				Tree right = t.poll();  //Pull the second node out of the queue
				right.setValue("1");  //set its string value to "1"
				System.out.println("left " + left.getFrequency() + " right " + right.getFrequency());
				Tree newNode = new Tree(left.getFrequency() + right.getFrequency()); //New for merging to
				newNode.setLeft(left);
				newNode.setRight(right);
				newNode.setValue(left.getValue() + right.getValue());
				t.add(newNode);
			}
		}
		System.out.println("Huffman build with size: " + size + " and final size " + t.size());
		return t;
	}

	//Hashmap function to create the hashmap for the ArrayList
	//public void countFrequencies(ArrayList<String> list)

	public void countFrequencies(ArrayList<String> list)
	{
		// hashmap to store the frequency of element
		symbolFrequencies = new HashMap<>();

		for (String sub : list) {
			Double j = symbolFrequencies.get(sub);
			//System.out.println("j is " + j);
			symbolFrequencies.put(sub, (j == null) ? 1 : j + 1); //Used for frequency counts

			//Used the following for percentage instead of frequency
			/*if(j == null){
				symbolFrequencies.put(sub,1.0/ list.size());
			} else {
				symbolFrequencies.put(sub, (j+1.0)/ list.size());
			}*/
		}
	}

	//  Prints out each symbol with its code
	public void printSymbolCodeMap() {
		System.out.println("Symbol Table is: ");
		for(Map.Entry<String,String> e: symbolCodeMap.entrySet()){
			System.out.println("Binary value: "+e.getKey() + " & Code is: " + e.getValue());
			//System.out.println(e);
			//finish = finish + e.getValue();
		}
	}

	public void symbolTable(Queue<Map.Entry<String,Double>> queue){

		symbolCodeMap = new HashMap<>();
		int size = queue.size();

		for(int j = 1; j <= size; j++){
			String s = Integer.toBinaryString(j);
			symbolCodeMap.put(queue.poll().getKey(),s);
		}

	}

	//
	public String compressFileContents(String fileContents) {

		return null;
	}

	// Using the frequencies of the symbols and lengths of their associated
	//  codeword, calculate the expected code length per symbol in fileContents
	public double expectedCodeLengthPerSymbol() {
		Double sum = 0.0;
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			//System.out.println("Binary value: "+e.getKey() + " & Frequency: " + e.getValue());
			//System.out.println(e);
			sum = sum + e.getValue();
		}

		return sum/symbolFrequencies.size();
	}

	//used for testing the treeTree Priority Queue.  Remove please before submission
	public void printQueue(PriorityQueue<Tree> treeTree){
		PriorityQueue<Tree> treeValue = treeTree;
		//PriorityQueue<Tree> treeCode = treeTree;
		while(!treeValue.isEmpty()){
			Tree x = treeValue.poll();
			System.out.println("Binary is " + x.getValue() + " Frequency value is " + x.getFrequency());
		}
	}

}

