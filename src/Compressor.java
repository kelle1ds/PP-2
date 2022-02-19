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
		//PriorityQueue<Map.Entry<String,Double>> queue = new PriorityQueue<Map.Entry<String,Double>>((a, b)-> {return Double.compare(a.getValue(), b.getValue());});

		//PriorityQueue which will serve as the Huffman code
		PriorityQueue<Tree> treeTree = new PriorityQueue<>();

		//Loop that creates a tree node for the treeTree PQ and also loads each PQ
		//Loop through symbolFrequencies Hashmap to populate queue and treeTree
		for(Map.Entry<String,Double> e: symbolFrequencies.entrySet()){
			Tree tree = new Tree(e.getKey(),e.getValue());  //Node placed in Priority Queue
			//queue.add(e);  //Priority queue used to create symbol table
			treeTree.add(tree);  //Priority queue used for compression
		}

		// Build code tree
		// This probably doesn't work like he wants it.  Or maybe it does
		//symbolTable(queue);


		//Build the Huffman code tree
		PriorityQueue<Tree> freeTree = buildHuffman(treeTree);
		BuildLookupTable(freeTree.peek());
		System.out.println("symboltable size " + symbolCodeMap.size() + " freetree size " + freeTree.size());


		//PriorityQueue<Tree> bt = BFS(freeTree);


		//System.out.println("BT size is " + bt.size());
		//for(int i = 0; i < bt.size(); i++){
		//	System.out.println(bt.poll().getSymbol());
		//}

		//System.out.println("Expected value is " + expectedCodeLengthPerSymbol());
		// Create encoding map
	}



	public PriorityQueue<Tree> BFS(PriorityQueue<Tree> x){
		PriorityQueue<Tree> bfs = new PriorityQueue<Tree>();
		bfs.add(x.poll());
		//System.out.println("bfs is size " + bfs.size());
		int loops = 0;
		while (!bfs.isEmpty()) {
			loops++;
			Tree tempNode = bfs.poll();

			//*Enqueue left child *//*
			if (tempNode.getLeft() != null) {
				//tempNode.getLeft().setSymbol("0");
				System.out.println("getleft " + tempNode.getLeft().getSymbol());
				bfs.add(tempNode.getLeft());
			}

			//*Enqueue right child *//*
			if (tempNode.getRight() != null) {
				//tempNode.getRight().setSymbol("1");
				bfs.add(tempNode.getRight());
				System.out.println("getRight " + tempNode.getRight().getSymbol());
			}
			System.out.println("bfs is size " + bfs.size() + " loops " + loops);

			//if (tempNode.getRight() == null) {
			//	System.out.println("tempNode is null");
			//}
		}
		return bfs;
	}


	public PriorityQueue<Tree> buildHuffman(PriorityQueue<Tree> t){

		int size = t.size();
		//Loop to pop two nodes out of the queue.  First is left and second is right
		//Execute loop until there is only one node left in queue.
		while(t.size()>1){   //one poll per loop until we get to 1 node
			Tree left = t.poll();  //The first node to merge.  Left has a string value of "0"
			//left.setSymbol("0");  //set string value

			//Just in case there is an odd number of nodes.  The "if" will only be executed
			//on the last node of the queue.
			if(t.isEmpty()){
				System.out.println("left " + left.getFrequency() + " right is null");
				Tree newNode = new Tree(left.getFrequency());
				newNode.setLeft(newNode);
				newNode.setSymbol(left.getSymbol());
				t.add(newNode);
			} else {    //For an even number of nodes with a left and a right merge to form a new node
				Tree right = t.poll();  //Pull the second node out of the queue
				Tree newNode = new Tree(left,right); //New Tree object for merging polled nodes
				//System.out.println("left symbol is " + left.getSymbol());
				//System.out.println("right symbol is " + right.getSymbol());

				t.add(newNode);  //Add new node to the end of the queue.
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

	//Two methods to create the lookup table
	//First method calls the second method which uses recursion
	private void BuildLookupTable(Tree root){
		//final Map<String,String> lookupTable = new HashMap<>();
		System.out.println("BuildLookupTable root" + root.getValue());
		symbolCodeMap = new HashMap<>();
		BuildLookUpTableImpl(root, "");
	}

	private void BuildLookUpTableImpl(final Tree node, final String s){
		if(!node.isLeaf()){
			BuildLookUpTableImpl(node.getLeft(), s + 'O');
			BuildLookUpTableImpl(node.getRight(), s + '1');
		} else {

			symbolCodeMap.put(node.getValue(),s);
		}
	}


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
			System.out.println("Binary is " + x.getSymbol() + " Frequency value is " + x.getFrequency());
		}
	}

}

