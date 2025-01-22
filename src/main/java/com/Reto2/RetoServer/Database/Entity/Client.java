package com.Reto2.RetoServer.Database.Entity;
// Generated 17 ene 2025, 15:37:13 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * Client generated by hbm2java
 */


public class Client implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -686199008108205020L;
	 @Expose
	private Integer userId;
	 @Expose
	private String userName;
	 @Expose
	private String surname;
	 @Expose
	private String secondsurname;
	 @Expose
	private String pass;
	 @Expose
	private String dni;
	 @Expose
	private String direction;
	 @Expose
	private int telephone;
	 @Expose
	private Boolean userType;
	 @Expose
	private Boolean registered;
	 @Expose
	private Student student;
	 @Expose
	private Professor professor;
	 @Expose
	private Set<Schedule> schedules = new HashSet<>();

	public Client() {
	}

	public Client(String userName, String surname, String pass) {
		this.userName = userName;
		this.surname = surname;
		this.pass = pass;
	}

	public Client(String userName, String surname, String secondsurname,String pass,String dni,String direction,int telephone, boolean userType, boolean registered, Student student, Professor professor, Set<Schedule> schedules) {
		this.userName = userName;
		this.surname = surname;
		this.secondsurname = secondsurname;
		this.pass = pass;
		this.dni = dni;
		this.direction = direction;
		this.telephone = telephone;
		this.registered = registered;
		this.userType = userType;
		this.student = student;
		this.professor = professor;
		this.schedules = schedules;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	

	public Boolean getUserType() {
		return userType;
	}

	public void setUserType(Boolean userType) {
		this.userType = userType;
	}

	public Boolean getRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Professor getProfessor() {
		return this.professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public Set<Schedule>  getSchedules() {
		return this.schedules;
	}

	public void setSchedules(Set<Schedule> schedules) {
		this.schedules = schedules;
	}

	public String getSecondsurname() {
		return secondsurname;
	}

	public void setSecondsurname(String secondsurname) {
		this.secondsurname = secondsurname;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getTelephone() {
		return telephone;
	}

	public void setTelephone(int telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "Client [userId=" + userId + ", userName=" + userName + ", surname=" + surname + ", secondsurname="
				+ secondsurname + ", pass=" + pass + ", dni=" + dni + ", direction=" + direction + ", telephone="
				+ telephone + ", userType=" + userType + ", registered=" + registered + ", student=" + student
				+ ", professor=" + professor + ", schedules=" + schedules + "]";
	}

	


	

}