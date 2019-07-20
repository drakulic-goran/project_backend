package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.TeacherSubjectId;

@Entity
@Table(name = "teacher_subject")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@IdClass(TeacherSubjectId.class)
public class TeacherSubjectEntity {
	
	//@Id
	@EmbeddedId
	//@JsonView(Views.Admin.class)
	//@GeneratedValue(strategy = GenerationType.AUTO)
	//@Column(name="teacher_subject_id")
	private TeacherSubjectId id;

	//@Id
	//@JsonIgnore
	@JsonView(Views.Teacher.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacherId"/*, referencedColumnName = "user_id"*/)
	//@JsonBackReference
	private TeacherEntity teacher;
	
	//@Id
	//@JsonIgnore
	@JsonView(Views.Teacher.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectId"/*, updatable = false, insertable = false, */ /*referencedColumnName = "subject_id"*/)
	//@JsonBackReference
	private SubjectEntity subject;
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "teacherSubject", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<GradeEntity> grades = new ArrayList<>();
	
	/* @JsonIgnore
	@JsonView(Views.Teacher.class)
	@ManyToMany(mappedBy = "teachers_subjects", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
    // private List<ClassEntity> classes = new ArrayList<>();
	private List<DepartmentEntity> departments = new ArrayList<>();*/
	//*********************************************ZA SADA NE KORISTIM
	
	@JsonView(Views.Teacher.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@NotNull (message = "Assignment date must be provided.")
	@Column(name="assignment_date")
	private Date assignmentDate;
	@JsonIgnore
	@Version
	private Integer version;
	
	public TeacherSubjectEntity() {
		super();
	}

	public TeacherSubjectEntity(TeacherEntity teacher, SubjectEntity subject) {
		super();
		this.teacher = teacher;
		this.subject = subject;
		this.id = new TeacherSubjectId(teacher.getId(), subject.getId());
		//this.subjectId = subject.getId();
		//this.teacherId = teacher.getId();
	}

	/*public TeacherSubjectEntity(TeacherEntity teacher, SubjectEntity subject, List<DepartmentEntity> departments) {
		super();
		this.teacher = teacher;
		this.subject = subject;
		//this.departments = departments;
	}*/

	public TeacherSubjectEntity(TeacherEntity teacher, SubjectEntity subject,
			@NotNull(message = "Assignment date must be provided.") Date assignmentDate) {
		super();
		this.teacher = teacher;
		this.subject = subject;
		this.assignmentDate = assignmentDate;
		this.id = new TeacherSubjectId(teacher.getId(), subject.getId());
	}

	public TeacherEntity getTeacher() {
		return teacher;
	}

	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}

	public SubjectEntity getSubject() {
		return subject;
	}

	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Date getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Date assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	/*public TeacherSubjectId getId() {
		return id;
	}

	public void setId(TeacherSubjectId id) {
		this.id = id;
	}*/

	public Integer getVersion() {
		return version;
	}

	public TeacherSubjectId getId() {
		return id;
	}

	public void setId(TeacherSubjectId id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (o == null || getClass() != o.getClass())
            return false;
 
        TeacherSubjectEntity that = (TeacherSubjectEntity) o;
        return Objects.equals(teacher, that.teacher) &&
               Objects.equals(subject, that.subject);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(teacher, subject);
    }

}
