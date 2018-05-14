package com.mypackage;

import java.sql.*;
import java.util.logging.Logger;

import com.encryption.EncryptPassword;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;


public class DatabaseAccess {
	
	private static final String USERNAME = "root";
    private static final String PASS = "asdf";
    private static final String MYSQLURL = "jdbc:mysql://localhost/conversation_app5?autoReconnect=true&useSSL=false";
    private static final String MYSQLURL2 = "jdbc:mysql://localhost/?autoReconnect=true&useSSL=false";
   
    
   
    private static Scanner sc = new Scanner(System.in);
    private static String choice=null;
    
    private Connection con=null;
    private Statement stmt=null;
    private PreparedStatement pstmt=null;
    private ResultSet rs=null;
      
	
	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
	
	
	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement st) {
		this.stmt = st;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
		
	public String getChoice() {
		return choice;
	}
	
	public void dbConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(MYSQLURL, USERNAME, PASS);
			setCon(con);

			if (this.getCon() != null) {
				System.out.println("Database connection established");
			}
		} catch (SQLException e) {
			System.out.println("SQL exception caught during connection with db \n");
			Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dbConnectionForLoginDatabaseCreation() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(MYSQLURL2, USERNAME, PASS);
			setCon(con);

			if (this.getCon() != null) {
				System.out.println("Database connection established");
			}
		} catch (SQLException e) {
			System.out.println("SQL exception caught during connection with db \n");
			Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//better to handle here exceptions rather other places because i will have to repeat it many times
	public void dbClose() {
		//Check which type of statement has  been used, preparedStatement or simpleStatement and .close() it accordingly to the method being invoked	
		if(this.getRs()!=null) {
			try {
				this.getRs().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.getPstmt() != null) {
			try {
				this.getPstmt().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (this.getStmt() != null) {
			try {
				this.getStmt().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			this.getCon().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connection closed");
	}


	public boolean login(String username, String password) {
		boolean status = false;
		try {
			String query = "SELECT * FROM users";
			this.dbConnection();
			this.setStmt(con.createStatement());
			this.setRs(getStmt().executeQuery(query));
			while (rs.next()) {
				if (rs.getString(2).equals(username.toLowerCase()) && rs.getString(3).equals(password)) {
					System.out.println("You have succesfully login");
					setLoginUserObjectData(rs); // load database user data to user object
					status = true;
					break;
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			this.dbClose();
		}
		return status;
	}
	
	// Sets database data to the users access level accordingly and access to menus !!
	private void setLoginUserObjectData(ResultSet rs) throws SQLException {
		if (rs.getString(5).toLowerCase().equals("admin")) {
			Admin admin = new Admin(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			MenuMainApplication.printMenuAdmin(admin);

		} else if (rs.getString(5).toLowerCase().equals("simple_user")) {
			SimpleUser suser = new SimpleUser(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			MenuMainApplication.printMenuSimpleUser(suser);

		} else if (rs.getString(5).toLowerCase().equals("s_moderator")) {
			SuperModerator superModerator = new SuperModerator(rs.getInt(1), rs.getString(3), rs.getString(2),rs.getString(4),rs.getString(5));

			MenuMainApplication.printMenuSuperModerator(superModerator);

		} else if (rs.getString(5).toLowerCase().equals("moderator")) {
			Moderator moderator = new Moderator(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			MenuMainApplication.printMenuModerator(moderator);
		}
	}
	
	
	
	/***************************************************************
	 * 					ADMIN METHODS					           *
	 ***************************************************************/

	public  void setPermissions(int id) {
		int x=1;
		System.out.println("What kind of permission rights do you want to give,press\n"
							+ " 1 for Simple User     (Read),\n"
							+ " 2 for Moderator       (Read & Edit),\n"
							+ " 3 for Super Moderator (Read & Edit & Delete)");
		
		String alvlString = "simple_user";
		do {
			String alvl = sc.nextLine();
			if ( alvl.equals("1") || alvl.equals("2") || alvl.equals("3")) {
				if (alvl.equals("1")) {
					alvlString = "simple_user";
				} else if (alvl.equals("2")) {
					alvlString = "moderator";
				} else if (alvl.equals("3")) {
					alvlString = "s_moderator";
				}
				x = 0;
			} else {
				System.out.println("Wrong option type again");
			}
		} while (x != 0);
		
		String query="UPDATE users "
					+"SET access_level = '"+alvlString+"' "
					+"WHERE id="+id+";";
		try {
			this.setStmt(con.createStatement());
			this.getStmt().executeUpdate(query);
			this.fetchUsersForAdmin();
			// logs
			String text = String.format("Timestamp=%15s Username %15s changed his permissions to %8s  ",
					new Date().toString(), convertIdToUsername(id), alvlString);
			FileAccess.printAdminAction(text);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void createUser(String username, String password) { 

		String query = "INSERT INTO users (username,password) "
					  + "VALUES (?,?);";
		this.dbConnection();
		try {
			if(!this.isDublicateUsername(username.toLowerCase())) { //check for duplicate user name returns boolean
					
				PreparedStatement createSt = con.prepareStatement(query);
				createSt.setString(1, username.toLowerCase());
				createSt.setString(2, password);
				createSt.executeUpdate();
				System.out.println("User Created");
				setPermissions(convertUsernameToId(username)); // convert username to database id to set the primary key
										// because it is auto-increment
				// logs
				String text = String.format("Timestamp=%15s Username %15s created   ",new Date().toString(),convertUsernameToId(username));
				FileAccess.printAdminAction(text);
			} else {
				System.out.println("That username already exists!!!");
			}
			this.dbClose();
		} catch (SQLException e) {
			System.out.println("Cannot have duplicate username in database");
		}

	}
	
	public void deleteUser(int id) {
		
		String query = "DELETE FROM users "  
					  +"WHERE id = ?;";

		try {
			String username=convertIdToUsername(id);	
			setPstmt(con.prepareStatement(query)); 
			getPstmt().setInt(1, id);
			getPstmt().executeUpdate();
			System.out.println("User Deleted");
			this.fetchUsersForAdmin();

			// logs
			String text=String.format("Timestamp=%15s Username: %s deleted   ",new Date().toString(), username);
			FileAccess.printAdminAction(text);

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public void updateUser(int id) {
		String updateInfo=null; //scanner for password and email	
		
		try {
			setPstmt(con.prepareStatement(this.updateUserHelper())); // this.updateUserHelper() is a query
			if (choice.equals("1")) {
				String username = Login.username().toLowerCase();
				if (!this.isDublicateUsername(username)) {
					getPstmt().setString(1, username);
					getPstmt().setInt(2, id); 
					getPstmt().executeUpdate();
					System.out.println("Username Updated");

				} else {
					System.out.println("That user already exists");
				}

			} else if (choice.equals("2")) {

				updateInfo = Login.password(); // scanner for password and email and username
				if (choice.equals("2")) { // encrypt if choice is for password
					updateInfo = new EncryptPassword().encryptPassword(updateInfo);
					System.out.println(updateInfo);
				}
				getPstmt().setString(1, updateInfo);
				getPstmt().setInt(2, id);
				getPstmt().executeUpdate();
				System.out.println("Password Updated");

			} else if (choice.equals("3")) {
				updateInfo = Login.email();
				getPstmt().setString(1, updateInfo);
				getPstmt().setInt(2, id);
				getPstmt().executeUpdate();
				System.out.println("email Updated");
			} else {
				System.out.println("Wrong option");
			} // end second if

			this.fetchUsersForAdmin();

		} catch (SQLException | NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("Invalid Option");
		}

	}
	
	// Method for helping updateUser() method 
	private String updateUserHelper() {
		String query=null;
		choice=sc.nextLine();
		if(choice.equals("1")) {
			 query = "UPDATE users "
					+"SET username = ?"
					+"WHERE id=?;";
			System.out.println("Give new username");
		}else if(choice.equals("2")) {
			query = "UPDATE users "
					+"SET password = ?"
					+"WHERE id=?;";
			System.out.println("Give new password");
		}else if(choice.equals("3")) {
			query = "UPDATE users "
					+"SET email = ?"
					+"WHERE id=?;";
			System.out.println("Give new email");
		}
		return query;
	}
	
	
	/***************************************************************
	 * 					END OF ADMIN METHODS					   *
	 ***************************************************************/
	
	
	
	/***************************************************************
	 * 					Simple User METHODS					       *
	 ***************************************************************/
	

	public void startConversation(Conversation c) {
		
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		
		String query = "INSERT INTO conversation (title,id_creator,id_receiver) " 
					 + "VALUES (?,?,?);";
	
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setString(1, c.getTitle());
			pstmt.setInt(2, c.getSender().getId());
			pstmt.setInt(3, c.getReceiver().getId());
			pstmt.executeUpdate();
			System.out.println("Conversation Created");
			System.out.println("Send your first message:");
			String text = sc.nextLine();
			c = loadConversationToObject(getConversationId(c.getSender())); // new conversation gets id and time because
																			// they are auto-generated from the database
			sendMessage(c, c.getSender(), c.getReceiver(), text);
			HashMap<Integer, Integer> myMap = viewConversationMessagesReturnHashMap(c, c.getSender(), false);
			new MenuMessages().viewMenuMessages(c, c.getSender(), myMap); // go back to menu
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Problem with startConversation()");
		}

	}
	
	public void sendMessage(Conversation conv,User messageSender, User messageReceiver, String text) { //procedure conversation_messages , to messages
		int idMessage = 0;
		
		String query = "INSERT INTO messages (id_sender,id_receiver,content) "  
				 	 + "VALUES (?,?,?);";
		String query2 = "INSERT INTO conversation_messages (id_conversation,id_message) "  
				     + "VALUES (?,?);";
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, messageSender.getId());
			pstmt.setInt(2, messageReceiver.getId());
			pstmt.setString(3, text);
			pstmt.executeUpdate();
			System.out.println("Message Sent");

			idMessage=getLastMessageId(messageSender); // getLast created message ID from database by sender
			// because it is auto-generated from database ,

			this.setPstmt(con.prepareStatement(query2));
			pstmt.setInt(1, conv.getId());
			pstmt.setInt(2, idMessage); // getMessageID so i can input it in conversation_messages table
			pstmt.executeUpdate();
			System.out.println("Table conversation_messages updated in sql");
			// getLastMessageObjectById
			Message message = getLastMessageObject(messageSender);
			// write log
			FileAccess.fileWriterAccess(conv, message);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	
	private int getLastMessageId(User user) {
		int a = 0;
		String query = "SELECT idmessage "  
				 	 + "FROM messages "
				 	 +"WHERE datetime_created IN (SELECT max(datetime_created) FROM messages) AND id_sender=?;";		
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, user.getId());
			rs = pstmt.executeQuery();
			rs.next();
			a = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	private Message getLastMessageObject(User userSender) {
		Message m=null;
		String query = "SELECT * "  
				 	 + "FROM messages "
				 	 +"WHERE datetime_created IN (SELECT max(datetime_created) FROM messages) AND id_sender=?;";		
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, userSender.getId());
			ResultSet rs1 = pstmt.executeQuery();
			rs1.next();
			m = new Message(rs1.getInt(1), rs1.getInt(2), rs1.getInt(3), rs1.getTimestamp(4), rs1.getString(5));
			//System.out.println(m); for debugging
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return m;
	}
			
	
	public void viewConversations(User sender) {
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		int i =1;
		String query="SELECT idconversation, id_creator, id_receiver, title, datetime_created " + 
				"FROM conversation " + 
				"INNER join users ON conversation.id_creator=users.id OR conversation.id_receiver=users.id " + 
				"WHERE users.id=?;";
			
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, sender.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				myMap.put(i, rs.getInt(1));
				System.out.format("Conversation id: %d\t Created by: %15s\t Receiver: %15s\t %20s With title: %s  \t\t  \n", i,convertIdToUsername(rs.getInt(2)),convertIdToUsername(rs.getInt(3)),rs.getString(5),rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewConversationMenu(sender,myMap,1);
	}// end of method
	
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
					DatabaseAccess dbA = new DatabaseAccess();
					dbA.dbConnection();
					conv = dbA.loadConversationToObject(myMap.get(idConversation)); 
					if (choice == 1) {
						new MenuMessages().viewMenuMessages(conv, sender, myMap);
					} else {
						new MenuMessagesPrivileges().viewMenuMessagesPrivileges(conv, sender, myMap); // sender here is
																										// editor
					}
					dbA.dbClose();
				} // end if
			} catch (InputMismatchException e) {
				System.out.println("Wrong input");
			}
		} // end if

	}
	
	
	
	
	
	
	
	

	// True print Messages , false do not print them
	public HashMap<Integer, Integer> viewConversationMessagesReturnHashMap(Conversation conv, User user, Boolean bool) {
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		String query;
		int choice = 0;
		int i = 1;// hashMap

		if (user.getClass().getSimpleName().equals("SimpleUser")) { // simple user cannot have access to message id
			choice = 1;
		}
		query = "SELECT messages.content, messages.idmessage, messages.id_sender, messages.datetime_created "+
				"FROM conversation_messages "+
				"INNER JOIN conversation ON conversation_messages.id_conversation = conversation.idconversation "+
				"INNER JOIN messages ON messages.idmessage = conversation_messages.id_message "+
				"WHERE conversation.idconversation=? ";
		try {

			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, conv.getId());
			pstmt.executeQuery();
			this.setRs(pstmt.executeQuery());
			while (rs.next()) {
				if (choice == 1) {
					if(bool==true) {
					System.out.format("Sent by: %10s \t DateTime:%12s Text: %s\n",convertIdToUsername(rs.getInt(3)),rs.getString(4), rs.getString(1));
					}
				} else {				
					myMap.put(i, rs.getInt(2));
					if(bool==true) {
						System.out.format("Message ID:%2d Sent by: %10s \t DateTime:%12s Text: %s\n",i,convertIdToUsername(rs.getInt(3)),rs.getString(4), rs.getString(1));															// print sender and receiver
					}

					i++;
				} // end if
			} // end while
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myMap;
	}


	/***************************************************************
	 * 				 Moderator user Methods						   *
	 ***************************************************************/

	public void modifyMessageById(int idMessage) { 
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		String query = "UPDATE messages "+ 
					   "SET content=? " + 
					   "WHERE idmessage=? ;";

		try {

			System.out.println("Insert new text to edit message up to 250 characters");
			String text = sc.nextLine();
			if (text.length() <= 250) {
				this.setPstmt(con.prepareStatement(query));
				pstmt.setString(1, text);
				pstmt.setInt(2, idMessage); 
				pstmt.executeUpdate();
			} else {
				System.out.println("You typed more than 250 characters");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void viewConversationsEveryone(User editor) {
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		int i =1;
		String query="SELECT idconversation, id_creator, id_receiver, title, datetime_created " + 
				"FROM conversation " + 
				"INNER join users ON conversation.id_creator=users.id OR conversation.id_receiver=users.id ;";
		try {
			this.setPstmt(con.prepareStatement(query));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				myMap.put(i, rs.getInt(1));
				System.out.format("Conversation id: %d\t Created by: %15s\t Receiver: %15s\t %20s With title: %s  \t\t  \n", i,convertIdToUsername(rs.getInt(2)),convertIdToUsername(rs.getInt(3)),rs.getString(5),rs.getString(4));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(editor); for debugging
		viewConversationMenu(editor,myMap,2);
		
	}
	
	
	
	
	
	public HashMap<Integer, Integer> viewConversationMessagesReturnHashMapPrivileges(Conversation conv, User user, Boolean bool) {
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		String query;
		int i = 1;// for hashMap

		
		query = "SELECT messages.content, messages.idmessage, messages.id_sender, messages.datetime_created "+
				"FROM conversation_messages "+
				"INNER JOIN conversation ON conversation_messages.id_conversation = conversation.idconversation "+
				"INNER JOIN messages ON messages.idmessage = conversation_messages.id_message "+
				"WHERE conversation.idconversation=? ";
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, conv.getId());
			pstmt.executeQuery();
			this.setRs(pstmt.executeQuery());
			while (rs.next()) {
				myMap.put(i, rs.getInt(2));
				if (bool == true) {
					System.out.format("Message ID:%2d Sent by: %10s \t DateTime:%12s Text: %s\n", i,
							convertIdToUsername(rs.getInt(3)), rs.getString(4), rs.getString(1)); // print sender and
																									// receiver
				}
				i++;
				// end if
			} // end while
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myMap;
	}

	/***************************************************************
	 * 				Super	Moderator Methods					       *
	 ***************************************************************/
	
	
	public void deleteMessageById(int id) { //when a message is deleted it also must be deleted from  a file log ?? 
		String query = "DELETE FROM messages " + 
					   	"WHERE idmessage=? ;";

	//	this.dbConnection();
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, id);
			pstmt.executeUpdate(); 
			System.out.println("Message deleted");
			//		this.dbClose();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/***************************************************************
	 * 					Help methods                               *
	 ***************************************************************/
	
	public Conversation loadConversationToObject(int idConversation) {  
		Conversation conv=null;
		
		String query = "SELECT * "+
		   	   	   	   "FROM conversation "+
		   	   	   	   "WHERE idconversation=?";

		try {
			System.out.println(idConversation);
			PreparedStatement pstmt = this.con.prepareStatement(query);
			pstmt.setInt(1, idConversation);
			ResultSet rs2 = pstmt.executeQuery();
			rs2.next();
			User creator = loadUserToObjectByUsername(this.convertIdToUsername(rs2.getInt(2)));
			User receiver = loadUserToObjectByUsername(this.convertIdToUsername(rs2.getInt(3)));
			conv = new Conversation(idConversation, creator, receiver, rs2.getString(4), rs2.getTimestamp(6)); /// maybe constr() arraylist																												
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conv;
	}

	//Help method because class Message is aggregated as a list instance variable for conversation class
	public ArrayList<Message> loadMessagesToListByConversation(int idConversation) {
		ArrayList<Message> messageList=new ArrayList<Message>();
		
		String query = "SELECT message.content " +
					   "FROM conversation_messages" + 
					   "INNER JOIN messages ON messages.idmessage=conversation.messages.id_message "+
					   "WHERE id=?";
		
		this.dbConnection();
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, idConversation);
			this.setRs(pstmt.executeQuery());		
			while (rs.next()) {
				messageList.add(new Message(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getTimestamp(4),rs.getString(5)));
			
			}
			this.dbClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messageList;
	}

	public int convertUsernameToId(String username) {
		int id = 0;
		String query = "SELECT * FROM users " + "WHERE username='" + username + "';";
		try {
			this.setStmt(con.createStatement());
			this.setRs(getStmt().executeQuery(query));
			rs.next();
			id = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}


	public String convertIdToUsername(int id) throws SQLException {
		String username;
		String query = "SELECT username FROM users " + "WHERE id=" + id + "; ";
		this.setStmt(con.createStatement());
		ResultSet rs1 = (this.getStmt().executeQuery(query)); // SKIPPING CURSOR
		rs1.next();
		username = rs1.getString(1);
		rs1.close();
		return username;
	}

	// Get users that are non admin
	public HashMap<Integer, Integer> fetchUsersForAdmin() {
		int i = 1;// for hashmap
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();

		String query="SELECT * FROM users "
					+"WHERE access_level != 'admin' OR access_level IS null;"; // need " IS null " because SELECT * does not get them otherwise
		try {
			this.setStmt(con.createStatement());
			this.setRs(getStmt().executeQuery(query));
			while (rs.next()) {
				myMap.put(i, rs.getInt(1));
				System.out.format("%4d Username:%10s\t Access Level:%s\n", i, rs.getString(2), rs.getString(5));
				i++;// for hashmap
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myMap;
	}

	
	public ArrayList<User> getUserListAndPrintThem(String username) { // print with hashMap for id
		int i = 1;
		ArrayList<User> listusers = new ArrayList<User>();
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();

		String query ="SELECT * FROM users "
					+"WHERE username !='"+username+"';";	
		try {
			this.setStmt(con.createStatement());
			this.setRs(getStmt().executeQuery(query));
			while (rs.next()) {
				myMap.put(i, rs.getInt(1));
				System.out.println(i + "    " + rs.getString(2) + " " + rs.getString(5));
				listusers.add(setUserObjectData(rs)); // Help method below to load each user object to list
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listusers;
	}
	
	public HashMap<Integer, Integer> getUserHashMapAndPrintThem(String username) { // print with hashMap for id
		int i = 1;
		HashMap<Integer, Integer> myMap = new HashMap<Integer, Integer>();

		String query ="SELECT * FROM users "
					+"WHERE username !='"+username+"';";	
		try {
			this.setStmt(con.createStatement());
			this.setRs(getStmt().executeQuery(query));
			while (rs.next()) {
				myMap.put(i, rs.getInt(1));
				System.out.println(i + "    " + rs.getString(2) + " " + rs.getString(5));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myMap;
	}
	
	private User setUserObjectData(ResultSet rs) throws SQLException {
		User user = null;
		if (rs.getString(5).toLowerCase().equals("admin")) {
			User admin = new Admin(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			user = admin;

		} else if (rs.getString(5).toLowerCase().equals("simple_user")) {
			User suser = new SimpleUser(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			user = suser;
		} else if (rs.getString(5).toLowerCase().equals("s_moderator")) {
			User superModerator = new SuperModerator(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			user = superModerator;

		} else if (rs.getString(5).toLowerCase().equals("moderator")) {
			User moderator = new Moderator(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4),rs.getString(5));
			user = moderator;
		}

		return user;
	}
	
	
	// For creation & updating user, check duplicates
	public boolean isDublicateUsername(String username) throws SQLException {
		boolean flag = false;
		String query = "SELECT username FROM users " +
					   "WHERE username='"+username+"';";
		this.setStmt(con.createStatement());
		ResultSet rs = getStmt().executeQuery(query);
		if (rs.next()) {
			if (rs.getString(1) == username) {
				flag = true;
			}
		}
		rs.close();
		// this.dbClose();
		return flag;
	}
	
	public User loadUserToObjectByUsername(String username){
		User user=null;
		String query="SELECT * FROM users "
					+"WHERE username='"+username+"';";

		try {
			setStmt(con.createStatement());
			setRs(getStmt().executeQuery(query));
			getRs().next();
			user = setUserObjectData(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public User loadUserToObjectById(int id){
		User user = null;
		String query="SELECT * "+
					 "FROM users "+
					 "WHERE id=?;";
		try {
			this.pstmt = con.prepareStatement(query);
			this.pstmt.setInt(1, id);
			ResultSet rs = this.pstmt.executeQuery();
			rs.next();
			user = setUserObjectData(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public int getConversationId(User user) {
		int a = 0;
		String query = "SELECT idconversation " + 
				"FROM conversation " + 
				"WHERE id_creator=? " + 
				"GROUP BY idconversation " + 
				"ORDER BY datetime_created  DESC LIMIT 1;";		
		try {
			this.setPstmt(con.prepareStatement(query));
			pstmt.setInt(1, user.getId());
			rs = pstmt.executeQuery();
			rs.next();
			a = rs.getInt(1);
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return a;
	}
	
	
	public boolean firstDBcreationFlag() {
		boolean flag=true;
		String query = "SELECT * " + 
					  "FROM conversation_app5.db_creation;";		
		try {
			stmt=(con.createStatement());
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			if(rs.getBoolean(1)==true) {
				flag=false;
			}
		} catch (SQLException e) {
		
		}
		return flag;
	}
	
	public  void firstDBcreation(Boolean bool) {
		String query = "UPDATE db_creation " + 
					   "SET created=? ; ";		
		try {
			pstmt=(con.prepareStatement(query));
			pstmt.setBoolean(1, bool);
			pstmt.executeUpdate();	
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
}



