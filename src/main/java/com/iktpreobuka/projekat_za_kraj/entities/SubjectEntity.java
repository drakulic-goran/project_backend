package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "subject", uniqueConstraints=@UniqueConstraint(columnNames= {"subject_name"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubjectEntity {
	

	//@JsonIgnore
	//@PersistenceContext
	//EntityManager em;
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
    private List<ClassEntity> classes = new ArrayList<>();
	
	@JsonIgnore
	@JsonView(Views.Teacher.class)
	@OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH}, orphanRemoval = true)
	//@JsonManagedReference
	private List<TeacherSubjectEntity> teachers = new ArrayList<>();
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="subject_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="subject_name", unique=true)
	@NotNull (message = "Subject name must be provided.")
	@NaturalId
	private String subjectName;
	@JsonView(Views.Parent.class)
	@Column(name="week_classes_number")
	@NotNull (message = "Number of classes in a week must be provided.")
	@Min(value=0, message = "Number of classes in a week must be {value} or higher!")
	private Integer weekClassesNumber;
	@JsonIgnore
	@Version
	private Integer version;

	public SubjectEntity() {
		super();
	}

	public SubjectEntity(@NotNull(message = "Subject name must be provided.") String subjectName) {
		super();
		this.subjectName = subjectName;
	}

	public SubjectEntity(@NotNull(message = "Subject name must be provided.") String subjectName,
			@NotNull(message = "Number of classes in a week must be provided.") @Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber) {
		super();
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
	}
	
	public SubjectEntity(List<TeacherSubjectEntity> teachers,
			@NotNull(message = "Subject name must be provided.") String subjectName,
			@NotNull(message = "Number of classes in a week must be provided.") @Min(value = 0, message = "Number of classes in a week must be {value} or higher!") Integer weekClassesNumber) {
		super();
		this.teachers = teachers;
		this.subjectName = subjectName;
		this.weekClassesNumber = weekClassesNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public List<TeacherSubjectEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherSubjectEntity> teachers) {
		this.teachers = teachers;
	}

	/* public void addTeacher(TeacherEntity teacher, Date assignmentDate) {
        TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity(teacher, this, assignmentDate);
        //em.persist(teacherSubject);
        teachers.add(teacherSubject);
        
        teacher.getSubjects().add(teacherSubject);
        //return teacherSubject;
    }
 
    public void removeTeacher(TeacherEntity teacher) {
        for (Iterator<TeacherSubjectEntity> iterator = teachers.iterator();
             iterator.hasNext(); ) {
        	TeacherSubjectEntity teacherSubject = iterator.next();
 
            if (teacherSubject.getSubject().equals(this) &&
                    teacherSubject.getTeacher().equals(teacher)) {
                iterator.remove();
                teacherSubject.getTeacher().getSubjects().remove(teacherSubject);
                teacherSubject.setTeacher(null);
                teacherSubject.setSubject(null);
            }
        }
    } */

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectEntity subject = (SubjectEntity) o;
        return Objects.equals(subjectName, subject.subjectName);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(subjectName);
    }
	
}
