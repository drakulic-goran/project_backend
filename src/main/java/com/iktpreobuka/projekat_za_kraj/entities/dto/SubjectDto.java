package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SubjectDto {
	
	@NotNull (message = "Subject name must be provided.")
	private String subjectName;
	@NotNull (message = "Number of classes in a week must be provided.")
	@Min(value=0, message = "Number of classes in a week must be {value} or higher!")
	private Integer weekClassesNumber;
	
	public SubjectDto() {
		super();
	}

	public SubjectDto(@NotNull(message = "Subject name must be provided.") String subjectName,
			@NotNull(message = "Number of classes in a week must be provided.") @Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber) {
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
