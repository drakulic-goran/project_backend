package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "student", uniqueConstraints=@UniqueConstraint(columnNames= {"school_identification_number"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@DiscriminatorValue("student") 
public class StudentEntity extends UserEntity {
	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@ManyToMany(mappedBy = "students", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
    // private List<ParentEntity> parents = new ArrayList<>();
	private Set<ParentEntity> parents = new HashSet<>();
	
	@JsonView(Views.Student.class)
	@JsonIgnore
	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<GradeEntity> grades = new ArrayList<>();
	
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student_department")
	private DepartmentEntity student_department;


	@JsonView(Views.Parent.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="enrollment_date")
	private Date enrollmentDate;
	@JsonView(Views.Teacher.class)
	@Column(name="school_identification_number", unique=true)
	@Pattern(regexp = "^[0-9]*$", message="School identification number is not valid.")
	@Size(min=8, message = "School identification number must be {min} characters long.")
	private String schoolIdentificationNumber;
	
	
	public StudentEntity() {
		super();
	}

	public StudentEntity(Date enrollmentDate, Set<ParentEntity> parents, DepartmentEntity student_department,
			List<GradeEntity> grades) {
		super();
		this.enrollmentDate = enrollmentDate;
		this.parents = parents;
		this.student_department = student_department;
		this.grades = grades;
	}

	public Set<ParentEntity> getParents() {
		return parents;
	}

	public void setParents(Set<ParentEntity> parents) {
		this.parents = parents;
	}

	public DepartmentEntity getStudent_department() {
		return student_department;
	}

	public void setStudent_department(DepartmentEntity student_department) {
		this.student_department = student_department;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public String getSchoolIdentificationNumber() {
		return schoolIdentificationNumber;
	}

	public void setSchoolIdentificationNumber(String schoolIdentificationNumber) {
		this.schoolIdentificationNumber = schoolIdentificationNumber;
	}

}
