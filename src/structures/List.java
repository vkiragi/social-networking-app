package structures;

/**
 * List.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */

import java.util.NoSuchElementException;
import java.util.Comparator;

public class List<T> {

    private class Node {

        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**
     * **CONSTRUCTOR***
     */
    /**
     * Instantiates a new List with default values
     *
     * @postcondition new list created
     */
    public List() {
        first = null;
        last = null;
        length = 0;
        iterator = null;

    }

    /**
     * Instantiates a new list by copying another list
     *
     * @param original the list to make copy of
     * @postcondition new list object, which is an identical but separate copy
     * of the List original
     */
    public List(List<T> original) {
        if (original == null) {
            return;
        }
        if (original.length == 0) {
            length = 0;
            first = null;
            last = null;
            iterator = null;
        } else {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
            iterator = null;
        }
    }

    /**
     * **ACCESSORS***
     */
    /**
     * Returns the value stored in the first node
     *
     * @precondition length != 0
     * @return the value stored at node first
     * @throws NoSuchElementException when precondition is violated
     */
    public T getFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getFirst(): List is empty.");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     *
     * @precondition length != 0
     * @return the value stored in the node last
     * @throws NoSuchElementException when precondition is violated
     */
    public T getLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("getLast(): List is empty.");
        }
        return last.data;
    }

    /**
     * Returns whether the list is currently empty
     *
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        if (length == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the current length of the list
     *
     * @return the length of the list from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns element currently pointed at by iterator
     *
     * @precondition iterator != null
     * @return value pointed at by iterator
     * @throws NullPointerException when precondition is violated
     */
    public T getIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("getIterator(): Iterator is null, can't get value.");
        }
        return iterator.data;
    }

    /**
     * Returns whether or not the iterator is off end of the list
     *
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**
     * Determines whether two lists have same data in same order
     *
     * @param L the list to compare this list
     * @return whether two lists are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof List)) {
            return false;
        } else {
            List<T> L = (List<T>) o; //cast
            if (this.length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) {
                    if (temp1.data != temp2.data) {
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
     * **MUTATORS***
     */
    /**
     * Creates a new first element
     *
     * @param data the data to insert at the front of the list
     * @postcondition a new first element
     */
    public void addFirst(T data) {
        if (first == null) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            N.next = first;
            first.prev = N;
            first = N;
        }
        length++;
    }

    /**
     * Creates a new last element
     *
     * @param data the data to insert at the end of the list
     * @postcondition a new last element
     */
    public void addLast(T data) {
        if (length == 0) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            Node temp = last;
            last.next = N;
            last = N;
            N.prev = temp;
        }
        length++;
    }

    /**
     * removes the element at the front of the list
     *
     * @precondition length != 0
     * @postcondition first element in the list is removed
     * @throws NoSuchElementException when precondition is violated
     */
    public void removeFirst() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("removeFirst(): Can't remove element because the list is empty");
        } else if (length == 1) {
            first = last = iterator = null;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
        }
        length--;
    }

    /**
     * removes the element at the end of the list
     *
     * @precondition length!= 0
     * @postcondition last element in the list is removed
     * @throws NoSuchElementException when precondition is violated
     */
    public void removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("removeLast(): Can't remove element because the list is empty.");
        } else if (length == 1) {
            first = last = iterator = null;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            Node temp = last.prev;
            temp.next = null;
            last = temp;
            //last.next = null;
        }
        length--;
    }

    /**
     * Moves iterator to beginning of list
     *
     * @postcondition iterator is at start of list
     */
    public void placeIterator() {
        iterator = first;
    }

    /**
     * Removes element pointed at by the iterator
     *
     * @precondition iterator != null
     * @throws NullPointerException when precondition is violated
     * @postcondition Iterator then points to null
     */
    public void removeIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("removeIterator(): Iterator is null, cannot remove.");
        } else if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.next.prev = iterator.prev;
            iterator.prev.next = iterator.next;
            iterator = null;
            length--;
        }
    }

    /**
     * Inserts new data in the list after the iterator
     *
     * @param data the new data to insert
     * @precondition iterator != null
     * @throws NullPointerException when precondition is violated
     */
    public void addIterator(T data) throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("addIterator(): Iterator is off end, cannot add.");
        } else if (iterator == last) {
            addLast(data);
        } else {
            Node n = new Node(data);
            n.next = iterator.next;
            n.prev = iterator;
            iterator.next.prev = n;
            iterator.next = n;
            length++;
        }
    }

    /**
     * Advances iterator by one node in the list
     *
     * @precondition iterator != null
     * @throws NullPointerException when precondition is violated
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator(): Iterator is null and cannot advance.");
        }
        iterator = iterator.next;
    }

    /**
     * Reverses iterator by one node in the list
     *
     * @precondition iterator != null
     * @throws NullPointerException when precondition is violated
     */
    public void reverseIterator() {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator(): Iterator is null and cannot reverse.");
        }
        iterator = iterator.prev;
    }

    /**
     * **ADDITIONAL OPERATIONS***
     */
    /**
     * List with each value on its own line At the end of the List a new line
     *
     * @return the List as a String for display
     */
    @Override
    public String toString() {
        String result = "";
        Node temp = first;
        while (temp != null) {
            result += temp.data + "\n\n";
            temp = temp.next;
        }
        return result;
    }

    /**
     * prints the contents of the linked list to the screen in the format #:
     * <element> followed by a newline
     *
     * @postcondition prints the list in numbered format
     */
    public void printNumberedList() {
        placeIterator();
        int i = 1;
        while (!offEnd()) {
            System.out.println(i + ": " + iterator.data.toString() + "\n");
            i++;
            advanceIterator();
        }
    }

    /**
     * Points the iterator at first and then advances it to the specified index
     *
     * @param index the index where the iterator should be placed
     * @precondition 0 < index <= length
    * @t
     * hrows IndexOutOfBoundsException when precondition is violated
     */
    public void iteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("iteratorToIndex(): index is out of bounds.");
        }
        placeIterator();
        int count = 0;
        while (count < index) {
            advanceIterator();
            count++;
        }
    }

    /**
     * Searches the List for the specified value using the linear search
     * algorithm
     *
     * @param value the value to search for
     * @return the location of value in the List or -1 to indicate not found
     * Note that if the List is empty we will consider the element to be not
     * found post: position of the iterator remains unchanged
     */
    public int linearSearch(T value, Comparator<T> c) {
        int index = 0;
        placeIterator();
        while (index <= length - 1) {
            if (c.compare(iterator.data, value) == 0) {
                return index;
            }
            index++;
            advanceIterator();
        }
        return -1;
    }

}
