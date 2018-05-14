package com.mypackage;

import java.util.Scanner;

public class MenuMainApplication {

	private static Scanner sc = new Scanner(System.in);
	private static int choice = -1;
	
	
	/***************************************************************
	 * 					ADMIN MENU                                 *
	 ***************************************************************/
	
	private static void printMenuAdminFirst(Admin user) {
		System.out.println("Welcome admin " + user.getUsername());
		System.out.println("Here are your options\n"+
							"1)Change permissions for user\n"+
							"2)Crete a new user\n"+
							"3)Delete an existing user\n"+
							"4)Update an user\n"+
							"5)Start new conversation\n"+
							"6)View my conversations\n"+ // inside options to delete or update messages of topic
							"7)View everyones conversations\n"+
							"0)Logout\n");
	}
	
	public static void printMenuAdmin(Admin user) {
		do {		
			printMenuAdminFirst(user);
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Please insert numbers!!");
			}
					
			switch (choice) {
			case 1:
				user.setPermissions();
				break;

			case 2:	
				user.createUser();
				break;
				
			case 3:
				user.deleteUser();
				break;
				
			case 4:		
				user.updateUser();
				break;
			case 5:
				user.startConversation(user);
				System.out.print("End of start of conversation\n");
				break;
			case 6:
				user.viewConversations(user);
				System.out.print("End of view conversations\n");
				break;
			case 7:
				user.viewConversationsEveryone(user);
				System.out.print("End of view conversations everyone\n");
				break;
			case 0:
				choice = 0; //out of while
				System.out.println("Log out......");
				break;
				
			default:
				System.out.println("No such choice");
			}//end of switch
			
		} while (choice != 0);
	}
	
	/***************************************************************
	 * 					Simple User MENU                           *
	 ***************************************************************/
	
	public static void printMenuSimpleUser(SimpleUser user) {

		do {
			System.out.println("Welcome "+user.getUsername()+" Access Level:Simple user");
			System.out.println("Here are your options\n"+
								"1)Start new conversation\n"+
								"2)View my conversations\n"+
								//"3)View unresponded conversations"+ //
								"0)Logout");

			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Please insert numbers!!");
			}
			switch (choice) {
			case 1:
				user.startConversation(user);

				break;
			case 2:
				user.viewConversations(user);
				System.out.print("End of view conversations\n");
				break;
			case 3:
				// user.viewUnrespondedConversations(user);
				// System.out.print("End of View unresponded conversations\n"); //not implemented
				break;
			case 0:
				choice = 0;
				System.out.println("Log out......");
				break;
			default:
				System.out.println("No such choice");
			}
		} while (choice != 0);
		
	}



	
/***************************************************************
* 					Moderator MENU                             *
***************************************************************/

	public static void printMenuModerator(Moderator user) {

		do {
		System.out.println("Welcome "+user.getUsername()+" Access Level:Moderator");
		System.out.println("Here are your options\n"+
							"1)Start new conversation\n"+
							"2)View conversations\n"+
							"3)View  everyones conversations\n"+
							//"3)View unresponded conversations"+ //not implemented
							"0)Logout");

			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Please insert numbers!!");
			}
			switch (choice) {
			case 1:
				user.startConversation(user);

				break;
			case 2:
				user.viewConversations(user);
				System.out.print("View conversations end\n");
				break;
			case 3:
				user.viewConversationsEveryone(user);
				System.out.print("End of view conversations everyone\n");
				break;
		//	case 3:
				//user.viewUnrespondedConversations(user);
				//System.out.print("View unresponded conversations\n"); //not implemented
			//	break;
			case 0:
				choice = 0;
				System.out.println("Log out......");
				break;
			default:
				System.out.println("No such choice");
			}
		} while (choice != 0);

	}

/***************************************************************
* 					Super Moderator  MENU                      *
***************************************************************/

	public static void printMenuSuperModerator(SuperModerator user) {

		do {
		System.out.println("Welcome "+user.getUsername()+" Access Level:Super Moderator");
		System.out.println("Here are your options\n"+
							"1)Start new conversation\n"+
							"2)View conversations\n"+
							"3)View  everyones conversations\n"+
							//"3)View unresponded conversations"+ //not implemented
							"0)Logout");

			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Please insert numbers!!");
			}
			switch (choice) {
			case 1:
				user.startConversation(user);
				break;
			case 2:
				user.viewConversations(user);

				break;
				
			case 3:
				user.viewConversationsEveryone(user);
				System.out.print("End of view conversations everyone\n");
				break;
		//	case 3:
			//not implemented
				//user.viewUnrespondedConversations(user);
			//	break;
			case 0:
				choice = 0;
				System.out.println("Log out......");
				break;
			default:
				System.out.println("No such choice");
			}
		} while (choice != 0);

	}

}

