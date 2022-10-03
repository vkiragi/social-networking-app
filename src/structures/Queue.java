package structures;

import java.util.NoSuchElementException;

/**
 * Queue.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */
public class Queue<T extends Comparable<T>> {
	private class Node {
		private final T data;
		private Node next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}
	}

	private int length;
	private Node front;
	private Node end;

	/**** CONSTRUCTOR ****/

	/**
	 * Instantiates a new Queue with default values
	 * 
	 * @postcondition creates a new instance of a Queue
	 */
	public Queue() {
		front = null;
		end = null;
		length = 0;
	}

	/**
	 * Instantiates a new Queue by copying another Queue
	 * 
	 * @param original the Queue to make a copy of
	 * @postcondition a new Queue object, which is an identical but separate copy of
	 *                the Queue original
	 */
	public Queue(Queue<T> original) {
		if (original == null) {
			return;
		}
		if (original.getLength() == 0) {
			length = 0;
			front = null;
			end = null;
		} else {
			Node temp = original.front;
			while (temp != null) {
				enqueue(temp.data);
				temp = temp.next;
			}
		}
	}

	/**** ACCESSORS ****/

	/**
	 * Returns the value stored at the front of the Queue
	 * 
	 * @precondition !isEmpty()
	 * @return the value stored at the front of the Queue
	 * @throws NoSuchElementException when precondition is violated
	 */
	public T getFront() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("getFront: Queue is Empty. No data to access!");
		}
		return front.data;
	}

	/**
	 * Returns the current length of the Queue
	 * 
	 * @return the length of the Queue
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns whether the Queue is currently empty
	 * 
	 * @return whether the Queue is empty
	 */
	public boolean isEmpty() {
		return (length == 0);
	}

	/**
	 * Determines whether data is sorted in ascending order by calling its recursive
	 * helper method isSorted() Note: when length == 0 data is (trivially) sorted
	 * 
	 * @return whether the data is sorted
	 */
	public boolean isSorted() {
		if (isEmpty()) {
			return true;
		} else {
			return isSorted(front);
		}
	}

	/**
	 * Helper method to isSorted Recursively determines whether data is sorted
	 * 
	 * @param node the current node
	 * @return whether the data is sorted
	 */
	private boolean isSorted(Node node) {
		if (node.next == null) {
			return true;
		} else if (node.data.compareTo(node.next.data) > 0) {
			return false;
		} else {
			return isSorted(node.next);
		}
	}

	/**
	 * Uses the iterative linear search algorithm to locate a specific element and
	 * return its position
	 * 
	 * @param element the value to search for
	 * @return the location of value from 1 to length Note that in the case
	 *         length==0 the element is considered not found
	 */
	public int linearSearch(T element) {
		if (isEmpty()) {
			return -1;
		}
		Node temp = front;
		int index = 1;
		while (temp.next != null) {
			if (temp.data.compareTo(element) == 0) {
				return index;
			}
			temp = temp.next;
			index++;
		}
		return -1;
	}

	/**
	 * Returns the location from 1 to length where value is located by calling the
	 * private helper method binarySearch
	 * 
	 * @param value the value to search for
	 * @return the location where value is stored from 1 to length, or -1 to
	 *         indicate not found
	 * @precondition isSorted()
	 * @throws IllegalStateException when the precondition is violated.
	 */
	public int binarySearch(T value) throws IllegalStateException {
		if (!isSorted()) {
			throw new IllegalStateException("binarySearch: cannot perform function on unsorted queue");
		} else if (isEmpty()) {
			return -1;
		} else {
			return binarySearch(0, length - 1, value);
		}
	}

	/**
	 * Searches for the specified value in by implementing the recursive
	 * binarySearch algorithm
	 * 
	 * @param low   the lowest bounds of the search
	 * @param high  the highest bounds of the search
	 * @param value the value to search for
	 * @return the location at which value is located from 1 to length or -1 to
	 *         indicate not found
	 */
	private int binarySearch(int low, int high, T value) {
		if (low > high) {
			return -1;
		}
		int mid = low + (high - low) / 2;
		Node iterator = front;
		for (int i = 0; i < mid; i++) {
			iterator = iterator.next;
		}
		if (iterator.data.compareTo(value) == 0) {
			return mid + 1;
		} else if (iterator.data.compareTo(value) < 0) {
			low = mid + 1;
		} else {
			high = mid - 1;
		}
		return binarySearch(low, high, value);
	}

	/**** MUTATORS ****/

	/**
	 * Creates a new end element
	 * 
	 * @param data the data to insert at the end of the Queue
	 * @postcondition a new end node is created
	 */
	public void enqueue(T data) {
		Node node = new Node(data);
		if (front == null) {
			front = end = node;
		} else {
			end.next = node;
			end = node;
		}
		length++;
	}

	/**
	 * removes the element at the front of the Queue
	 * 
	 * @precondition !isEmpty()
	 * @postcondition front element is removed from the front of the Queue
	 * @throws NoSuchElementException when precondition is violated
	 */
	public void dequeue() throws NoSuchElementException {
		if (isEmpty()) {
			throw new NoSuchElementException("dequeue(): Cannot remove from an empty queue!");
		} else if (length == 1) {
			front = end = null;
		} else {
			front = front.next;
		}
		length--;
	}

	/**** ADDITIONAL OPERATIONS ****/

	/**
	 * Queue with each value separated by spaces At the end of the Queue a new line
	 * 
	 * @return the Queue as a String for display
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Node temp = front;
		while (temp != null) {
			result.append(temp.data).append(" ");
			temp = temp.next;
		}
		return (result.append("\n").toString());
	}

	/**
	 * Determines whether two Queues have the same data in the same order
	 * 
	 * @param o the Queue to compare to this Queue
	 * @return whether the two Queues are equal
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Queue)) {
			return false;
		} else {
			Queue<T> q = (Queue<T>) o;
			if (this.length != q.length) {
				return false;
			} else {
				Node temp1 = this.front;
				Node temp2 = q.front;
				while (temp1 != null) { // Queues are same length
					if (temp1.data.compareTo(temp2.data) != 0) {
						return false;
					}
					temp1 = temp1.next;
					temp2 = temp2.next;
				}
				return true;
			}
		}
	}

	/**
	 * Prints in reverse order to the console, followed by a new line by calling the
	 * recursive helper method printReverse
	 */
	public void printReverse() {
		printReverse(front);
		System.out.println();
	}

	/**
	 * Recursively prints to the console the data in reverse order (no loops)
	 * 
	 * @param node the current node
	 */
	private void printReverse(Node node) {
		if (node != null) {
			printReverse(node.next);
			System.out.print(node.data + " ");
		}
	}
}
