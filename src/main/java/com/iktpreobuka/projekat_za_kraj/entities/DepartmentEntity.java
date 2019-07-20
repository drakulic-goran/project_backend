package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "department")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="id")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
public class DepartmentEntity {
	
	/*@JsonView(Views.Teacher.class)
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject_department", joinColumns = { @JoinColumn(name = "department_id") }, inverseJoinColumns = { @JoinColumn(name = "teacher_id"), @JoinColumn(name = "subject_id") })
	private List<TeacherSubjectEntity> teachers_subjects = new ArrayList<>();	
	// @JsonBackReference
	// private Set<TeacherSubjectEntity> teacher_subject = new HashSet<>();*/
	//********************************************************************ZA SADA NE KORISTIM
	
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "class_label")
	@NotNull (message = "Class label must be provided.")
	private ClassEntity class_label;

	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "student_department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<StudentEntity> students = new ArrayList<>();
	
	@JsonView(Views.Student.class)
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "primary_teacher")
	//@NotNull (message = "Primary teacher must be provided.")
	private TeacherEntity primary_teacher;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="department_id")
	private Integer id;
	/* @JsonView(Views.Private.class)
	@Column(name="class_number")
	@NotNull (message = "Class number must be provided.")
	@Min(value=1, message = "Class number must be {value} or higher!")
	@Max(value=8, message = "Class number must be {value} or lower!")
	private String classNumber; */
	//@JsonView(Views.Public.class)
	@Column(name="department_label")
	@JsonView(Views.Student.class)
	@Min(value=1, message = "Department label must be {value} or higher!")
	@NotNull (message = "Department label must be provided.")
	private String departmentLabel;
	@JsonView(Views.Student.class)
	@Column(name="school_year")
	@NotNull (message = "School year must be provided.")
	@Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\/(20|[3-9][0-9])\\\\d{2}$", message="School year is not valid.")
	private String schoolYear;
	@JsonView(Views.Student.class)
	@Column(name="semester")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Semester must be provided.")
	private ESemester semester;
	@JsonIgnore
	@Version
	private Integer version;

	public DepartmentEntity() {
		super();
	}

	public DepartmentEntity(@NotNull(message = "Class number must be provided.") ClassEntity classLabel,
			@Min(value = 1, message = "Department number must be {value} or higher!") String departmentLabel,
			@NotNull(message = "Semester must be provided.") ESemester semester,
			@NotNull (message = "School year must be provided.") String schoolYear) {
		super();
		this.class_label = classLabel;
		this.departmentLabel = departmentLabel;
		this.semester = semester;
		this.schoolYear = schoolYear;
	}

	public DepartmentEntity(List<TeacherSubjectEntity> teachers_subjects, List<StudentEntity> students,
			@NotNull(message = "Class number must be provided.") ClassEntity classLabel,
			@Min(value = 1, message = "Department number must be {value} or higher!") String departmentLabel,
			@NotNull(message = "Semester must be provided.") ESemester semester,
			@NotNull (message = "School year must be provided.") String schoolYear) {
		super();
		//this.teachers_subjects = teachers_subjects;
		this.students = students;
		this.class_label = classLabel;
		this.departmentLabel = departmentLabel;
		this.semester = semester;
		this.schoolYear = schoolYear;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ClassEntity getClass_label() {
		return class_label;
	}

	public void setClass_label(ClassEntity class_label) {
		this.class_label = class_label;
	}

	public String getDepartmentLabel() {
		return departmentLabel;
	}

	public void setDepartmentLabel(String departmentLabel) {
		this.departmentLabel = departmentLabel;
	}

	public ESemester getSemester() {
		return semester;
	}

	public void setSemester(ESemester semester) {
		this.semester = semester;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TeacherEntity getPrimary_teacher() {
		return primary_teacher;
	}

	public void setPrimary_teacher(TeacherEntity primary_teacher) {
		this.primary_teacher = primary_teacher;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}


}
