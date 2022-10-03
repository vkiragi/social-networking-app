package structures;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * HashTable.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */
public class HashTable<T> {
    
    private int numElements;
    private ArrayList<List<T> > Table;

    /**
     * Constructor for the hash 
     * table. Initializes the Table to
     * be sized according to value passed
     * in as a parameter
     * Inserts size empty Lists into the
     * table. Sets numElements to 0
     * @param size the table size
     */
    public HashTable(int size) {
        Table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
        	Table.add(new List<T>());
        }
        numElements = 0;
    }
       
    /**Accessors*/
    
    /**
     * returns the hash value in the Table
     * for a given Object 
     * @param key the string to hash to a bucket in the table
     * @return the index in the Table
     */
    private int hash(String key) {
    	int code = Math.abs(key.hashCode());
        return code % Table.size();
    }
    
    /**
     * counts the number of keys at this index
     * @param index the index in the Table
     * @precondition 0 <=  index < Table.length
     * @return the count of keys at this index
     * @throws IndexOutOfBoundsException
     */
    public int countBucket(int index) throws IndexOutOfBoundsException{
        if (index < 0 || index >= Table.size()) {
        	throw new IndexOutOfBoundsException("countBucket: index out of range");
        }
    	return Table.get(index).getLength();
    }
    
    /**
     * returns total number of keys in the Table
     * @return total number of keys
     */
    public int getNumElements() {
        return numElements;
    }
    
    /**
     * Accesses a specified key in the Table
     * 
     * @param key the String to hash to a bucket in the table
     * @param t the value to find
     * @param c Comparator object to use for comparisons
     * @return the value mapped at the specified key,
     * or null if the value does not exist in this table
     * @precondition key != null
	 * @throws NullPointerException if the precondition is violated     
	 */
    public T get(String key, T t, Comparator<T> c) throws NullPointerException{
        if (key == null) {
        	throw new NullPointerException("get: Cannot hash a null value");
        }
    	int bucket = hash(key);
        List<T> list = Table.get(bucket);
    	int index = list.linearSearch(t, c);
    	if (index != -1) {
    		list.iteratorToIndex(index);
            return list.getIterator();
    	} else {
    		return null;
    	}
    }
    
    /**
     * Gets the list at the given key in the hash table
     * 
     * @param key the String to hash to a bucket in the table
     * @return List of elements at the given key
     * or null if this table contains no mapping for the key
     * @throws NullPointerException if key == null
     */
    public List<T> getList(String key) throws NullPointerException {
		if (key == null) {
			throw new NullPointerException("get: Cannot hash a null value");
		}
		int bucket = hash(key);
		List<T> list = Table.get(bucket);
		return list.getLength() > 0 ? list : null;
	}
    
    /**
     * Returns a List of all elements that match the search value
     * 
     * @param key the String to hash to a bucket in the table
     * @param t the value to find
     * @param c Comparator object to use for comparisons
     * @return a list of elements to which the given search value matches 
     * where the key is mapped, or null if this table contains no mapping for the key.
     * @precondition key != null
     * @throws NullPointerException if the precondition is violated
     */
    public List<T> getAll(String key, T t, Comparator<T> c) throws NullPointerException {
    	if (key == null) {
    		throw new NullPointerException("getAll: Cannot hash a null value");
    	}
    	int bucket = hash(key);
        List<T> list = Table.get(bucket),
        		result = new List<>();
    	list.placeIterator();
        while (!list.offEnd()) {
        	if (c.compare(t, list.getIterator()) == 0) {
        		result.addLast(list.getIterator());
        	}
        	list.advanceIterator();
    	}
        return result.getLength() > 0 ? result : null;
    }
    
    /**
     * Determines whether a specified key is in 
     * the Table
     * 
     * @param key the String to hash to a bucket in the table
     * @param t the value to check
     * @return  whether the key is in the Table 
     * @precondition t != null
     * @throws NullPointerException if the specified key is null
     */
    public boolean contains(String key, T t, Comparator<T> c) throws NullPointerException{
    	if (key == null) {
        	throw new NullPointerException("contains: cannot hash null value");
        }
    	int bucket = hash(key);
        List<T> list = Table.get(bucket);
    	int index = list.linearSearch(t, c);
    	return index != -1;
    }
    
     
    /**Mutators*/
    
    /**
     * Inserts a new element in the Table
     * at the end of the chain in the bucket
     * to which the key is mapped
     * @param key the String to hash to a bucket in the table
     * @param t the value to insert
     * @precondition t != null
     * @throws NullPointerException for a null key
     */
    public void put(String key, T t) throws NullPointerException {
    	if (key == null) {
        	throw new NullPointerException("put: cannot hash null value");
        }
    	int bucket = hash(key);
        List<T> list = Table.get(bucket);
    	list.addLast(t);
    	numElements++;
    }
     
     
    /**
     * removes the key t from the Table
     * calls the hash method on the key to
     * determine correct placement
     * has no effect if t is not in
     * the Table or for a null argument          
     * @param key the String to hash to a bucket in the table
     * @param t the value to remove
     * @throws NullPointerException if the key is null
     */
    public void remove(String key, T t, Comparator<T> c) throws NullPointerException {
    	if (key == null) {
        	throw new NullPointerException("remove: cannot hash null value");
        }
    	int bucket = hash(key);
        List<T> list = Table.get(bucket);
        int index = list.linearSearch(t, c);
        if (index != -1) {
        	list.iteratorToIndex(index);
        	list.removeIterator();
        	numElements--;
        }
    }
    
    /**
     * Clears this hash table so that it contains no keys.
     */
    public void clear() {
        int size = Table.size();
    	Table.clear();
    	for (int i = 0; i < size; i++) {
    		Table.add(new List<T>());
    	}
    }

    /**Additional Methods*/

    /**
     * Prints all the keys at a specified
     * bucket in the Table. Each key displayed
     * on its own line, with a blank line 
     * separating each key
     * Above the keys, prints the message
     * "Printing bucket #<bucket>:"
     * Note that there is no <> in the output
     * @param bucket the index in the Table
     */
    public void printBucket(int bucket) {
        System.out.println("Printing bucket #" + bucket + ":");
        System.out.println(Table.get(bucket).toString());
    }
        
    /**
     * Prints the first key at each bucket
     * along with a count of the total keys
     * with the message "+ <count> -1 more 
     * at this bucket." Each bucket separated
     * with two blank lines. When the bucket is 
     * empty, prints the message "This bucket
     * is empty." followed by two blank lines
     */
    public void printTable(){
         for (List<T> list : Table) {
        	 if (list.isEmpty()) {
        		 System.out.println("This bucket is empty.\n");
        	 } else {
        		 System.out.println(list.getFirst() + " + " + 
        				 (list.getLength()-1) + " more at this bucket.\n");
        	 }
         }
     }
    
    /**
     * Starting at the first bucket, and continuing
     * in order until the last bucket, concatenates
     * all elements at all buckets into one String
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<T> list : Table) {
        	sb.append(list.toString());
        }
    	return sb.toString();
    }
    
}
