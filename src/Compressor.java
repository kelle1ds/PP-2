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
		System.out.println("Original File length is " + fileContents.length());
		Compressor c = new Compressor(fileContents);
		c.printSymbolCodeMap();
		String compressedFileContents = c.compressFileContents(fileContents);
		System.out.println(compressedFileContents);
		System.out.println("Compressed File length is " + compressedFileContents.length());
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

		//PriorityQueue which will be passed to the buildHuffman() method
		PriorityQueue<Tree> treeTree = new PriorityQueue<>();

		//Loop that creates a tree node for the treeTree PQ and also loads each PQ
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			Tree tree = new Tree(e.getKey(),e.getValue());  //Node placed in Priority Queue
			treeTree.add(tree);  //Priority queue used for compression
		}

		//Build the Huffman code tree
		PriorityQueue<Tree> freeTree = buildHuffman(treeTree);
		BuildLookupTable(freeTree.peek());
	}

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
	//Two methods to create the lookup table
	//First method calls the second method which uses recursion
	private void BuildLookupTable(Tree root){
		symbolCodeMap = new HashMap<>();
		BuildLookUpTableImpl(root, "");
	}

	private void BuildLookUpTableImpl(Tree node, String s){
		if(!node.isLeaf()){
			BuildLookUpTableImpl(node.getLeft(), s + 'O');
			BuildLookUpTableImpl(node.getRight(), s + '1');
		} else {
			symbolCodeMap.put(node.getValue(),s);
		}
	}

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

	// Using the frequencies of the symbols and lengths of their associated
	//  codeword, calculate the expected code length per symbol in fileContents
	public double expectedCodeLengthPerSymbol() {
		Double sum = 0.0;
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			sum = sum + e.getValue();
		}

		return sum/symbolFrequencies.size();
	}

}

class Tree implements Comparable<Tree>{
	private Double frequency;  //Frequency count
	private String symbol;  //"0" or "1"
	private String value;  //original binary value
	private Tree left;
	private Tree right;

	//Constructor for tree nodes in first priority queue

	public Tree(String c, Double v){
		this.frequency = v;
		this.value = c;
		this.symbol = "";
		left = null;
		right = null;
	}

	public Tree(Tree leftSide, Tree rightSide){
		this.symbol = "";
		this.frequency = rightSide.getFrequency() + leftSide.getFrequency();
		left = leftSide;
		right = rightSide;
	}
/*
	public Tree(Double v){
		this.symbol = "";
		this.frequency = v;
		left = null;
		right = null;
	}
 */

	boolean isLeaf(){
		return this.left == null && this.right == null;
	}

	public Double getFrequency() {
		return frequency;
	}

	public String getCode() {
		return symbol;
	}

	public void setFrequency(Double value) {
		this.frequency = frequency;
	}

	public void setCode(String code) {
		this.symbol = code;
	}

	public boolean equals(Tree other){
		return this.getFrequency() == other.getFrequency();
	}

	public Tree getLeft() {
		return left;
	}

	public void setLeft(Tree left) {
		this.left = left;
	}

	public Tree getRight() {
		return right;
	}

	public void setRight(Tree right) {
		this.right = right;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String value) {
		this.symbol = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	//Comparator
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

