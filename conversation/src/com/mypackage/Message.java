package com.mypackage;
//fortwma se lista oi xristes gia taxutita !

//import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

	
public class Message {
	private int id;
	private String text;
	private User sender; 
	private User receiver;
	private Timestamp dateTime;
	
	
	//First constructon when message is created and ID and Time is auto-generated from database
	public Message(String text, User sender, User receiver) { 
		this.text = text;
		this.sender = sender;
		this.receiver = receiver;
	}

	//When we know the created messages in the database and e.g want to load them in a list
	public Message(int id, int sender, int receiver, Timestamp dateTime, String text) throws SQLException { 
		DatabaseAccess a=new DatabaseAccess();
		a.dbConnection();
		this.id=id;
		this.text = text;
		this.sender = a.loadUserToObjectById(sender); 
		this.receiver = a.loadUserToObjectById(receiver);
		a.dbClose();
		this.dateTime = dateTime;
	}

	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public User getSender() {
		return sender;
	}


	public void setSender(User sender) {
		this.sender = sender;
	}


	public User getReceiver() {
		return receiver;
	}


	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}
	
	@Override
	public String toString() {
		return "Message [ sender=" + sender.getUsername() + "\t\t\t, receiver=" + receiver.getUsername() + "\t\t\t, timeStamp=" + dateTime
				+" text=" + text +"]";
	}


}
