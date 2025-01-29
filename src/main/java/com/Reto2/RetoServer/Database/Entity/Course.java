package com.Reto2.RetoServer.Database.Entity;
// Generated 27 ene 2025, 16:10:05 by Hibernate Tools 6.5.1.Final

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * Course generated by hbm2java
 */
public class Course implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8675429549950810959L;
	@Expose
	private Integer courseId;
	@Expose
	private Schedule schedule;
	@Expose
	private String title;
	@Expose
	private String email;
	@Expose
	private String courseDescription;
	@Expose
	private Set subjects = new HashSet(0);
	@Expose
	private Set matriculations = new HashSet(0);
	@Expose
	private Set documentses = new HashSet(0);

	public Course() {
	}

	public Course(Schedule schedule, String title, String email) {
		this.schedule = schedule;
		this.title = title;
		this.email = email;
	}

	public Course(Schedule schedule, String title, String email, String courseDescription, Set subjects,
			Set matriculations, Set documentses) {
		this.schedule = schedule;
		this.title = title;
		this.email = email;
		this.courseDescription = courseDescription;
		this.subjects = subjects;
		this.matriculations = matriculations;
		this.documentses = documentses;
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

	public Set getDocumentses() {
		return this.documentses;
	}

	public void setDocumentses(Set documentses) {
		this.documentses = documentses;
	}

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", schedule=" + schedule + ", title=" + title + ", email=" + email
				+ ", courseDescription=" + courseDescription + ", subjects=" + subjects + ", matriculations="
				+ matriculations + ", documentses=" + documentses + "]";
	}
	

}
