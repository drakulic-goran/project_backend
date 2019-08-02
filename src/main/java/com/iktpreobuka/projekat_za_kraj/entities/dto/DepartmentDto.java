package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class DepartmentDto {

	@JsonView(Views.Student.class)
	private ClassEntity class_department;
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^[a-zA-Z]$", message="Department label is not valid, can contain only one letter.")
	private String departmentLabel;
	//@Enumerated(EnumType.STRING)
	//@NotNull (message = "Semester must be provided.")
	//private String semester;
	@JsonView(Views.Teacher.class)
	private List<StudentEntity> students;
	@JsonView(Views.Student.class)
	private TeacherEntity primaryTeacher;
	@JsonView(Views.Student.class)
	private List<TeacherSubjectEntity> teachers_subjects;
	
	public DepartmentDto() {
		super();
	}

	public DepartmentDto(ClassEntity class_department,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") String departmentLabel,
			List<StudentEntity> students, TeacherEntity primaryTeacher, List<TeacherSubjectEntity> teachers_subjects) {
		super();
		this.class_department = class_department;
		this.departmentLabel = departmentLabel;
		this.students = students;
		this.primaryTeacher = primaryTeacher;
		this.teachers_subjects = teachers_subjects;
	}

	public DepartmentDto(List<TeacherSubjectEntity> teachers_subjects, ClassEntity class_department,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") String departmentLabel) {
		super();
		this.teachers_subjects = teachers_subjects;
		this.class_department = class_department;
		this.departmentLabel = departmentLabel;
	}

	public DepartmentDto(ClassEntity class_department,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") String departmentLabel,
			TeacherEntity primaryTeacher) {
		super();
		this.class_department = class_department;
		this.departmentLabel = departmentLabel;
		this.primaryTeacher = primaryTeacher;
	}

	public DepartmentDto(ClassEntity class_department,
			@Pattern(regexp = "^[a-zA-Z]$", message = "Department label is not valid, can contain only one letter.") String departmentLabel,
			List<StudentEntity> students) {
		super();
		this.class_department = class_department;
		this.departmentLabel = departmentLabel;
		this.students = students;
	}

	public ClassEntity getClass_department() {
		return class_department;
	}

	public void setClass_department(ClassEntity class_department) {
		this.class_department = class_department;
	}

	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public TeacherEntity getPrimaryTeacher() {
		return primaryTeacher;
	}

	public void setPrimaryTeacher(TeacherEntity primaryTeacher) {
		this.primaryTeacher = primaryTeacher;
	}

	public List<TeacherSubjectEntity> getTeachers_subjects() {
		return teachers_subjects;
	}

	public void setTeachers_subjects(List<TeacherSubjectEntity> teachers_subjects) {
		this.teachers_subjects = teachers_subjects;
	}
	
}
