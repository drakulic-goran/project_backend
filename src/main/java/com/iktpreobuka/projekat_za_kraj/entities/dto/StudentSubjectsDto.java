package com.iktpreobuka.projekat_za_kraj.entities.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class StudentSubjectsDto {

	@JsonView(Views.Student.class)
	@NotNull (message = "Student must be provided.")
	private StudentEntity student;
	@JsonView(Views.Student.class)
	@NotNull (message = "Subject must be provided.")
	private List<SubjectEntity> subject;
	
	public StudentSubjectsDto() {
		super();
	}

	public StudentSubjectsDto(@NotNull(message = "Student must be provided.") StudentEntity student,
			@NotNull(message = "Subject must be provided.") List<SubjectEntity> subject) {
		super();
		this.student = student;
		this.subject = subject;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public List<SubjectEntity> getSubject() {
		return subject;
	}

	public void setSubject(List<SubjectEntity> subject) {
		this.subject = subject;
	}

	
}
