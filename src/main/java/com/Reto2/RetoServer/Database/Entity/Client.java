package com.Reto2.RetoServer.Database.Entity;
// Generated 17 ene 2025, 15:37:13 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Client generated by hbm2java
 */


public class Client implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	private String userName;
	private String surname;
	private String pass;
	private Student student;
	private Professor professor;
	private Set schedules = new HashSet(0);

	public Client() {
	}

	public Client(String userName, String surname, String pass) {
		this.userName = userName;
		this.surname = surname;
		this.pass = pass;
	}

	public Client(String userName, String surname, String pass, Student student, Professor professor, Set schedules) {
		this.userName = userName;
		this.surname = surname;
		this.pass = pass;
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

	public Set getSchedules() {
		return this.schedules;
	}

	public void setSchedules(Set schedules) {
		this.schedules = schedules;
	}

	@Override
	public String toString() {
		return "Client [userId=" + userId + ", userName=" + userName + ", surname=" + surname + ", pass=" + pass
				+ ", student=" + student + ", professor=" + professor + ", schedules=" + schedules + "]";
	}

}
