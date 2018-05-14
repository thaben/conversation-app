package com.mypackage;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuConversations {
	
	
	//this does not work  !! for future development
	
	
	public void viewConversationMenu(User sender, HashMap<Integer, Integer> myMap,int choice) {
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		Conversation conv = null;
		int idConversation;
		
		if (myMap.size() == 0) {
			System.out.println("--------------------------No conversations found-------------------------");
		} else {
			System.out.println("Select which conversation you want to open by typing its id"); // Maybe new Scanner
			try { 
				// a new conversation
				idConversation = sc.nextInt();

				if (idConversation > myMap.size() || idConversation <= 0) {
					System.out.println("Out of index");
				} else {
					conv = new DatabaseAccess().loadConversationToObject(myMap.get(idConversation));
					if (choice == 1) {
						new MenuMessages().viewMenuMessages(conv, sender, myMap);
					} else {
						new MenuMessagesPrivileges().viewMenuMessagesPrivileges(conv, sender, myMap); // sender here is
																										// editor
					}
				} // end if
			} catch (InputMismatchException e) {
				System.out.println("Wrong input");
			}
		} // end if

	}

}
