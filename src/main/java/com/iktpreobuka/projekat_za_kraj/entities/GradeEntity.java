package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "grade")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GradeEntity {
	
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student")
	@NotNull (message = "Student value must be provided.")
	private StudentEntity student;
	
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JoinColumn(name = "teacher_subject")
	@JoinColumns(value = { @JoinColumn(name = "teacher"), @JoinColumn(name = "subject") })
	@NotNull (message = "Subject value must be provided.")
	private TeacherSubjectEntity teacherSubject;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="grade_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="grade_value")
	@NotNull (message = "Grade value must be provided.")
	@Min(value=1, message = "Grade value must be {value} or higher!")
	@Max(value=5, message = "Grade value must be {value} or lower!")
	private Integer gradeValue;
	@JsonView(Views.Student.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Grade made date must be provided.")
	private Date gradeMadeDate;
	@JsonView(Views.Student.class)
	@Column(name="department")
	@NotNull (message = "Department label must be provided.")
	private String departmentLabel;
	@JsonView(Views.Student.class)
	@Column(name="class")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Class label must be provided.")
	private EClass classLabel;
	@JsonView(Views.Student.class)
	@Column(name="semester")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Semester must be provided.")
	private ESemester semester;
	@JsonIgnore
	@Version
	private Integer version;
	
	public GradeEntity() {
		super();
	}
	
	public GradeEntity(@NotNull(message = "Student value must be provided.") StudentEntity student,
			@NotNull(message = "Subject value must be provided.") TeacherSubjectEntity subject,
			@NotNull(message = "Grade value must be provided.") @Min(value = 1, message = "Grade value must be {value} or higher!") @Max(value = 5, message = "Grade value must be {value} or lower!") Integer gradeValue,
			Date gradeMade) {
		super();
		this.student = student;
		//this.subject = subject;
		this.gradeValue = gradeValue;
		this.gradeMadeDate = gradeMade;
		this.departmentLabel = student.getStudent_department().getDepartmentLabel();
		this.classLabel = student.getStudent_department().getClass_label().getClassLabel();
		this.semester = student.getStudent_department().getSemester();		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGradeValue() {
		return gradeValue;
	}

	public void setGradeValue(Integer gradeValue) {
		this.gradeValue = gradeValue;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public Date getGradeMade() {
		return gradeMadeDate;
	}

	public Date getGradeMadeDate() {
		return gradeMadeDate;
	}

	public void setGradeMadeDate(Date gradeMadeDate) {
		this.gradeMadeDate = gradeMadeDate;
	}

	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	public EClass getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(EClass classLabel) {
		this.classLabel = classLabel;
	}

	public void setGradeMade(Date gradeMade) {
		this.gradeMadeDate = gradeMade;
	}

	public ESemester getSemester() {
		return semester;
	}

	public void setSemester(ESemester semester) {
		this.semester = semester;
	}

}
