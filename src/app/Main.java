package app;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Main.java
 * @author Jesse Lutan
 * @author Jesse Friaz
 * @author Varun Kiragi
 * @author Tommy Le
 * @author Y Pham
 * @author Rohit Pokuri
 * CIS 22C, Final Project
 */
public class Main {
	
	public static void main(String[] args) {
		App app = null; 
		// initialize App instance
		try {
			File usersFile = new File("users.txt"),
					friendsFile = new File("friends.txt");
			app = new App(usersFile, friendsFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace(System.err);
			return;
		}
		app.start();
	}
	
	

}

