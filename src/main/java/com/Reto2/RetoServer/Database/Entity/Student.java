package com.Reto2.RetoServer.Database.Entity;
// Generated 27 ene 2025, 16:10:05 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Student generated by hbm2java
 */
public class Student implements java.io.Serializable {

	private int userId;
	private Client client;
	private char userYear;
	private boolean intensiveDual;
	private Set matriculations = new HashSet(0);

	public Student() {
	}

	public Student(Client client, char userYear, boolean intensiveDual) {
		this.client = client;
		this.userYear = userYear;
		this.intensiveDual = intensiveDual;
	}

	public Student(Client client, char userYear, boolean intensiveDual, Set matriculations) {
		this.client = client;
		this.userYear = userYear;
		this.intensiveDual = intensiveDual;
		this.matriculations = matriculations;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public char getUserYear() {
		return this.userYear;
	}

	public void setUserYear(char userYear) {
		this.userYear = userYear;
	}

	public boolean isIntensiveDual() {
		return this.intensiveDual;
	}

	public void setIntensiveDual(boolean intensiveDual) {
		this.intensiveDual = intensiveDual;
	}

	public Set getMatriculations() {
		return this.matriculations;
	}

	public void setMatriculations(Set matriculations) {
		this.matriculations = matriculations;
	}

}
