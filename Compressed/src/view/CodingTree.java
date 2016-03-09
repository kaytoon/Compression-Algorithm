package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * CodingTree is a class that represents a huffman tree of characters
 * that can encode characters from a string into smaller sized bit 
 * representation.
 * 
 * @author David Lee & Mohamed Dahir
 * @version Fall 2015
 */
public class CodingTree {
	private static Map<String, String> myMap;
	/**Stringbuilder used to build strings. */
	public StringBuilder builder;
	public static int mode;
	/**Map of characters and their frequency count. */
	public Map<Character, Integer> frequencies;

	/**Huffman tree node. */
	public HuffManNode huffNode;

	/**List of characters. */
	public ArrayList<Character> charList;

	/**Map of characters and their encoded bit values. */
	public Map<Character, String> codes;

	/**List of bytes. */
	public List<Byte> bits;

	private Character[] array;

	/**
	 * Constructs a CodingTree that gets frequency of 
	 * character occurrences, huffman tree initialization,
	 * reads codes from huffman tree, and encodes a message.
	 * 
	 * @param message String of a text file to be encoded.
	 * @throws IOException
	 */
	public CodingTree(String message) throws IOException {
		
		builder = new StringBuilder();
		charList = new ArrayList<Character>();
		frequencies = new HashMap<Character, Integer>();
		codes = new HashMap<Character, String>();
		bits = new ArrayList<Byte>();
		getCharacterFrequencies(message);
		huffNode = getCodes();
		assignBinary(huffNode);
		encode(message);
		decode();
	}

	/**
	 * Gets the frequency of character occurrences and saves to map.
	 * 
	 * @param Message String of a text file to be encoded.
	 * @throws IOException
	 */
	private void getCharacterFrequencies(String Message) throws IOException{
		
		int freq = 1;
		char[] messageArray = Message.toCharArray();
		for (int i = 0; i < messageArray.length; i++) {
			char character = messageArray[i];
			charList.add(character);
		}
		for (int i = 0; i < charList.size(); i++) {
			if (frequencies.containsKey(charList.get(i))) {
				freq = frequencies.get(charList.get(i)) + 1;
			}
			frequencies.put(charList.get(i), freq);
			freq = 1;
		}

	}
	/**
	 * Huffman Tree initialization and binary encoding for character keys.
	 * 
	 * @return HuffManNode root node of the huffman tree.
	 */
	private HuffManNode getCodes() {
		//Parallel array lists of characters and frequencies 
		array = new Character[frequencies.size()];
		frequencies.keySet().toArray(array);
//				ArrayList<Character> keys = new ArrayList<Character>(frequencies.keySet());
//				ArrayList<Integer> values = new ArrayList<Integer>(frequencies.values());
		MyQueue<HuffManNode> trees = new MyQueue<HuffManNode>();
		//PriorityQueue<HuffManNode> trees = new PriorityQueue<HuffManNode>();
		for (int i = 0; i < frequencies.size(); i++) {
			HuffManNode node = new HuffManNode(array[i], frequencies.get(array[i]), null, null);

			trees.offer(node);

		}
		while (trees.size() > 1) {
			// two trees with least frequency
			HuffManNode a = trees.poll();
			HuffManNode b = trees.poll();
			// create into new node and re-insert into the priority queue
			HuffManNode huff = new HuffManNode(a.myFreq + b.myFreq,
					a, b);
			trees.offer(huff);
		}
		
		HuffManNode node = trees.poll();
		
		return node;
	}

	/**
	 * Assigns binary bit values for each character from huffman tree.
	 * 
	 * @param trees Huffman Tree
	 */
	private void assignBinary(HuffManNode trees){
		if(trees != null){
			if (trees.myLeft == null) {
				codes.put(trees.myData, builder.toString());
			} else {
				builder.append('0');
				assignBinary(trees.myLeft);
				builder.deleteCharAt(builder.length()-1);
				
				builder.append('1');
				assignBinary(trees.myRight);
				builder.deleteCharAt(builder.length()-1);
			}
		}	
		
	}
	
	/**
	 * Encodes a text file using codes from huffman tree
	 * into list of bytes.
	 * 
	 * @param message String of a text file to be encoded.
	 * @throws FileNotFoundException 
	 */
	private void encode(String message) throws FileNotFoundException {
		
		char[] messageArray = message.toCharArray();
		
		for (int i = 0; i < messageArray.length; i++) {
			String s = codes.get(messageArray[i]);
			builder.append(s);
		}
	
		
		String binary = builder.toString();
		char[] array = binary.toCharArray();
		 mode = array.length % 8;
		String emptyString = "";
		for (int i = 0; i < array.length; i++) {

			if(i>=array.length-mode){
				if (emptyString.length() < 8) {
					emptyString = emptyString + array[i];

				} else {
					Byte b = (byte) Integer.parseInt(emptyString,2);
					bits.add(b);
					emptyString = "";
					emptyString = emptyString + array[i];
				}
			}
			else {
				if (emptyString.length() < 8 ) {
					emptyString = emptyString + array[i];

				} else {
					Byte b = (byte) Integer.parseInt(emptyString,2);	
					bits.add(b);
					emptyString = "";
					emptyString = emptyString + array[i];
				}
			}
		}

		Byte b = (byte) Integer.parseInt(emptyString,2);
		bits.add(b);
	}
	private static void decode() throws IOException{
		 FileWriter writer = new FileWriter("original.txt");
		myMap = new HashMap<String, String>();
		FileReader codes = new FileReader("codes.txt");
		BufferedReader codesReader = new BufferedReader(codes);
		FileInputStream compressedReader = new FileInputStream("compressed.txt");
		StringBuilder codesBuilder = new StringBuilder();
		StringBuilder compressedBuilder = new StringBuilder();
		StringBuilder originalText = new StringBuilder();
		int ch;
		while((ch = codesReader.read()) !=-1) {
			char chara = (char)ch;
			codesBuilder.append(chara);
		}
		String st = ""+codesBuilder;
		ArrayList<String> newString = new ArrayList<String>();
		String[] s = st.split(", ");
		for(int l = 0; l<s.length;l++){
			  newString.add(s[l]);
		}
		String c = "";
		for(int l = 0; l<newString.size();l++ ){
			c = newString.get(l);
			int k =c.indexOf("=");
			if(c.contains("{")){	
				myMap.put(c.substring(k+1, c.length()),c.substring(1,k));
			} else if(c.contains("==")){
				myMap.put(c.substring(k+2, c.length()),c.substring(0,k+1));
			}
			else if(c.contains("}")){	
				myMap.put(c.substring(k+1, c.length()-1),c.substring(0,k));
			}
			else if(k>0){
				String[] parts= c.split("=", 2);
				myMap.put(parts[1] ,parts[0]);
			}

		}
		Byte text;
		while(compressedReader.available() >0) {
			text =   (byte) compressedReader.read();
			String string = Integer.toBinaryString(text);
			
			if(string.length() > 8){
				string = string.substring(string.length()-8, string.length());
			}
			if(compressedReader.available() > 0){
				for(int i = string.length(); i < 8;i++){
					string = "0"+string;
				}
				
			} else {
				for(int i = string.length(); i <mode;i++){
					string = "0"+string;
				}
			}
			compressedBuilder.append(string);
		}
		String w = "";
		for( int i = 0; i < compressedBuilder.length();i++){
			String k = ""+compressedBuilder.charAt(i);
			w = w+k;
			if(myMap.containsKey(w)){
				String value = myMap.get(w);
				w = "";
				writer.write(value);
			}

		}
		
		writer.flush();
		writer.close();
	}
	
}
