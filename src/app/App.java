package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import structures.Graph;
import structures.HashTable;
import structures.BST;
import structures.List;

/**
 * App.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */
public class App {
	private User currentUser;
	private Graph network;
    private List<User> userList; // list of all users
	private HashTable<User> loginTable; // hashed by username
	private HashTable<User> nameTable; // hashed by first name
	private List<Interest> interestList;
	private List<BST<User>> interestBSTs;
	private HashTable<BST<User>> interestTable; // hashed by interest
    private Scanner userInput;
	private boolean exitFlag;
    
	/*
	 * Constructors
	 */
	
	/**
	 * Initializes the Social Networking App
	 * using data read from two input files
	 * 
	 * @precondition the files exist in the project directory
	 * @throws FileNotFoundException when the precondition is violated
	 * @param usersFile file to read user data from
	 * @param friendsFile file to read friend data from
	 */
	public App(File usersFile, File friendsFile) throws FileNotFoundException {
		userInput = new Scanner(System.in);
		userList = new List<>();
        interestList = new List<>();
        interestBSTs = new List<>();
		int numUsers = readUsersFromFile(usersFile); // create users and hash tables
		network = new Graph(numUsers + 1); // 1 extra spot in case a new user is created
		userListToLoginTable();
		userListToNameTable();
		interestListToInterestTable();
		System.out.println("Welcome to JavaBook. Please login.\n");
		currentUser = userLogin();
		readFriendsFromFile(friendsFile); // create network graph
		exitFlag = false;
		System.out.println("\nInitialization successful! Starting the app...");
//		System.out.println(nameTable);
	}
	
	/**
	 * Reads user data from an input file
	 * and initializes user hash tables
	 * 
	 * @param usersFile file to read user data from
	 * @throws FileNotFoundException if the file cannot be found
	 * @return total number of users in text file
	 */
	private int readUsersFromFile(File usersFile) throws FileNotFoundException {
		Scanner reader;
		try {
			reader = new Scanner(usersFile);
		} catch(FileNotFoundException e) {
			throw new FileNotFoundException("Could not find users input file");
		}
		
		// initialize users and interests
		int userCount = 0;
		while (reader.hasNext()) {
			String fullName = reader.nextLine().trim();
			String[] name = fullName.split(" ");
			String username = reader.nextLine().trim();
			String password = reader.nextLine().trim();
			String city = reader.nextLine().trim();
			// create new user using scanned data
			User user = new User(name[0], name[1], username, password, city);
			// read list of user's interests
			String interest = reader.nextLine().trim();
			while(!interest.equals("")) {
				// search for interest in interests array
				Interest interestObj = new Interest(interest, -1);
				/*
				 * insert interest as a key to interestTable
				 * 	if it already exists, insert the user at that key 
				 */
				int index = interestList.linearSearch(interestObj, new InterestComparator());
				if (index != -1) {
					interestList.iteratorToIndex(index);
					interestObj = interestList.getIterator();
					interestBSTs.iteratorToIndex(index);
					interestBSTs.getIterator().insert(user, new NameComparator());
				} else {
					interestObj = new Interest(interest);
					BST<User> temp = new BST<>();
					temp.insert(user, new NameComparator());
					interestList.addLast(interestObj);
					interestBSTs.addLast(temp);
				}
				user.addInterest(interestObj);
				if (reader.hasNext()) {
					interest = reader.nextLine().trim();
				} else {
					interest = "";
				}
			}
            userList.addLast(user);
			userCount++;
		}
		reader.close();
        return userCount;
	}
	
	/**
	 * Reads friend data from an input file
	 * and initializes network graph
	 * 
	 * @precondition nameTable is not null
	 * @param friendsFile file to read friend data from
	 * @throws FileNotFoundException if file cannot be found
	 * @throws IllegalStateException if nameTable is null
	 */
	private void readFriendsFromFile(File friendsFile) throws FileNotFoundException, IllegalStateException {
		if (nameTable == null) {
			throw new IllegalStateException("Something went wrong while initializing the nameTable.");
		}
		Scanner reader;
		try {
			reader = new Scanner(friendsFile);
		} catch(FileNotFoundException e) {
			throw new FileNotFoundException("Could not find friends input file");
		}
		
		// initialize friends BSTs and graph edges
		while (reader.hasNext()) {
			String fullName = reader.nextLine().trim();
			String[] name = fullName.split(" ");
			User user = nameTable.get(name[0], new User(name[0], name[1]), new FullNameComparator());
			// read list of user's friends
			String friendFullName = reader.nextLine().trim();
			while (!friendFullName.equals("")) {
				String[] friendName = friendFullName.split(" ");
				// get friend's account from hash table
				User friend = nameTable.get(friendName[0], new User(friendName[0], friendName[1]), new FullNameComparator());
				// add to user's friends list
				user.addFriend(friend);
				// update network graph
				addConnection(user, friend);
				if (reader.hasNext()) {
					friendFullName = reader.nextLine().trim();
				} else {
					friendFullName = "";
				}
			}
		}
		reader.close();
	}
	
	/*
	 * Mutators
	 */
        
    /**
	 * Convert userList into HashTable loginTable
	 */
	private void userListToLoginTable(){
        loginTable = new HashTable<User>(userList.getLength());
        userList.placeIterator();
        for(int i = 0; i< userList.getLength(); i++){
            User user = userList.getIterator();
        	loginTable.put(user.getUsername(), user);
            userList.advanceIterator();
        }
    }
        
	/**
	 * Convert userList into HashTable nameTable
	 * Call this whenever you want to access HashTable
	 */
    private void userListToNameTable(){
        nameTable = new HashTable<User>(userList.getLength());
        userList.placeIterator();
        for(int i = 0; i< userList.getLength(); i++){
        	User user = userList.getIterator();
        	nameTable.put(user.getFirstName(), user);
            userList.advanceIterator();
        }
    }
    
    /**
     * Convert interestList to interestTable
     */
    private void interestListToInterestTable() {
    	interestTable = new HashTable<>(interestBSTs.getLength());
    	interestList.placeIterator();
    	interestBSTs.placeIterator();
    	for (int i = 0; i < interestBSTs.getLength(); i++) {
    		String interest = interestList.getIterator().getInterest();
    		BST<User> userTree = interestBSTs.getIterator();
    		interestTable.put(interest, userTree);
    		interestList.advanceIterator();
    		interestBSTs.advanceIterator();
    	}
    }
    
    /**
     * Prompts the user for login credentials
     * If the password is incorrect, prompts to try again
     * If the account is not found, asks the user if
     * they want to create a new account or try again
     * 
     * @return the user that is found in the hash table
     */
	private User userLogin() {//pending additional testing
        User user = null;
        //loginTable.printTable();
        while(user == null){
            System.out.print("Enter your username: ");
            String username = userInput.nextLine().trim();
            System.out.print("Enter your password: ");
            String password = userInput.nextLine().trim();
            
            user = loginTable.get(username, new User(username), new UsernameComparator());
            if(user != null){
            	if(user.checkPassword(password)){
            		break;
                } else {
                	user = null;
                	System.out.println("Wrong password. Please try again.");
                }
            } else {
            	System.out.println("The account was not found in the system. Would you like to create a new account?");
            	System.out.print("Yes/No: ");
            	// TODO: handle new user account creation
                String choice = userInput.nextLine().trim();
                
                if(choice.equalsIgnoreCase("yes")){
                    user = newUserCreation();
                    userListToLoginTable();
                    userListToNameTable();
                }
            }
        }
        return user;
	}
        
	/**
	 * Creates a new account and adds it to
	 * hash tables and network graph
	 * 
	 * @return the newly created user
	 */
    private User newUserCreation(){
        User user = null;
    	while(true){
            System.out.print("Enter username: ");
            String newUserName = userInput.nextLine().trim();
            user = loginTable.get(newUserName, new User(newUserName), new UsernameComparator());
            if(user != null){
                System.out.println("Username already exist.");
            } else {
                System.out.print("Enter password: ");
                String password = userInput.nextLine().trim();
                System.out.print("Enter first name: ");
                String firstName = userInput.nextLine().trim();
                System.out.print("Enter last name: ");
                String lastName = userInput.nextLine().trim();
                System.out.print("Enter city: ");
                String city = userInput.nextLine().trim();
                currentUser = user = new User(firstName, lastName, newUserName, password, city);
                userList.placeIterator();
                boolean exit = false;
                List<User> users = new List<>(userList);
                while (!exit) {
                	System.out.println("Users:");
                	users.printNumberedList();
                	System.out.print("Please add at least 1 friend:\n"
                			+ "1. Add a friend\n"
                			+ "2. Exit\n"
                			+ "> ");
                	int selection = Integer.valueOf(userInput.nextLine().trim());
                	switch (selection) {
                	case 1:
                		System.out.print("Who would you like to add as a friend? "
                				+ "(Enter a number from 1-" + users.getLength() + ")\n"
                				+ "> ");
                		selection = Integer.valueOf(userInput.nextLine().trim());
                		if (selection < 1 || selection > users.getLength()) {
                			System.out.println("Invalid selection.");
                		} else {
                			users.iteratorToIndex(selection - 1);
                			User friend = users.getIterator();
                			addFriend(friend);
                			addConnection(currentUser, friend);
    						System.out.println(friend.getFullName()
    								+ " has been added as a friend.\n");
    						users.removeIterator();
    						if (users.getLength() <= 0)
    							exit = true;
    						System.out.println("\nPress enter to continue...");
    						userInput.nextLine();
                		}
                		break;
                	case 2:
                		if (user.getFriends().getSize() < 1) {
                			System.out.println("You must add at least 1 friend.");
                		} else {
                			exit = true;
                		}
                		break;
                	default:
                		System.out.println("Please enter an option 1-2.");
                	}
                }
                userList.addLast(user);
                break;
            }
        }
        return user;
    }
	
	/**
	 * Adds user2 as a friend of user1 in the
	 * friend network graph by creating a
	 * directed edge from user1 to user2
	 * 
	 * @param user1 user to access in the network
	 * @param user2 user to add as a friend of user1
	 * @precondition user1 != null && user2 != null
	 * @throws NullPointerException when precondition is violated
	 */
	private void addConnection(User user1, User user2) throws NullPointerException {
		if (user1 == null || user2 == null) {
			throw new NullPointerException("addFriend: users cannot null");
		}
		network.addDirectedEdge(user1.getId(), user2.getId());
	}
	
	/**
	 * Removes user2 from user1's adjacency list and
	 * vice versa in the friend network graph
	 * 
	 * @param user1 user to access in the network
	 * @param user2 user to add as a friend of user1
	 * @precondition user1 != null && user2 != null
	 * @throws NullPointerException when precondition is violated
	 */
	private void removeConnection(User user1, User user2) throws NullPointerException {
		if (user1 == null || user2 == null) {
			throw new NullPointerException("addFriend: users cannot null");
		}
		network.removeAdjacency(user1.getId(), user2.getId());
	}
	
	/**
	 * Add the friend as a friend of the current user
	 * and the current user as a friend of the friend
	 * 
	 * @param friend friend's User account
	 * @precondition friend != null
	 * @throws NullPointerException when precondition is violated
	 */
	private void addFriend(User friend) throws NullPointerException {
		try {
			currentUser.addFriend(friend);
			friend.addFriend(currentUser);
			addConnection(currentUser, friend);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Remove the friend from the current user's friends list
	 * and removes the current user from the friend's friend list
	 * If the friend's account is not found in the user's friends list,
	 * does nothing
	 * 
	 * @param friend friend's User account
	 */
	private void removeFriend(User friend) throws NullPointerException {
		try {
			currentUser.removeFriend(friend);
			friend.removeFriend(currentUser);
			removeConnection(currentUser, friend);
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * Accessors
	 */
	
	private boolean exitted() {
		return exitFlag;
	}
	
	/*
	 * Additional Operations
	 */
	
	public void start() {
		while (!exitted()) {
			menu();
		}
	}
	
	/**
	 * Prints the main menu to console and reads user input
	 */
	private void menu() {
		System.out.print("\nJavaBook Menu:\n"
				+ "1. My Friends\n"
				+ "2. Search Users\n"
				+ "3. Friend Recommendations\n"
				+ "4. Exit\n"
				+ "> ");	
		int selection = Integer.valueOf(userInput.nextLine().trim());
		switch (selection) {
		case 1:
			myFriendsMenu();
			break;
		case 2:
			searchUsers();
			break;
		case 3: {
			List<User> recommendations = getRecommendations();
//			network.printBFS(); // for debugging
			
			boolean exit = false;
			while (!exit) {
				System.out.println("\nFriend Recommendations:\n");
				recommendations.printNumberedList();
				System.out.print("\nPlease Select an option below:\n"
						+ "1. Add a user to friends list\n"
						+ "2. Exit\n"
						+ "> ");
				selection = Integer.valueOf(userInput.nextLine().trim());
				switch(selection) {
				case 1:
					System.out.print("Which user would you like to add as a friend? "
							+ "(Enter a number 1-" + recommendations.getLength() + ")\n"
							+ "> ");
					selection = Integer.valueOf(userInput.nextLine().trim());
					if (selection < 1 || selection > recommendations.getLength()) {
						System.out.println("Invalid selection.");
					} else { // add user as a friend and remove from recommendations
						recommendations.iteratorToIndex(selection - 1);
						User user = recommendations.getIterator();
						// add user to friends list and add adjacency in network
						addFriend(user);
						addConnection(currentUser, user);
						System.out.println(user.getFullName()
								+ " has been added as a friend.\n");
						recommendations.removeIterator();
						if (recommendations.getLength() <= 0)
							exit = true;
						System.out.println("\nPress enter to continue...");
						userInput.nextLine();
					}
					break;
				case 2:
					exit = true;
					break;
				default:
					System.out.println("Please enter an option 1-2.");
				}
			}
			break;
		}
		case 4:
			exit();
			break;
		default:
			System.out.println("Please enter an option 1-4.");
		}
	}
	
	/**
	 * Prints the main menu to console and reads user input
	 */
	private void myFriendsMenu() {
		boolean exit = false;
		while (!exit) {
			System.out.print("\nMy Friends:\n"
					+ "1. View friends list\n"
					+ "2. View a friend's profile\n"
					+ "3. Remove a friend\n"
					+ "4. Exit My Friends\n"
					+ "> ");
			int selection = Integer.valueOf(userInput.nextLine().trim());
			switch(selection) {
			case 1:
				currentUser.printFriendsList();
				System.out.println("\nPress enter to continue...");
				userInput.nextLine();
				break;
			case 2: {
				boolean exit1 = false;
				while (!exit1) {
					List<User> friends = currentUser.getFriends().toListInOrder();
					int index = 0;
					System.out.println("\nYour friends:\n");
					friends.placeIterator();
					while (!friends.offEnd()) {
						System.out.println(++index + ": " + friends.getIterator().getFullName() + "\n");
						friends.advanceIterator();
					}
					System.out.print("Please select an option below:\n"
							+ "1. View a friend's profile\n"
							+ "2. Exit\n"
							+ "> ");
					selection = Integer.valueOf(userInput.nextLine().trim());
					switch (selection) {
					case 1:
						System.out.print("Which friend's profile would you like to view? "
								+ "(Enter a number 1-" + friends.getLength() + ")\n"
								+ "> ");
						selection = Integer.valueOf(userInput.nextLine().trim());
						if (selection < 1 || selection > friends.getLength()) {
							System.out.println("Invalid selection.");
						} else {
							friends.iteratorToIndex(selection - 1);
							System.out.println(friends.getIterator());
							System.out.println("\nPress enter to continue...");
							userInput.nextLine();
						}
					case 2:
						exit1 = true;
						break;
					default:
						System.out.println("Please enter an option 1-2.");
					}
				}
				break;
			}
			case 3: {
				boolean exit1 = false;
				while (!exit1) {
					List<User> friends = currentUser.getFriends().toListInOrder();
					int index = 0;
					System.out.println("\nYour friends:\n");
					friends.placeIterator();
					while (!friends.offEnd()) {
						System.out.println(++index + ": " + friends.getIterator().getFullName() + "\n");
						friends.advanceIterator();
					}
					System.out.print("Please select an option below:\n"
							+ "1. Remove a friend\n"
							+ "2. Exit\n"
							+ "> ");
					selection = Integer.valueOf(userInput.nextLine().trim());
					switch (selection) {
					case 1:
						System.out.print("Which friend would you like to remove? "
								+ "(Enter a number 1-" + friends.getLength() + ")\n"
								+ "> ");
						selection = Integer.valueOf(userInput.nextLine().trim());
						if (selection < 1 || selection > friends.getLength()) {
							System.out.println("Invalid selection.");
						} else {
							friends.iteratorToIndex(selection - 1);
							// remove friend from friends list and remove adjacency from network 
							removeFriend(friends.getIterator());
							System.out.println(friends.getIterator().getFullName()
									+ " has been removed from your friends list.");
							friends.removeIterator();
							System.out.println("\nPress enter to continue...");
							userInput.nextLine();
						}
					case 2:
						exit1 = true;
						break;
					default:
						System.out.println("Please enter an option 1-2.");
					}
				}
				break;
			}
			case 4: 
				exit = true;
				break;
			default:
				System.out.println("Please enter an option 1-4.");
			}
		}
	}
	
	/**
	 * Prompts the user to search for 
	 * users by name or by interest
	 */
	private void searchUsers() {
		boolean exit = false;
		while (!exit) {
			System.out.print("\nSearch for users by:\n"
					+ "1. Name\n"
					+ "2. Interest\n"
					+ "3. Exit\n"
					+ "> ");
			int selection = Integer.valueOf(userInput.nextLine().trim());
			switch(selection) {
			case 1: {
				System.out.print("\nUser's name: ");
				String fullName = userInput.nextLine().trim();
				List<User> users = findUsers(fullName);
				if (users == null || users.getLength() <= 0) {
					System.out.println("Try again with a capitalized name. "
							+ "If the message still occurs, then there are no users with the given name"
							+ " or you are already friends with them all.");
				} else {
					boolean exit1 = false;
					while (!exit1) {
						System.out.println("\nUsers named " + fullName + ":\n");
						users.printNumberedList();
						System.out.print("\nPlease select an option below:\n"
								+ "1. Add a friend from this list\n"
								+ "2. Exit\n"
								+ "> ");
						selection = Integer.valueOf(userInput.nextLine().trim());
						switch (selection) {
						case 1:
							System.out.print("\nWhich user would you like to add as a friend? "
									+ "(Enter a number 1-" + users.getLength() + ")\n"
									+ "> ");
							selection = Integer.valueOf(userInput.nextLine().trim());
							if (selection < 1 || selection > users.getLength()) {
								System.out.println("Invalid selection.");
							} else {
								users.iteratorToIndex(selection - 1);
								addFriend(users.getIterator());
								System.out.println(users.getIterator().getFullName()
										+ " has been added to your friends list.");
								users.removeIterator();
								if (users.getLength() <= 0)
									exit1 = true;
								System.out.println("\nPress enter to continue...");
								userInput.nextLine();
							}
							break;
						case 2:
							exit1 = true;
							break;
						default:
							System.out.println("Please enter an option 1-2.");
						}
					}
				}
				break;
			}
			case 2: {
				System.out.print("\nName of interest: ");
				String interest = userInput.nextLine().trim().toLowerCase();
				Interest interestObj = new Interest(interest, -1);
				List<User> users = findUsers(interestObj);
				if (users == null || users.getLength() <= 0) {
					System.out.println("There are no users with the given interest"
							+ " or you are already friends with them all.");
				} else {
					boolean exit1 = false;
					while (!exit1) {
						System.out.println("\nUsers who are interested in " + interest + ":\n");
						users.printNumberedList();
						System.out.print("Please select an option below:\n"
								+ "1. Add a friend from this list\n"
								+ "2. Exit\n"
								+ "> ");
						selection = Integer.valueOf(userInput.nextLine().trim());
						switch (selection) {
						case 1:
							System.out.print("\nWhich user would you like to add as a friend? "
									+ "(Enter a number 1-" + users.getLength() + ")\n"
									+ "> ");
							selection = Integer.valueOf(userInput.nextLine().trim());
							if (selection < 1 || selection > users.getLength()) {
								System.out.println("Invalid selection.");
							} else {
								users.iteratorToIndex(selection - 1);
								addFriend(users.getIterator());
								System.out.println(users.getIterator().getFullName()
										+ " has been added to your friends list.");
								users.removeIterator();
								if (users.getLength() <= 0)
									exit1 = true;
								System.out.println("\nPress enter to continue...");
								userInput.nextLine();
							}
							break;
						case 2:
							exit1 = true;
							break;
						default:
							System.out.println("Please enter an option 1-2.");
						}
					}
				}
				break;
			}
			case 3:
				exit = true;
				break;
			default:
				System.out.println("Please enter an option 1-3.");
			}
		}
	}
	
	/**
	 * Finds user(s) that match the given name
	 * and are not friends with the current user
	 * If only one name is given, it is assumed to be the first name
	 * 
	 * @param fullName first name or full name of the users to search
	 * @return an ArrayList containing all users that have the given name
	 * and are not friends with the current user, or null if no users are found
	 */
	private List<User> findUsers(String fullName) {
		String[] name = fullName.split(" ");
		List<User> l = new List<User>();
		if (name.length > 1) {
			l = nameTable.getAll(name[0], new User(name[0], name[1]), new NameComparator());
		} else {
			l = nameTable.getAll(name[0], new User(name[0], "unknown"), new NameComparator());	
		}
		if (l != null) {
			List<User> curFriends = currentUser.getFriends().toListInOrder();
			l.placeIterator();
			while (!l.offEnd()) {
				if (l.getIterator() == currentUser || 
						curFriends.linearSearch(l.getIterator(), new FullNameComparator()) != -1) {
					l.removeIterator();
				} else {
					l.advanceIterator();
				}
			}
		}
		return l;
	}
	
	/**
	 * Finds user(s) that have the given interest and are
	 * not friends with the current user
	 * 
	 * @param interest the Interest to search
	 * @return a List containing all users that have the given interest
	 * and are not friends with the current user, or null if no users are found
	 */
	private List<User> findUsers(Interest interest) {
        if (interestList.linearSearch(interest, new InterestComparator()) == -1) {
        	return null;
        }
		List<BST<User>> list = interestTable.getList(interest.getInterest());
        if (list == null)
        	return null;
        list.placeIterator();
        List<User> users = list.getIterator().toListInOrder();
		//i = interestTable.get(interest.getInterest(), i, new InterestComparator());
        if (users != null) {
			List<User> curFriends = currentUser.getFriends().toListInOrder();
			users.placeIterator();
			while (!users.offEnd()) {
				if (users.getIterator() == currentUser || 
						curFriends.linearSearch(users.getIterator(), new FullNameComparator()) != -1) {
					users.removeIterator();
				} else {
					users.advanceIterator();
				}
			}
		}
        return users;
	}
	
	/**
	 * Compiles a list of friend recommendations within the user's social circle
	 * This includes friends of friends of any distance
	 * Recommendations are ranked in order of distance and number of common interests
	 * 
	 * @return a List containing all users that can be
	 * recommended to the current user, ranked in order of distance and common interests
	 */
	private List<User> getRecommendations() {
		network.BFS(currentUser.getId());
		ArrayList<User> recommendList = new ArrayList<>();
		List<User> recommendations = new List<>();
		
		for (int i = 1; i <= network.getNumVertices(); i++) {
			// recommend users who have a distance >= 2 from the current user  
			if (network.getDistance(i) >= 2) {
				userList.iteratorToIndex(i - 1);
				recommendList.add(userList.getIterator());
			}
		}
		// sort recommendations in order of distance and common interests
		// bubble sort - O(n^2) time complexity
		for (int i = 0; i < recommendList.size(); i++) {
			for (int j = 0; j < recommendList.size() - i - 1; j++) {
				User user1 = recommendList.get(j), user2 = recommendList.get(j + 1);
				int distance1 = network.getDistance(user1.getId()),
						distance2 = network.getDistance(user2.getId());
				if (distance1 > distance2) {
					User temp = recommendList.get(j);
					recommendList.set(j, recommendList.get(j + 1));
					recommendList.set(j + 1, temp);
				} else if (distance1 == distance2) {
					if (user1.compareInterests(currentUser) < user2.compareInterests(currentUser)) {
						User temp = recommendList.get(j);
						recommendList.set(j, recommendList.get(j + 1));
						recommendList.set(j + 1, temp);
					}
				}
			}
		}
		// fill recommendations List with sorted ArrayList elements
		for (int i = 0; i < recommendList.size(); i++) {
			recommendations.addLast(recommendList.get(i));
		}
		return recommendations;
	}
	
	/**
	 * Saves current session data to text files
	 * and sets the exit flag to true 
	 */
	private void exit() { 
		System.out.println("\nSaving session data to database...");
		// write session data to file before quitting
		FileWriter writeUsers = null, writeFriends = null;
		try {
			File usersFile = new File("users.txt"),
					friendsFile = new File("friends.txt");
			usersFile.delete();
			friendsFile.delete();
			usersFile.createNewFile();
			friendsFile.createNewFile();
			
			writeUsers = new FileWriter(usersFile);
			writeFriends = new FileWriter(friendsFile);
			// write to users.txt
			userList.placeIterator();
			while (!userList.offEnd()) {
				User user = userList.getIterator();
				writeUsers.write(user.getFullName() + '\n');
				writeUsers.write(user.getUsername() + '\n');
				writeUsers.write(user.getPassword() + '\n');
				writeUsers.write(user.getCity() + '\n');
				List<Interest> interests = user.getInterests();
				interests.placeIterator();
				while (!interests.offEnd()) {
					writeUsers.write(interests.getIterator().getInterest() + '\n');
					interests.advanceIterator();
				}
				writeUsers.write('\n');
				userList.advanceIterator();
			}
			// write to friends.txt
			userList.placeIterator();
			while (!userList.offEnd()) {
				User user = userList.getIterator();
				writeFriends.write(user.getFullName() + '\n');
				BST<User> friends = user.getFriends();
				List<User> friendsList = friends.toListInOrder();
				friendsList.placeIterator();
				while (!friendsList.offEnd()) {
					writeFriends.write(friendsList.getIterator().getFullName() + '\n');
					friendsList.advanceIterator();
				}
				writeFriends.write('\n');
				userList.advanceIterator();
			}
			writeUsers.close();
			writeFriends.close();
		} catch (IOException e) {
			System.out.println("There was an issue accessing database files. No changes were made.");
		}
		
		exitFlag = true;
		System.out.println("\nThank you for using this program!");
	}
}
