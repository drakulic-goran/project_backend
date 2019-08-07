package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "teacher_subject_department", uniqueConstraints=@UniqueConstraint(columnNames= {"teacher_id", "subject_id", "department_id", "school_year"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TeacherSubjectDepartmentEntity {
	
	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="teacher_subject_department_id")
	private Integer id;
	
	@JsonIgnore
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable=false)
	@NotNull (message = "Department must be provided.")
	private DepartmentEntity teachingDepartment;
	
	@JsonIgnore
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable=false)
	@NotNull (message = "Subject must be provided.")
	private SubjectEntity teachingSubject;

	@JsonIgnore
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable=false)
	@NotNull (message = "Teacher must be provided.")
	private TeacherEntity teachingTeacher;
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "teacher_subject_department", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<GradeEntity> grades = new ArrayList<>();
	
	
	@JsonView(Views.Student.class)
	@Column(name="school_year", nullable=false)
	@NotNull (message = "School year must be provided.")
	//@Pattern(regexp = "^(20|[3-9][0-9])[0-9]{2}\\-(20|[3-9][0-9])[0-9]{2}$", message="School year is not valid, must be in format YYYY-YYYY.")
	private String schoolYear;
	@JsonView(Views.Admin.class)
	@Max(1)
    @Min(-1)
    @Column(name = "status", nullable = false)
	private Integer status;
	@JsonView(Views.Admin.class)
    @Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdById;
    @JsonView(Views.Admin.class)
    @Column(name = "updated_by")
    private Integer updatedById;
    @JsonIgnore
	@Version
	private Integer version;
	
	
	public TeacherSubjectDepartmentEntity() {
		super();
	}

	public TeacherSubjectDepartmentEntity(
			@NotNull(message = "Department must be provided.") DepartmentEntity teachingDepartment,
			@NotNull(message = "Subject must be provided.") SubjectEntity teachingSubject,
			@NotNull(message = "Teacher must be provided.") TeacherEntity teachingTeacher,
			@NotNull(message = "School year must be provided.") @Pattern(regexp = "^(20|[3-9][0-9])\\d{2}\\-(20|[3-9][0-9])\\\\d{2}$", message = "School year is not valid, must be in format YYYY-YYYY.") String schoolYear,
			Integer createdById) {
		super();
		this.teachingDepartment = teachingDepartment;
		this.teachingSubject = teachingSubject;
		this.teachingTeacher = teachingTeacher;
		this.schoolYear = schoolYear;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatusInactive() {
		this.status = getStatusInactive();
	}

	public void setStatusActive() {
		this.status = getStatusActive();
	}

	public void setStatusArchived() {
		this.status = getStatusArchived();
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

	public DepartmentEntity getTeaching_department() {
		return teachingDepartment;
	}

	public void setTeaching_department(DepartmentEntity teaching_department) {
		this.teachingDepartment = teaching_department;
	}

	public SubjectEntity getTeaching_subject() {
		return teachingSubject;
	}

	public void setTeaching_subject(SubjectEntity teaching_subject) {
		this.teachingSubject = teaching_subject;
	}

	public TeacherEntity getTeaching_teacher() {
		return teachingTeacher;
	}

	public void setTeaching_teacher(TeacherEntity teaching_teacher) {
		this.teachingTeacher = teaching_teacher;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}
		

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        TeacherSubjectDepartmentEntity that = (TeacherSubjectDepartmentEntity) o;
        return Objects.equals(class_subject, that.class_subject) &&
               Objects.equals(class_department, that.class_department) &&
               Objects.equals(teacher_department, that.teacher_department);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(class_subject, class_department, teacher_department);
    }*/

}
