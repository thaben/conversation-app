package com.mypackage;

public abstract class User {
	private int id;
	private String password;
	private String username;
	private String email;
	protected String accessLevel;
	
	public User() {		
	}
	public User(int id,String password, String username, String email,String accessLevel) {
		super();
		this.id=id;
		this.password = password;
		this.username = username;
		this.email = email;
		this.accessLevel=accessLevel;
	}
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	protected void setEmail(String email) {
		this.email = email;
	}


	protected String getAccessLevel() {
		return accessLevel;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", username=" + username + ", email=" + email
				+ ", accessLevel=" + accessLevel + "]";
	}
	

}
