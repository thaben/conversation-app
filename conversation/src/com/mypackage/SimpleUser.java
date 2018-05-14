package com.mypackage;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class SimpleUser extends User{

	public SimpleUser(int id,String password, String username, String email,String accessLevel) {
		super(id, password, username, email,accessLevel);
		// TODO Auto-generated constructor stub
	}
	
	public SimpleUser() {
		super();
	}

	private  String receiverUsername;
	private  String title;

	public void startConversation(User user) {
		
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		
		int x = 1;
		String choiceForRandom = null;
		
		DatabaseAccess dbA = new DatabaseAccess();
		dbA.dbConnection();

		ArrayList<User> listusers = dbA.getUserListAndPrintThem(user.getUsername()); //maybe change to hashMap
		if(listusers.size()!=0) {
					
		while (x != 0) {
			System.out.println("Press anything to select which user you want to start an conversation");
			System.out.println("Press 1 to enter CHAT ROULETE");
			choiceForRandom=sc.nextLine();
			
			if (choiceForRandom.equals("1")) {
				System.out.println("Chat Roulete");
				Random randomGenerator = new Random();			
				int index = randomGenerator.nextInt(listusers.size());
				User receiverUser = listusers.get(index);			
//				System.out.print(listusers.get(index));
//				System.out.println(receiverUser);
//				System.out.println(receiverUser.getUsername());
				//null exceptions sometimes needs more testing			
				receiverUsername = receiverUser.getUsername();
					System.out.println("The lucky user is:" + receiverUsername);
				} else {
					System.out
							.println("Select with which user you want to start an conversation by typing his username");
					receiverUsername = sc.nextLine().toLowerCase();
				}
				for (User receiver : listusers) {
					//System.out.println(receiver);
					//dbA.loadUserToObjectById(x) if i want to implement with hashmap
					if (receiver.getUsername().equals(receiverUsername)) {
						User receiverUser = dbA.loadUserToObjectByUsername(receiverUsername);
						
						System.out.println("Select an title for your conversation");
						title = sc.nextLine();
					if (title.isEmpty()) {
						System.out.println("Empty title going back...");
					} else {
						// get conversation id from database because it is auto generated
						Conversation conversation = new Conversation(title, user, receiverUser);
						dbA.startConversation(conversation);					
						dbA.dbClose();
						x = 0;
						break;
						}
					} // end if
				} // end for
			} // end while
		} // end if
		System.out.println("---------------------------------No users in the system-------------------------");
	}// end of method
	
	public void viewConversations(User user) {
		DatabaseAccess dbA=new DatabaseAccess();
		dbA.dbConnection();
		dbA.viewConversations(user);
		dbA.dbClose();
	}

	//extra not implemented
	public void viewUnrespondedConversations(User user) {
		DatabaseAccess dbA=new DatabaseAccess();
		dbA.dbConnection();
		dbA.dbClose();
	}


}
