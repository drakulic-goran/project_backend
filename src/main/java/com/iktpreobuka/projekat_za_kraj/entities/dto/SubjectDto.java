package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

public class SubjectDto {
	
	@JsonView(Views.Student.class)
	@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message="Subject is not valid.")
	private String subjectName;
	@JsonView(Views.Student.class)
	@Min(value=0, message = "Number of classes in a week must be {value} or higher!")
	private Integer weekClassesNumber;
	
	public SubjectDto() {
		super();
	}

	public SubjectDto(@Pattern(regexp = "^([_A-Za-z0-9- _])+$", message = "Subject is not valid.") String subjectName,
			@Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber) {
		super();
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getWeekClassesNumber() {
		return weekClassesNumber;
	}

	public void setWeekClassesNumber(Integer weekClassesNumber) {
		this.weekClassesNumber = weekClassesNumber;
	}

}
