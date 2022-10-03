package app;

import java.util.Comparator;

/**
 * Interest.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */
public class Interest {
	
	private static int count = 0;
	private int id;
	private String interest;
	
	/**
	 * Creates an instance of Interest
	 * with a unique id value
	 * 
	 * @param interest interest String
	 */
	public Interest(String interest) {
		this.id = ++count;
		this.interest = interest;
	}
	
	/**
	 * Utility constructor for searching purposes
	 * 
	 * @param interest interest String
	 * @param id placeholder id value
	 */
	public Interest(String interest, int id) {
		this.id = id;
		this.interest = interest;
	}
	
	/**
	 * Accessors
	 */
	
	/**
     *  Accesses the id of the user
     * @return id
     */
	public int getId() {
		return this.id;
	}
	
	/**
     *  Accesses the interest of the user
     * @return interest
     */
	public String getInterest() {
		return this.interest;
	}
	
	/*
	 * Additional Operations
	 */
	
	/**
	 * Checks if a given object is equal to this interest
	 * Two interests are equal if they have the same interest string
	 * 
	 * @param Object o comparison object
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof Interest)) {
			return false;
		} else {
			Interest comp = (Interest) o;
			if (comp.getInterest().compareTo(interest) == 0) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Return the String of this object
	 * 
	 * @return String version of this Interest object
	 */
	@Override
	public String toString() {
		return this.interest;
	}
}

class InterestIdComparator implements Comparator<Interest> {

    /**
     * Compares two Interests by comparing their IDs
     *
     * @param interest1 the first interest
     * @param interest2 the second interest
     */
    public int compare(Interest interest1, Interest interest2) {
        return interest1.getId()-interest2.getId();
    }
}

class InterestComparator implements Comparator<Interest> {

    /**
     * Compares two User accounts by comparing the first interest in their list
     *
     * @param interest1 first interest
     * @param interest2 second interest
     */
    public int compare(Interest interest1, Interest interest2) {
        return interest1.getInterest().compareTo(interest2.getInterest());
    }
}