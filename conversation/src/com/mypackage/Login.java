package com.mypackage;

import java.io.BufferedReader;
//import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Scanner;

import com.encryption.EncryptPassword;

public class Login {

	public Login() {
		
	}

	public void terminateApp() {
		System.out.println("Conversation App terminated");
	}

	public void logout() {
		this.loginMenu(); // set to null destroy objects created??
	}

	public void loginMenu() {
		int counter = 3;
		System.out.println("Conversation app v0.1 alpha ");
		System.out.println("********************LOGIN MENU ***************************");
		System.out.println("You have " + counter + " attemps or else you ll get temporary restricted\n");
		do {

			System.out.print("Give Username:");
			String uname = Login.username();
			System.out.print("Give password:");

			String pwd = new EncryptPassword().encryptPassword(Login.password());// encrypt password
			DatabaseAccess dbAccess = new DatabaseAccess();
			if (dbAccess.login(uname, pwd)) {
				counter = 0;
			} else {
				counter--;
				if (counter != 1) {
					System.out.println(counter + " more attempts");
				} else {
					System.out.println(counter + " more attempt");
				}
				if (counter == 0) {
					try {
						System.out.println("\nYour ip is:"+InetAddress.getLocalHost()+" and will get banned\n for failed login attempts\n");
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logout();
					// too many attempts
				}
			}
		} while (counter != 0);

		System.out.println("Press 1 to terminate application or press anything else to login");

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String a = sc.nextLine();
		if (a.equals("1")) {
			sc.close();// close scanner only here it must close only one time or else System.in gets
						// blocked
			terminateApp();
		} else {
			loginMenu();
		}

	}

	// check if username or password have correct format

	public static String username() {
		String username = null;
		int x = 1;

		do {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			username = sc.nextLine();
			if (username.contains(" ") || username.isEmpty() || username.length() < 4) {
				System.out.println("The username you entered is not valid empty or short < 4 or has spaces");
				System.out.print("Give username again:");
			} else {
				x = 0;
			}
		} while (x != 0);

		return username;
	}

	public static String password() {
		String password = null;
		int x = 1;

		do {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			//password masking for running from terminal and not in eclipse console because it does not work inside eclipse
//			Console console = System.console();
//			char[] pw = console.readPassword("Password: ");
//			password = String.valueOf(pw);

			//enable if i need password in eclipse password
			 password = sc.nextLine();

			if (password.contains(" ") || password.isEmpty() || password.length() < 2) {
				System.out.println("Input is not valid, empty or short or has spaces");
				System.out.print("Give password again:");
			} else {
				x = 0;
			}
		} while (x != 0);

		return password;
	}
	
	public static String email() {
		String email = null;
		int x = 1;
		do {
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			email = sc.nextLine();
			if (!isValidEmailAddress(email)) {
				System.out.println("Input is not valid, empty or short or has spaces");
				System.out.print("Give password again:");
			} else {
				x = 0;
			}
		} while (x != 0);

		return email;
	}
	
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }
    
    
    public void checkDBcreation() {
    	DatabaseAccess dbA = new DatabaseAccess();
		dbA.dbConnectionForLoginDatabaseCreation();
		if (dbA.firstDBcreationFlag()) {
			System.out.println("Database is being created.....");
			ScriptRunner runner = new ScriptRunner(dbA.getCon(), false, false);
			String file = "script.sql";
			try {
				runner.runScript(new BufferedReader(new FileReader(file)));
				dbA.firstDBcreation(true);
				System.out.println("Database created!");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}dbA.dbClose();
    }
    
    
    
    
    
	
}