package com.Reto2.RetoServer.Database.Entity;

import java.util.Objects;

public abstract class User {
	
	private String userID = null;
	private String name = null;
	private String surName = null;
	private String passWord = null;
	private String email = null;
	
	public User(String userID,String name, String surName,String passWord,String email) {
		this.userID = userID;
		this.name = name;
		this.surName = surName;
		this.passWord = passWord;
		this.email = email;
	}
	
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		return Objects.hash(email, name, passWord, surName, userID);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(passWord, other.passWord) && Objects.equals(surName, other.surName)
				&& Objects.equals(userID, other.userID);
	}
	@Override
	public String toString() {
		return "User [userID=" + userID + ", name=" + name + ", surName=" + surName + ", passWord=" + passWord
				+ ", email=" + email + "]";
	}
	
	
}
