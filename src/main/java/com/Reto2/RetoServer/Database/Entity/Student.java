package com.Reto2.RetoServer.Database.Entity;

import java.util.Objects;

public class Student extends User  {
	
	private int idCourse = 0;
	private int courseYear = 0;
	private boolean intensiveDual = false;
	
	public Student(String userID, String name, String surName, String passWord, String email,int idCourse,int courseYear,boolean intensiveDual ) {
		super(userID, name, surName, passWord, email);
		this.idCourse = idCourse;
		this.courseYear = courseYear;
		this.intensiveDual = intensiveDual;
		
	}

	public int getIdCourse() {
		return idCourse;
	}

	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}

	public int getCourseYear() {
		return courseYear;
	}

	public void setCourseYear(int courseYear) {
		this.courseYear = courseYear;
	}

	public boolean isIntensiveDual() {
		return intensiveDual;
	}

	public void setIntensiveDual(boolean intensiveDual) {
		this.intensiveDual = intensiveDual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(courseYear, idCourse, intensiveDual);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return courseYear == other.courseYear && idCourse == other.idCourse && intensiveDual == other.intensiveDual;
	}

	@Override
	public String toString() {
		return "Student [idCourse=" + idCourse + ", courseYear=" + courseYear + ", intensiveDual=" + intensiveDual
				+ "]";
	}
	
	

	
}
