package com.Reto2.RetoServer.Config;

public enum Events {

	ON_LOGIN ("onLogin"),
	ON_GET_ALL ("onGetAll"),
    ON_LOGOUT ("onLogout"),
    ON_LOGIN_ANSWER ("onLoginAnswer"),
	ON_GET_ALL_ANSWER ("onGetAllAnswer"),
	ON_LOGIN_SUCCESS("onLoginSuccess"),
	ON_LOGIN_FAIL("onLoginFail"),
	ON_FILTER_BY_COURSE("onFilterByCourse"),
    ON_FILTER_BY_CYCLE("onFilterByCycle"),
    ON_FILTER_BY_SUBJECT("onFilterBySubject"),
    ON_FILTER_BY_COURSE_RESPONSE("onFilterByCourseResponse"),
    ON_FILTER_BY_CYCLE_RESPONSE("onFilterByCycleResponse"),
    ON_FILTER_BY_SUBJECT_RESPONSE("onFilterBySubjectResponse"),
    ON_FILTER_ERROR("onFilterError"),
	ON_LOGIN_STUDENT("onLoginStudent"),
	ON_LOGIN_PROFESSOR("onLoginProfessor"),
	ON_REGISTER("onRegister"),
	ON_REGISTER_ANSWER("onRegisterAnswer"),
	ON_REGISTER_SUCCESS("onRegisterSuccess"),
	ON_REGISTER_FAIL("onRegisterFall"),
	ON_REGISTER_SAME_PASSWORD("onRegisterSamePassword"),
	ON_GET_EXTERNAL_COURSES_ANSWER("onExternalCourseAnswer"),
    ON_GET_EXTERNAL_COURSES_ERROR("onExternalCourseError"),
    ON_GET_EXTERNAL_COURSES("onExternalCourse"),
    ON_CHANGE_PASSWORD("onChangePassword"),
    ON_CHANGE_PASSWORD_FAIL("onChangePasswordFail"),
    ON_CHANGE_PASSWORD_ANSWER("onChangePasswordAnswer"),
    ON_GET_REUNIONS_ANSWER("onGetReunionsAnswer"),
    ON_GET_REUNIONS_ERROR("onGetReunionsError"),
    ON_GET_REUNIONS("onGetReunions"),
    ON_CREATE_REUNION("onCreateReunion"),
    ON_CREATE_REUNION_ANSWER("onCreateReunionAnswer"),
    ON_CREATE_REUNION_ERROR("onCreateReunionError"),
    ON_ACCEPT_REUNION("onAcceptReunion"),
    ON_ACCEPT_REUNION_ANSWER("onAcceptReunionAnswer"),
    ON_ACCEPT_REUNION_ERROR("onAcceptReunionError"),
    ON_REJECT_REUNION("onRejectReunion"),
    ON_REJECT_REUNION_ANSWER("onRejectReunionAnswer"),
    ON_REJECT_REUNION_ERROR("onRejectReunionError"),
    ON_FORCE_REUNION("onForceReunion"),
    ON_FORCE_REUNION_ANSWER("onForceReunionAnswer"),
    ON_FORCE_REUNION_ERROR("onForceReunionError"),
    ON_FORGOT_PASSWORD("onForgotPassword"),
    ON_FORGOT_PASSWORD_ANSWER("onForgotPasswordAnswer"),
    ON_FORGOT_PASSWORD_ERROR("onForgotPasswordError");
	
	
	public final String value;

	private Events(String value) {
		this.value = value;
	}
}	