/**
 * Program #3 BinaryHeapPriorityQueue
 * This is the Binary Heap implementation of a priority queue with a shell sort for the array
 * CS310-01
 * 10-Apr-2019
 * @author Kyle McLain cssc 1497
 */

package data_structures;

import java.sql.Wrapper;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue <E extends Comparable <E>> implements PriorityQueue<E> {
	
	private Wrapper<E>[] list;
	private int currentSize, maxCapacity, entryNumber;
	private long modificationCounter;
	
	/** A constructor to set the default max capacity in the system
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeapPriorityQueue(){
		list = new Wrapper[DEFAULT_MAX_CAPACITY];
		maxCapacity = DEFAULT_MAX_CAPACITY;
		currentSize = maxCapacity = entryNumber = 0;
		modificationCounter = 0;
	}
	
	/** A constructor take the size parameter
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeapPriorityQueue(int maxCapacity){
		list = new Wrapper[maxCapacity];
		this.maxCapacity = maxCapacity;
		currentSize = maxCapacity = entryNumber = 0;
		modificationCounter = 0;
	}
	
	/** Inserts a new object into the priority queue. Returns true if the insertion is successful.
	 *  If the PQ is full, the insertion is aborted, and the method returns false.
	 * @param object
	 * @return boolean
	 */
	@Override
	public boolean insert(E object) {
		
		if(isFull()) {					//Checking if its full
			return false;
		}		
		
		Wrapper<E> wrapped = new Wrapper(object);	//Using a wrapper obj to store the insertion order
		list[currentSize++] = wrapped;
		trickleUp(currentSize - 1);
		
		modificationCounter++;
		return true;
		
	}
	
	/** Removes the object of highest priority that has been in the PQ the longest, and returns it. 
	 * Returns null if the PQ is empty.
	 * @param object
	 */
	@Override
	public E remove() {
		
		if(isEmpty()) {					//Checking if its empty
			return null;
		}

		E toReturn = list[0].data;		//Object to return is always at ele. 0 in the remove
		trickleDown(0);
		
		currentSize--;
		modificationCounter++;
		return toReturn;
	}
	
	/** Deletes all instances of the parameter obj from the PQ if found, and returns true. 
	 * Returns false if no match to the parameter obj is found.
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean delete(E obj) {

		boolean found = false;			//Set to default: false
		
		if(isEmpty()) {					//Checking if its empty
			return false;
		}
		
		for(int i = 0; i < currentSize; i++) {			
			if(list[i].data.compareTo(obj) == 0) {
				found = true; 			//Found the object in the array
				
				if(i == 0) {			//So we do not go out of bounds at i
					remove();
				}
				
				else {	
					int parent = (i - 1) >> 1;			//Parent equation
					if(list[currentSize - 1].data.compareTo(list[parent].data) < 0) {	//Comparing the parents for trickleup
						list[parent] = list[currentSize - 1];
						trickleUp(i);
						
					}else{
						trickleDown(i);						
					}
					currentSize--;
					modificationCounter++;
				}
				i--;				
			}
		}
		return found;
	}
	
	/** Returns the object of highest priority that has been in the PQ the longest, 
	 * but does NOT remove it. Returns null if the PQ is empty.	
	 * @return object
	 */
	@Override
	public E peek() {	
		if(isEmpty()) {
			return null;
		}else {
			return list[0].data;		//Highest priority always at 0
		}		
	}

	/** Returns true if the priority queue contains the specified element false otherwise.
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean contains(E obj) {
		if(isEmpty()) {
			return false;
		}
		
		for(int i = 0; i < currentSize; i++) {
			if(list[i].data.compareTo(obj) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/** Returns the number of objects currently in the PQ.
	 * @return int
	 */
	@Override
	public int size() {
		return currentSize;
	}

	/** Returns the PQ to an empty state.
	 */
	@Override
	public void clear() {
		currentSize = 0;
		modificationCounter++;
	}
	
	/** Returns true if the PQ is empty, otherwise false
	 * @return boolean
	 */
	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	/** Returns true if the PQ is full, otherwise false. List based implementations should always return false.
	 * @return boolean
	 */
	@Override
	public boolean isFull() {
		return currentSize == maxCapacity;
	}
	
	/** This is the trickle up sorting where it compares the parent with the child
	 * and it determines whether or not a swap is needed. (Sorts upward)
	 * @param index
	 */
	private void trickleUp(int index) {
		int newIndex = index;
		int parentIndex = (newIndex-1) >> 1;
		
		Wrapper<E> newValue = list[newIndex];
		while(parentIndex >= 0 && newValue.compareTo(list[parentIndex]) < 0) {
			list[newIndex] = list[parentIndex];
			newIndex = parentIndex;
			parentIndex = (parentIndex-1) >> 1;
		}
		
		list[newIndex] = newValue;
		
	}
	
	/** This is the trickle down sorting where it compares the parent with the child
	 * and it determines whether or not a swap is needed. (Sorts downward)
	 * @param index
	 */
	private void trickleDown(int index) {
		int current = index;
		int child = getNextChild(current);
	
		while(child != -1 && list[current].compareTo(list[child]) < 0 && list[child].compareTo(list[currentSize-1]) < 0){			
			list[current] = list[child];
			current = child;
			child = getNextChild(current);		
		}
		
		list[current] = list[currentSize - 1];
		
	}
	
	/** This method looks for the child for the parent at the current index
	 * @param current
	 * @return int
	 */
	private int getNextChild(int current) {
		int left = (current << 1) + 1;
		int right = left + 1;
		
		if(right < currentSize) {
			if(list[left].compareTo(list[right]) < 0) 
				return left;
			return right;	
		}
		
		if(left < currentSize)
			return left;
		return -1;
	}
	
	/** This is the iterator for the function and in is constructor is utilizes the shell sort to sort
	 * the heap sorted array into an ordered array 
	 * @return Iterator<E>
	 */
	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
	class IteratorHelper implements Iterator<E>{
		int iterIndex;
		long stateCheck;
		Wrapper<E>[] sortedArray;
		Wrapper<E> temp;
		
		public IteratorHelper() {
			iterIndex = 0;
			stateCheck = modificationCounter;	
			sortedArray = list.clone();
			
			//Shell Sort
			int h = 1;
			int size = currentSize;
			
			while(h <= size/3) 
				h = h * 3 + 1;
			
			while(h > 0) {
				
				for(int out = h; out < size; out++) {
					temp = sortedArray[out];
					int in = out;
					
					while(in > h-1 && sortedArray[in-h].compareTo(temp) >= 0) {
						sortedArray[in] = sortedArray[in-h];
						in -= h;
					}
					
					sortedArray[in] = temp;
					
				}
				h = (h-1)/3;
			}
		}
		
		public boolean hasNext() {
			if(stateCheck != modificationCounter) {
				throw new ConcurrentModificationException();
			}
			return (iterIndex < currentSize);
		}
		
		public E next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}		
			return sortedArray[iterIndex++].data;		//Returning the array post shell sort
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}		
	}
	
	/** This is the wrapper class that helps to store and compare the entryNumber(a.k.a. also the order it was entered
	 * into the array
	 */
	protected class Wrapper<E> implements Comparable<Wrapper<E>>{
		long number;
		E data;
		
		public Wrapper(E d) {		//Storing when the number was inserted into the array
			number = entryNumber++;
			data = d;
		}
		public int compareTo(Wrapper<E> o) {	//Comparing the two numbers and their entryNumbers
			if(((Comparable<E>)data).compareTo(o.data) == 0) {
				return (int) (number - o.number);
			}
			return ((Comparable<E>)data).compareTo(o.data);
		}
	}
		
}
