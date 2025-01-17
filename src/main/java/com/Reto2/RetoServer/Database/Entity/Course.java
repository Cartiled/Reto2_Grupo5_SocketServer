package com.Reto2.RetoServer.Database.Entity;
// Generated 17 ene 2025, 15:37:13 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Course generated by hbm2java
 */
public class Course implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3837166687655056942L;
	private Integer courseId;
	private Schedule schedule;
	private String title;
	private String email;
	private String courseDescription;
	private Set subjects = new HashSet(0);
	private Set matriculations = new HashSet(0);

	public Course() {
	}

	public Course(Schedule schedule, String title, String email) {
		this.schedule = schedule;
		this.title = title;
		this.email = email;
	}

	public Course(Schedule schedule, String title, String email, String courseDescription, Set subjects,
			Set matriculations) {
		this.schedule = schedule;
		this.title = title;
		this.email = email;
		this.courseDescription = courseDescription;
		this.subjects = subjects;
		this.matriculations = matriculations;
	}

	public Integer getCourseId() {
		return this.courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Schedule getSchedule() {
		return this.schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCourseDescription() {
		return this.courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public Set getSubjects() {
		return this.subjects;
	}

	public void setSubjects(Set subjects) {
		this.subjects = subjects;
	}

	public Set getMatriculations() {
		return this.matriculations;
	}

	public void setMatriculations(Set matriculations) {
		this.matriculations = matriculations;
	}

}
