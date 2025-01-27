package com.Reto2.RetoServer.Database.Entity;
// Generated 27 ene 2025, 16:10:05 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Professor generated by hbm2java
 */
public class Professor implements java.io.Serializable {

	private int userId;
	private Client client;
	private Set assistants = new HashSet(0);
	private Set reunions = new HashSet(0);

	public Professor() {
	}

	public Professor(Client client) {
		this.client = client;
	}

	public Professor(Client client, Set assistants, Set reunions) {
		this.client = client;
		this.assistants = assistants;
		this.reunions = reunions;
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

	public Set getAssistants() {
		return this.assistants;
	}

	public void setAssistants(Set assistants) {
		this.assistants = assistants;
	}

	public Set getReunions() {
		return this.reunions;
	}

	public void setReunions(Set reunions) {
		this.reunions = reunions;
	}

}
