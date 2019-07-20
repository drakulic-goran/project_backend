package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table(name = "teacher")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@JsonIdentityInfo(generator=ObjectIdGenerators.UUIDGenerator.class, property="id")
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class)
@DiscriminatorValue("teacher") 
public class TeacherEntity extends UserEntity {
	
	/* @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_subject", joinColumns = { @JoinColumn(name = "teacher_id") }, inverseJoinColumns = { @JoinColumn(name = "subject_id") })
	// private List<SubjectEntity> subjects = new ArrayList<>();	
	private Set<SubjectEntity> subjects = new HashSet<>(); */
	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	//@JsonManagedReference
	private List<TeacherSubjectEntity> subjects = new ArrayList<>();
	
	@JsonView(Views.Teacher.class)
	//@JsonIgnore
	@OneToOne(mappedBy = "primary_teacher", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private DepartmentEntity primaryDepartment;

	
	@JsonView(Views.Teacher.class)
	@Column(name="certificate")
	//@NotNull (message = "Certificate background must be provided.")
	private String certificate;
	@JsonView(Views.Teacher.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="employment_date")
	//@NotNull (message = "Employment date must be provided.")
	private Date employmentDate;
	

	public TeacherEntity() {
		super();
	}

	public TeacherEntity(List<TeacherSubjectEntity> subjects) {
		super();
		this.subjects = subjects;
	}

	public TeacherEntity(String certificate, Date employmentDate,
			List<TeacherSubjectEntity> subjects) {
		super();
		this.certificate = certificate;
		this.employmentDate = employmentDate;
		this.subjects = subjects;
	}

	
	public TeacherEntity(DepartmentEntity primaryDepartment, String certificate, Date employmentDate) {
		super();
		this.certificate = certificate;
		this.employmentDate = employmentDate;
	}

	public List<TeacherSubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<TeacherSubjectEntity> subjects) {
		this.subjects = subjects;
	}
	
	public DepartmentEntity getPrimaryDepartment() {
		return primaryDepartment;
	}

	public void setPrimaryDepartment(DepartmentEntity primaryDepartment) {
		this.primaryDepartment = primaryDepartment;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public Date getEmploymentDate() {
		return employmentDate;
	}

	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}

	public void addSubject(SubjectEntity subject, Date assignmentDate) {
        TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity(this, subject, assignmentDate);
        //em.persist(teacherSubject);
        subjects.add(teacherSubject);
        subject.getTeachers().add(teacherSubject);
        // return teacherSubject;
    }
	
    public void removeSubject(SubjectEntity subject) {
        for (Iterator<TeacherSubjectEntity> iterator = subjects.iterator();
             iterator.hasNext(); ) {
        	TeacherSubjectEntity teacherSubject = iterator.next();
 
            if (teacherSubject.getTeacher().equals(this) &&
                    teacherSubject.getSubject().equals(subject)) {
                iterator.remove();
                teacherSubject.getSubject().getTeachers().remove(teacherSubject);
                teacherSubject.setTeacher(null);
                teacherSubject.setSubject(null);
            }
        }
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        UserEntity teacher = (TeacherEntity) o;
        return Objects.equals(this.getjMBG(), teacher.getjMBG());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(this.getjMBG());
    }
	
	 
}
