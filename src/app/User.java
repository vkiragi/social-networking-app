package app;

import java.util.Comparator;

import structures.List;
import structures.BST;

/**
 * User.java
 *
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri CIS 22C, Final Project
 */
public class User {

    private static int count = 0;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String city;
    private BST<User> friends;
    private List<Interest> interests;
    private int id;

    /**
     * Constructor designed for searching in hash table
     *
     * @param firstName user's first name
     * @param lastName user's last name
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = null;
        this.password = null;
        this.city = null;
        this.id = -1;
    }
    
    /**
     * Constructor designed for searching in hash table
     *
     * @param username user's username
     */
    public User(String username){
    	this.firstName = null;
        this.lastName = null;
    	this.username = username;
    	this.password = null;
    	this.city = null;
        this.id = -1;
    }
    /**
     * Create a copy of original User object for searching purpose only
     *
     * @param original User object
     */
    public User(User original) {
        this.username = original.username;
        this.password = original.password;
        this.id = original.id;
    }

    /**
     * Create a new instance of User
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @param username user's username
     * @param password user's password in plain text
     * @param city user's city of residence
     */
    public User(String firstName, String lastName, String username,
            String password, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.setPassword(password);
        this.city = city;
        this.friends = new BST<User>();
        this.interests = new List<Interest>();
        this.id = ++count;
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
     *  Accesses the first name of the user
     * @return first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     *  Accesses the last name of the user
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Accesses the username of this user
     * @return this user's username
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Accesses the password of this user
     * @return this user's password
     */
    public String getPassword() {
    	return this.password;
    }
    
    /**
     *  Accesses the city of the user
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     *  Accesses the the full name of the user
     * @return the first name + last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * returns a BST of this user's friends
     * @return this user's friends
     */
    public BST<User> getFriends() {
    	return friends;
    }
    
    /**
     * returns a list of this user's interests
     * @return this user's interests
     */
    public List<Interest> getInterests() {
        return this.interests;
    }

    /**
	 * Mutators
     */
    
    /**
     *  Sets the first name
     * @param firstName a new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     *  Sets the last name
     * @param lastName a new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *  Sets the username
     * @param username a new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *  Sets the password 
     * @param password a new password
     */
    public void setPassword(String password) {
//        this.password = Integer.toString(password.hashCode());
        this.password=password;
    }

    /**
     *  Adds an interest to the list
     * @param interest a new interest
     */
    public void addInterest(Interest interest) {
        interests.addLast(interest);
    }

    /**
     * Adds a user to this user's friends list
     * 
     * @param user User account to add to friends list
     * @throws NullPointerException if user == null
     */
    public void addFriend(User user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException("addFriend(): Error, the user you are trying to enter is null.");
        }
        friends.insert(user, new NameComparator());
    }

    /**
     * Removes a user from this user's friends list
     * Does nothing if the user is not found
     * 
     * @param user User account to remove from friends list
     * @throws IllegalStateException if friends list is empty
     */
    public void removeFriend(User user) throws IllegalStateException {
        if (friends.isEmpty()) {
            throw new IllegalStateException("Cannot remove from an empty friends list.");
        } else {
            friends.remove(user, new NameComparator());
        }
    }
    
    /**
     *  Removes the specified interest
     * @param i the interest to be removed
     */
    public void removeInterest(Interest i) {
    	int search;
        search = interests.linearSearch(i, new InterestComparator());
        interests.placeIterator();
        for (int j = 0; j <= search; j++) {
            interests.advanceIterator();
        }
        interests.removeIterator();
    }
    
    /**
     *  Hash code method to implement into the Hash Table
     * @return the hash
     */
    @Override
    public int hashCode() {
        int sum = 0;
        char eArray[] = username.toCharArray();
        for (int i = 0; i < username.length(); i++) {
            sum += (int) eArray[i];
        }
        return Math.abs(sum);
    }

    /**
	 * Additional Operations
     */
    
    /**
     * Checks if an object is equal to this User instance Two Users are
     * considered equal if their full names are identical
     *
     * @returns true if the object is a User and has the same name as this
     * instance, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // TODO
        if (this == o) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User compare = (User) o;
            return this.username.equals(compare.username);
        }
    }

    /**
     *  Checks if the password is the same as the password entered
     * @return boolean if passwords are equal
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     *  Prints the list of friends of the user
     * 
     */
    public void printFriendsList() {
        System.out.println("\nYou are friends with:\n");
        friends.inOrderPrint();
    }
    
    /**
     * Returns the number of common interests that the
     * given user has with this user
     * 
     * @param other User to compare interests with
     * @return number of common interests between
     * given user and this user
     */
    public int compareInterests(User other) {
    	int count = 0;
    	interests.placeIterator();
    	// O(n^2) time complexity
    	while (!interests.offEnd()) {
    		other.interests.placeIterator();
    		while(!other.interests.offEnd()) {
    			if (interests.getIterator().equals(other.interests.getIterator())) {
    				count++;
    			}
    			other.interests.advanceIterator();
    		}
    		interests.advanceIterator();
    	}
    	return count;
    }
    
    /**
     *  returns the String representation of this user
     *  
     *  @return String representation of this user
     */
    @Override public String toString(){//place holder for testing purpose
        // TODO: return string of relevant user data
    	String str = firstName + " " + lastName + "\n"
    			+ "Username: " + username + "\n"
    			+ "City: " + city + "\n"
    			+ "Interests: ";
    	interests.placeIterator();
    	for (int i = 0; i < interests.getLength() - 1; i++) {
    		str += interests.getIterator() + ", ";
    		interests.advanceIterator();
    	}
    	return str + interests.getIterator();
    }
}

class UsernameComparator implements Comparator<User> {

    /**
     * Compares two User accounts by comparing their usernames
     *
     * @param user1 the first user
     * @param user2 the second user
     */
    @Override
    public int compare(User user1, User user2) {
        return user1.getUsername().compareTo(user2.getUsername());
    }
}

class UserIdComparator implements Comparator<User> {

    /**
     * Compares two User accounts by comparing first interest in their list
     *
     * @param user1 the first user
     * @param user2 the second user
     */
    public int compare(User account1, User account2) {
        return account1.getId()-account2.getId();
    }
}

class NameComparator implements Comparator<User> {

    /**
     * Compares two User accounts by comparing their first names
     *
     * @param user1 the first user
     * @param user2 the second user
     */
    public int compare(User account1, User account2) {
    	return account1.getFirstName().compareTo(account2.getFirstName());
    }
}

class FullNameComparator implements Comparator<User> {

    /**
     * Compares two User accounts by comparing first names and last names
     *
     * @param user1 the first user
     * @param user2 the second user
     */
    public int compare(User account1, User account2) {
        if (account1.getFirstName().compareTo(account2.getFirstName()) == 0) {
            return account1.getLastName().compareTo(account2.getLastName());
        } else {
            return account1.getFirstName().compareTo(account2.getFirstName());
        }
    }
}