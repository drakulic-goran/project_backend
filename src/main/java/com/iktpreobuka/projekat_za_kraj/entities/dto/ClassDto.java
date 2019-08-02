package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class ClassDto {

	@JsonView(Views.Student.class)
	@Pattern(regexp = "^(IV|V?I{1,4})$", message="Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.")
	private String classLabel;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private List<SubjectEntity> subjects;
	@JsonView(Views.Admin.class)
	//@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	private List<DepartmentEntity> departments;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private SubjectEntity subject;
	@JsonView(Views.Student.class)
	//@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	private DepartmentEntity department;

	public ClassDto() {
		super();
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			SubjectEntity subject,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			DepartmentEntity department) {
		super();
		this.classLabel = classLabel;
		this.subject = subject;
		this.department = department;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<SubjectEntity> subjects,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			DepartmentEntity department) {
		super();
		this.classLabel = classLabel;
		this.subjects = subjects;
		this.department = department;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			SubjectEntity subject,
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			List<DepartmentEntity> departments) {
		super();
		this.classLabel = classLabel;
		this.subject = subject;
		this.departments = departments;
	}

	public ClassDto(
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String classLabel,
			//@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") 
			List<SubjectEntity> subjects) {
		super();
		this.classLabel = classLabel;
		this.subjects = subjects;
	}

	public ClassDto(
			//@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") 
			List<DepartmentEntity> departments,
			@Pattern(regexp = "^(IV|V?I{1,4})$", message = "Student class is not valid, must be I, II, III, IV, V, VI, VII or VIII.") String classLabel) {
		super();
		this.departments = departments;
		this.classLabel = classLabel;
	}

	public String getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}

	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public List<DepartmentEntity> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentEntity> departments) {
		this.departments = departments;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}
	
}
