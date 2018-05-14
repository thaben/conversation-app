package com.mypackage;

public class Moderator extends SimpleUser {
	
	
	public Moderator(int id, String password, String username, String email,String accessLevel) {
		super(id,password, username, email, accessLevel);
		// TODO Auto-generated constructor stub
	}

	public Moderator() {
		super();
	}
	
	public void viewConversationsEveryone(User user) {
		DatabaseAccess dbA=new DatabaseAccess();
		dbA.dbConnection();
		dbA.viewConversationsEveryone(user);;
		dbA.dbClose();
	}
	

}
