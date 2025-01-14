package com.Reto2.RetoServer.Database.Entity;

import java.util.Objects;

public class Student extends User {
	
	private int idCourse = 0;
	private int courseYear = 0;
	private boolean intensiveDual = false;

	public Student(String userId,String userName,String surName,String password,String email,int i, int j ,boolean b) {
		userId = this.getUserID();
		userName = this.getSurName();
		surName = this.getSurName();
		password = this.getPassWord();
		email = this.getEmail();
		this.idCourse = i;
		this.courseYear = j;
		this.intensiveDual = b;
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
		return Objects.hash(courseYear, idCourse, intensiveDual);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
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
