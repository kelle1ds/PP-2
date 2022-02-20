import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Compressor Class.
 * @author kelle1ds
 *
 */
public class Compressor {

	public static void main(String[] args) {
		String fileContents = "";

		try {
			Scanner scnr = new Scanner(new File("File.txt"));
			fileContents = scnr.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Original File length is " + fileContents.length());
		Compressor c = new Compressor(fileContents);
		c.printSymbolCodeMap();
		String compressedFileContents = c.compressFileContents(fileContents);
		System.out.println(compressedFileContents);
		System.out.println("Compressed File length is " + compressedFileContents.length());

	}
	//Global Variables in Compressor Class
	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap;
	HashMap<String, Double> symbolFrequencies;
	String finish = "";

	/**
	 * Main constructor for class
	 * Method receives the file contents as one String
	 * File is streamed into an arraylist 8 char chucks at a time
	 * @author kelle1ds
	 * @param fileContents String variable
	 * @return nothing.
	 */
	Compressor(String fileContents) {

		ArrayList<String> list = new ArrayList<>();
		int length = fileContents.length();
		int reminder = length % SYMBOL_LENGTH;  //just in case input is not / 8

		for(int i = 0; i < fileContents.length()-reminder; i = i + SYMBOL_LENGTH){
			list.add(fileContents.substring(i,i+SYMBOL_LENGTH)); //add 8 char substring to list
		}

		// create Hashmap for frequency counts
		countFrequencies(list);

		//PriorityQueue which will be passed to the buildHuffman() method
		PriorityQueue<Tree> treeTree = new PriorityQueue<>();

		//Loop that creates a tree node for the treeTree PQ and also loads each PQ
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			Tree tree = new Tree(e.getKey(),e.getValue());  //Node placed in Priority Queue
			treeTree.add(tree);  //Priority queue used for compression
		}

		//Build the Huffman code tree
		System.out.println("Build Huffman");

		PriorityQueue<Tree> freeTree = buildHuffman(treeTree);
		System.out.println("Byild lookup");

		BuildLookupTable(freeTree.peek());
		System.out.println("Print tree " + freeTree.peek().getValue());
	}

	/**
	 * Builds the huffman code tree
	 * @author kelle1ds
	 * @param t Priority Queue of Tree elements is passed into the method.
	 * @return t the Priority Queue with only one element.
	 */
	public PriorityQueue<Tree> buildHuffman(PriorityQueue<Tree> t){

		int size = t.size();  //used for testing
		//Loop to pop two nodes out of the queue.  First is left and second is right
		//Execute loop until there is only one node left in queue.
		while(t.size()>1){   //one poll per loop until we get to 1 node
			Tree left = t.poll();  //The first node to merge.  Left has a string value of "0"
			Tree right = t.poll();  //Pull the second node out of the queue
			Tree newNode = new Tree(left,right); //New Tree object for merging polled nodes
			t.add(newNode);  //Add new node to the end of the queue.
		}
		System.out.println("Huffman build with size: " + size + " and final size " + t.size());
		return t;
	}

	/**
	 * Counts the number of occurences per 8 bit character and stores in symbolFrequences hashmap
	 * @author kelle1ds
	 * @param list ArrayList of Strings.
	 * @return void.
	 */
	public void countFrequencies(ArrayList<String> list)
	{


		symbolFrequencies = new HashMap<>();

		/**
		 * Loop through the arrayList counting frequencies for each symbol
		 */
		for (String sub : list) {
			Double j = symbolFrequencies.get(sub);
			symbolFrequencies.put(sub, (j == null) ? 1 : j + 1); //Used for frequency counts

			//Used the following for percentage instead of frequency
			if(j == null){
				symbolFrequencies.put(sub,1.0/ list.size());
			} else {
				symbolFrequencies.put(sub, (j+1.0)/ list.size());
			}
		}
	}

	//  Prints out each symbol with its code

	public void printSymbolCodeMap() {
		System.out.println("Symbol Table is: ");
		for(Map.Entry<String,String> e: symbolCodeMap.entrySet()){
			System.out.println("Binary value: "+e.getKey() + " & Code is: " + e.getValue());
		}
	}



	///////CODE MAP CREATION//////////////
	/**
	 * Method that builds the Symbol Code Map
	 * @author kelle1ds
	 * @param Tree root
	 * @return void.
	 */
	private void BuildLookupTable(Tree root){
		symbolCodeMap = new HashMap<>();
		BuildLookUpTableImpl(root, "");
	}

	/**
	 * Helper recursive method for BuildLookupTable() method.
	 * This is a recursive method that walks through the Huffman tree.
	 * @author kelle1ds
	 * @param Tree node, String s
	 * @return void.
	 */
	private void BuildLookUpTableImpl(Tree node, String s){
		if(!node.isLeaf()){
			BuildLookUpTableImpl(node.getLeft(), s + 'O');
			BuildLookUpTableImpl(node.getRight(), s + '1');
		} else {
			symbolCodeMap.put(node.getValue(),s);
		}
	}

	/**
	 * This method compresses the String 'filecontents'
	 * Method uses the Symbol Code Map to compress the contents of the string
	 * @author kelle1ds
	 * @param String fileContents
	 * @return void.
	 */
	public String compressFileContents(String fileContents) {

		ArrayList<String> list = new ArrayList<>();
		int length = fileContents.length();
		int reminder = length % SYMBOL_LENGTH;  //just in case input is not / 8

		String compressed ="";

		for(int i = 0; i < fileContents.length()-reminder; i = i + SYMBOL_LENGTH){
			String number = fileContents.substring(i,i+SYMBOL_LENGTH);
			compressed = compressed + symbolCodeMap.get(number);
		}
		return compressed;
	}

	/**
	 * This method calculates the expected lenght of each symbol
	 * Method uses the Symbol Code Map to compress the contents of the string
	 * @author kelle1ds
	 * @param None
	 * @return double.
	 */
	public double expectedCodeLengthPerSymbol() {
		Double sum = 0.0;
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			sum = sum + e.getValue();
		}

		return sum/symbolFrequencies.size();
	}

}

/**
 * Tree Class.  Class that defines the tree node.
 * @author kelle1ds
 */
class Tree implements Comparable<Tree>{
	private Double frequency;  //Frequency count
	private String symbol;  //"0" or "1"
	private String value;  //original binary value
	private Tree left;
	private Tree right;

	/**
	 * Constructor for tree nodes in first priority queue
	 * @author kelle1ds
	 * @param String c, Double v
	 * @return void.
	 */
	public Tree(String c, Double v){
		this.frequency = v;
		this.value = c;
		this.symbol = "";
		left = null;
		right = null;
	}
	/**
	 * Constructor for tree nodes in first priority queue
	 * @author kelle1ds
	 * @param Tree leftSide, Tree rightSide
	 * @return void.
	 */
	public Tree(Tree leftSide, Tree rightSide){
		this.symbol = "";
		this.frequency = rightSide.getFrequency() + leftSide.getFrequency();
		left = leftSide;
		right = rightSide;
	}

	/**
	 * Method used to detect if a Tree node is a leaf.
	 * A binary tree node's children will be null
	 * @author kelle1ds
	 * @param String c, Double v
	 * @return void.
	 */
	boolean isLeaf(){
		return this.left == null && this.right == null;
	}

	/**
	 * gets the frequency value of a Tree node
	 * @author kelle1ds
	 * @param none
	 * @return Double.
	 */
	public Double getFrequency() {
		return frequency;
	}

	/**
	 * Checks to see if two Tree nodes have the same frequency
	 * @author kelle1ds
	 * @param Tree other
	 * @return boolean.
	 */
	public boolean equals(Tree other){
		return this.getFrequency() == other.getFrequency();
	}

	/**
	 * gets the left tree node of a parent
	 * @author kelle1ds
	 * @param none
	 * @return Tree.
	 */
	public Tree getLeft() {
		return left;
	}

	/**
	 * gets the right tree node of a parent
	 * @author kelle1ds
	 * @param none
	 * @return Tree.
	 */
	public Tree getRight() {
		return right;
	}

	/**
	 * gets the original binary string value for a tree nod
	 * @author kelle1ds
	 * @param none
	 * @return String.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Comparator used in Priority Queue
	 * @author kelle1ds
	 * @param Tree other
	 * @return int.
	 */
	@Override
	public int compareTo(Tree other){
		if(this.equals(other)){
			return 0;
		}
		else if(getFrequency() > other.frequency){
			return 1;
		}
		else {
			return -1;
		}
	}

}

