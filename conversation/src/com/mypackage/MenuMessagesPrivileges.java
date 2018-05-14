package com.mypackage;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuMessagesPrivileges {
	
	
	
	public void viewMenuMessagesPrivileges(Conversation conv, User editor, HashMap<Integer, Integer> myMap) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String choice;
		int x = 1;

		DatabaseAccess dbA = new DatabaseAccess();
		dbA.dbConnection();

		while (x != 0) {
			myMap = dbA.viewConversationMessagesReturnHashMap(conv, editor, true);
			if (editor.getClass().getSimpleName().equals("Moderator")) {
				try {
					System.out.print("Actions\n" +
									"1)Modify  message \n" + 
									"0)Go back to menu\n");
					choice = sc.nextLine();
					if (choice.equals("1")) {
						System.out.println("Modify message");
						myMap = dbA.viewConversationMessagesReturnHashMap(conv, editor, true);
						if (myMap.size() != 0) {
							System.out.println("Select by id which message to modify");

							int idMessage = Integer.parseInt(sc.nextLine());
							if (idMessage > myMap.size() || idMessage <= 0) {
								System.out.println("Out of index");
							} else {
								System.out.println(myMap.get(idMessage));
								try {
									dbA.modifyMessageById(myMap.get(idMessage));
									//x = 0;
								} catch (NullPointerException e) {
									System.out.println("There is no message with that kind of id");
								}
							} // end if
						} else {
							System.out.println("No messages");
						}
					} else if (choice.equals("0")) {
						System.out.print("Going back to menu");
						x = 0;
					} else {
						System.out.println("Messages refreshed");
					} // end if
				} catch (InputMismatchException e) {
					System.out.println("Invalid option");
				}
			} else {
				System.out.print("Actions\n "
						+ "1)Delete message\n "
						+ "2)Modify message\n "
						+ "0)Go back to menu");
				try {
					@SuppressWarnings("resource")
					Scanner sc1 = new Scanner(System.in);
					choice = sc1.nextLine();

					 if (choice.equals("1")) {
						
						System.out.println("Delete message");
						myMap = dbA.viewConversationMessagesReturnHashMap(conv, editor, true);
						if (myMap.size() != 0) {
							System.out.println("Select by id which message to delete");
							int idMessage = sc1.nextInt();
							try {
								dbA.deleteMessageById(myMap.get(idMessage));
							} catch (NullPointerException e) {
								System.out.println("There is no such message with that kind of id");
							}
						}else {
							System.out.println("No messages");
						}

					} else if (choice.equals("2")) {

						System.out.println("Modify message");
						myMap = dbA.viewConversationMessagesReturnHashMap(conv, editor, true);
						if (myMap.size() != 0) {
							System.out.println("Select by id which message to modify");
							int idMessage = sc1.nextInt();

							if (idMessage > myMap.size() || idMessage <= 0) {
								System.out.println("Out of index");
							} else {
								//System.out.println(myMap.get(idMessage)); // For debugging
								try {
								dbA.modifyMessageById(myMap.get(idMessage));
								} catch (NullPointerException e) {
									System.out.println("No such message with that id");
								}
							} // end if
						} else {
							System.out.println("No messages");
						}
					} else if (choice.equals("0")) {
						System.out.print("Going back to menu");
						x = 0;
					} else {
						System.out.println("Messages refreshed");
					} // end if
				} catch (InputMismatchException e) {
					System.out.println("Invalid option");
				}
			}//end if
		} // end while
		dbA.dbClose();
	}

}
