package view;

import java.util.ArrayList;
import java.util.List;

/**
 * MyQueue is a class that implements a priority queue.
 * 
 * @author David Lee & Mohamed Dahir
 * @version Fall 2015
 * 
 */
public class MyQueue<Type> {

	/**
	 * Size of priority queue.
	 */
	private int mySize;

	/**
	 * List of HuffManNodes.
	 */
	public List<HuffManNode> list;

	/**
	 * Constructs a MyQueue priority queue.
	 */
	public MyQueue() {
		list = new ArrayList<HuffManNode>();
		mySize = 0;
	}

	/**
	 * Checks if MyQueue is empty.
	 * 
	 * @return boolean is empty.
	 */
	public boolean isEmpty(){
		return (mySize == 0);
	}

	/**
	 * Adds object to the MyQueue with priority.
	 * @param theObject huffman node.
	 */
	public void offer(HuffManNode theObject) {
		list.add(theObject);
		if(list.size()>1){

			addHeapIfy(list.size()-1);
		}
		mySize++;
	}
	private void addHeapIfy(double index) {

		if (index >=1 &&list.get((int) index).compareTo(list.get((int) parent(index)))<0 ){
			swap(list.get((int) index),list.get((int) parent(index)));
			addHeapIfy(parent(index));
		}
	}
	private void minHeapIfy(double index) {
		

		if (isLeaf(index)) {
			if(!(rightChild(index)<mySize)&&leftChild(index)<mySize){
				if(list.get((int) leftChild(index)).compareTo(list.get((int) parent(leftChild(index))))< 0){
					swap(list.get((int) leftChild(index)), list.get((int) index));
					minHeapIfy(leftChild(index));
				} 
			}
			else if(list.get((int) leftChild(index)).compareTo(list.get((int) rightChild(index)))<=0){
				if(list.get((int) leftChild(index)).compareTo(list.get((int) parent(leftChild(index))))< 0){
					swap(list.get((int) leftChild(index)), list.get((int) index));
					minHeapIfy(leftChild(index));
				} 
			}
			else if(list.get((int) rightChild(index)).compareTo(list.get((int) leftChild(index)))<=0){
				if(list.get((int) rightChild(index)).compareTo(list.get((int) parent(rightChild(index))))< 0){
					swap(list.get((int) rightChild(index)), list.get((int) index));
					minHeapIfy(rightChild(index));
				} 

			}
		}
	}
	private boolean isLeaf(double index){
		
		if(leftChild(index) < mySize){
			return true;
		}
		return false;
	}
	private double parent(double pos){
		return (pos-1) / 2;
	}
	private double rightChild(double pos){
		return ((2 * pos) + 2);
	}
	private double leftChild(double pos){
		return ((2 * pos)+1);
	}
	private void swap(HuffManNode child, HuffManNode parent){
		int indexOfParent = list.indexOf(parent);
		int IndexOfChild = list.indexOf(child);
		list.set(IndexOfChild, parent);
		list.set(indexOfParent, child);
	}
	/**
	 * Removes object from this MyQueue with priority.
	 * @return HuffManNode node object.
	 */
	public HuffManNode poll() {
		HuffManNode newNode = list.get(0);

		swap(list.get(0),list.get(list.size()-1));
		list.remove(list.size()-1);
		mySize--;
		
		minHeapIfy(0);


		return newNode;
	}

	/**
	 * Gets the size of the MyQueue.
	 * @return int length/size.
	 */
	public int size(){
		return mySize;
	}
	public String toString(){
		return list.toString();

	}

}
