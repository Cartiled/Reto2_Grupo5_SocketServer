package com.Reto2.RetoServer.Database.Entity;
// Generated 27 ene 2025, 16:10:05 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * Professor generated by hbm2java
 */
public class Professor implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 29662681295181430L;
	@Expose
	private int userId;

	@Expose
	private String name;

	private Client client;
	private Set<Assistant> assistants = new HashSet<Assistant>(0);
	private Set<Reunion> reunions = new HashSet<Reunion>(0);

	public Professor() {
	}

	public Professor(Client client) {
		this.client = client;
	}

	public Professor(Client client, Set<Assistant> assistants, Set<Reunion> reunions) {
		this.client = client;
		this.assistants = assistants;
		this.reunions = reunions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<Assistant> getAssistants() {
		return this.assistants;
	}

	public void setAssistants(Set<Assistant> assistants) {
		this.assistants = assistants;
	}

	public Set<Reunion> getReunions() {
		return this.reunions;
	}

	public void setReunions(Set<Reunion> reunions) {
		this.reunions = reunions;
	}

}