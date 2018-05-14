package com.mypackage;
import java.util.HashMap;
import java.util.Scanner;

import com.encryption.EncryptPassword;

public class Admin extends SuperModerator {
	
	private static Scanner sc=new Scanner(System.in);
	private static HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
	
	
	
	public Admin(int id, String password, String username, String email,String accessLevel) {
		super(id, password, username, email, accessLevel);
		// TODO Auto-generated constructor stub
	}

	public Admin() {
		// TODO Auto-generated constructor stub
	}
	

	
	public void setPermissions() {
		System.out.println("Change Permissions for user");
		System.out.println("Choose which user you want to apply permission changes");
		DatabaseAccess dbA=new DatabaseAccess();
		dbA.dbConnection();
		myMap = dbA.fetchUsersForAdmin(); // access_level administrators are excluded from administrator list
		try {
			int id = Integer.parseInt(sc.nextLine());
			dbA.setPermissions(myMap.get(id));
		} catch (NumberFormatException e) {
			System.out.println("--------------------------Enter only numbers please-------------------------");
		}
		dbA.dbClose();

	}

	public void createUser() {
		System.out.print("Create new user\n");
		System.out.print("Give username\n");
		String username = Login.username();
		System.out.print("Give password\n");
		String password = Login.password();
		new DatabaseAccess().createUser(username, new EncryptPassword().encryptPassword(password));  //password encryption MD5
	}

	public void updateUser() {
		System.out.println("Update user");
		System.out.println("Choose which user you want to apply updates");
		DatabaseAccess dbA = new DatabaseAccess();
		dbA.dbConnection();
		myMap = dbA.fetchUsersForAdmin();
		try {
			int choice = Integer.parseInt(sc.nextLine());
			if (choice > myMap.size() || choice <= 0) {
				System.out.print("Out of index!!");
			} else {
				System.out.println("What do you want to update??");
				System.out.println("1) username?");
				System.out.println("2) password?");
				System.out.println("3) email?\n");
				dbA.updateUser(myMap.get(choice));
			}
		} catch (NumberFormatException e) {
			System.out.println("--------------------------Enter only numbers please-------------------------");
		}
		dbA.dbClose();
	}
	
	public void  deleteUser() {
		System.out.println("Delete user");
		System.out.println("Choose User for deletion");
		DatabaseAccess dbA = new DatabaseAccess();
		dbA.dbConnection();
		myMap = dbA.fetchUsersForAdmin(); 

		if (myMap.isEmpty()) {
			System.out.println("-----------------------No users left to delete------------------");
		} else {
			String sid = sc.nextLine();
			if (sid.isEmpty() || !sid.matches(".*\\d+.*")) {
				System.out.println("Invalid");
			} else {
				int id = Integer.parseInt(sid);

				if (id > myMap.size()) {
					System.out.print("Out of index!!");
				} else {
					dbA.deleteUser(myMap.get(id));
				}
			}
		}
		dbA.dbClose();
	}
}
