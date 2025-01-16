package com.Reto2.RetoServer.Database.Entity;
// Generated 16 ene 2025, 17:35:05 by Hibernate Tools 6.5.1.Final

/**
 * CourseId generated by hbm2java
 */
public class CourseId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int courseId;
	private int scheduleId;
	private int userId;

	public CourseId() {
	}

	public CourseId(int courseId, int scheduleId, int userId) {
		this.courseId = courseId;
		this.scheduleId = scheduleId;
		this.userId = userId;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CourseId))
			return false;
		CourseId castOther = (CourseId) other;

		return (this.getCourseId() == castOther.getCourseId()) && (this.getScheduleId() == castOther.getScheduleId())
				&& (this.getUserId() == castOther.getUserId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCourseId();
		result = 37 * result + this.getScheduleId();
		result = 37 * result + this.getUserId();
		return result;
	}

}
