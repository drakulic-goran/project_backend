package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class TeacherDto {
	
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="First name is not valid, can contain only letters and minimum is 2 letter.")
	private String firstName;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[A-Za-z]{2,}$", message="Last name is not valid, can contain only letters and minimum is 2 letter.")
	private String lastName;
	@JsonView(Views.Admin.class)
	@Pattern(regexp = "^[0-9]{13,13}$", message="JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.")
	//@Size(min=13, max=13, message = "JMBG must be exactly {min} characters long.")
	private String jMBG;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE")
	private String gender;
	@JsonView(Views.Teacher.class)
	private String certificate;
	@JsonView(Views.Teacher.class)
	@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Employment date is not valid, must be in dd-MM-yyyy format.")
	private String employmentDate;
	
	@JsonView(Views.Teacher.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private List<SubjectEntity> subjects;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^([A-Za-z]{1,1})$", message="Student department is not valid, must be only one letter.")
	private String primaryDepartment;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^(IV|V?I{1,2})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	private String primaryClass;
	
	@JsonView(Views.Admin.class)
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	@JsonView(Views.Admin.class)
	@Pattern(regexp="^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$",message="Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT")
	private String accessRole;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String password;
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String confirmedPassword;

	public TeacherDto() {
		super();
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender, 
			String certificate, 
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender, 
			String certificate, 
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate, 
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.") 
			List<SubjectEntity> subjects) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<SubjectEntity> subjects,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp="^(GENDER_MALE|GENDER_FEMALE)$",message="Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender, 
			String certificate, 
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message="Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String primaryDepartment,
			@Pattern(regexp = "^(IV|V?I{1,2})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String primaryClass) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.primaryDepartment = primaryDepartment;
		this.primaryClass = primaryClass;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String primaryDepartment,
			@Pattern(regexp = "^(IV|V?I{1,2})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String primaryClass,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.primaryDepartment = primaryDepartment;
		this.primaryClass = primaryClass;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public TeacherDto(
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "First name is not valid, can contain only letters and minimum is 2 letter.") String firstName,
			@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Last name is not valid, can contain only letters and minimum is 2 letter.") String lastName,
			@Pattern(regexp = "^[0-9]{13,13}$", message = "JMBG is not valid, can contain only numbers and must be exactly 13 numbers long.") @Size(min = 13, max = 13, message = "JMBG must be exactly {min} characters long.") String jMBG,
			@Pattern(regexp = "^(GENDER_MALE|GENDER_FEMALE)$", message = "Gender is not valid, must be GENDER_MALE or GENDER_FEMALE") String gender,
			String certificate,
			@Pattern(regexp = "^([0][1-9]|[1|2][0-9]|[3][0|1])[./-]([0][1-9]|[1][0-2])[./-]([1-2][0-9]{3})$", message = "Employment date is not valid, must be in dd-MM-yyyy format.") String employmentDate,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<SubjectEntity> subjects,
			@Pattern(regexp = "^([A-Za-z]{1,1})$", message = "Student department is not valid, must be only one letter.") String primaryDepartment,
			@Pattern(regexp = "^(IV|V?I{1,2})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String primaryClass,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^(ROLE_ADMIN|ROLE_TEACHER|ROLE_PARENT|ROLE_STUDENT)$", message = "Role is not valid, must be ROLE_ADMIN, ROLE_TEACHER, ROLE_PARENT or ROLE_STUDENT") String role,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
		this.primaryDepartment = primaryDepartment;
		this.primaryClass = primaryClass;
		this.username = username;
		this.accessRole = role;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
	}

	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public String getPrimaryDepartment() {
		return primaryDepartment;
	}

	public void setPrimaryDepartment(String primaryDepartment) {
		this.primaryDepartment = primaryDepartment;
	}

	public String getPrimaryClass() {
		return primaryClass;
	}

	public void setPrimaryClass(String primaryClass) {
		this.primaryClass = primaryClass;
	}

	public String getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(String role) {
		this.accessRole = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String geneder) {
		this.gender = geneder;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(String employmentDate) {
		this.employmentDate = employmentDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

}
