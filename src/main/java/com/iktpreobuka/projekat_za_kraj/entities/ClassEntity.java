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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "class", uniqueConstraints=@UniqueConstraint(columnNames= {"class_label"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ClassEntity {
	
	@JsonView(Views.Teacher.class)
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "class_subject", joinColumns = { @JoinColumn(name = "class_id") }, inverseJoinColumns = { @JoinColumn(name = "subject_id") })
	private List<SubjectEntity> subjects = new ArrayList<>();	
	
	@JsonView(Views.Teacher.class)
	@JsonIgnore
	@OneToMany(mappedBy = "class_label", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	private List<DepartmentEntity> departments = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="class_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="class_label", unique=true)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Class label must be provided.")
	private EClass classLabel;
	@JsonIgnore
	@Version
	private Integer version;
	
	
	public ClassEntity() {
		super();
	}
	
	public ClassEntity(List<SubjectEntity> subjects, List<DepartmentEntity> departments,
			@NotNull(message = "Class label must be provided.") EClass classLabel) {
		super();
		this.subjects = subjects;
		this.departments = departments;
		this.classLabel = classLabel;
	}

	public List<SubjectEntity> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectEntity> subjects) {
		this.subjects = subjects;
	}

	public List<DepartmentEntity> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentEntity> departments) {
		this.departments = departments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EClass getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(EClass classLabel) {
		this.classLabel = classLabel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
