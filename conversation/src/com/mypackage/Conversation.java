package com.mypackage;

import java.sql.Timestamp;



public class Conversation {
	
	private int id;
	private String title;
	private User sender;
	private User receiver;
	private Timestamp dateTime ;
	
	
	public Conversation(String title,User sender,User receiver) { // First constructor for upon creation of a convesation , id is created auto incremented from databse as well as date
		this.sender = sender;
		this.receiver = receiver;
		this.title=title;
	}
	
	public Conversation(int id,User sender,User receiver,String title,Timestamp dateTime) { // Second constructor  when we open an existing conversation so all data is already created from database and known
		this.id=id;
		this.sender = sender;
		this.receiver = receiver;
		this.title=title;
		this.dateTime=dateTime;
	}
	


	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User user) {
		this.sender = user;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User user) {
		this.receiver = user;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "Conversation [id=" + id + ", title=" + title + ", sender=" + sender + ", receiver=" + receiver
				+ ", dateTime=" + dateTime + "]";
	}
	
	
	
	
}
