package com.iktpreobuka.projekat_za_kraj.entities;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "user_account"/*, uniqueConstraints=@UniqueConstraint(columnNames= {"username"})*/)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserAccountEntity {

	private static final Integer STATUS_INACTIVE = 0;
	private static final Integer STATUS_ACTIVE = 1;
	private static final Integer STATUS_ARCHIVED = -1;
	
	@JsonView(Views.Student.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	@NotNull (message = "User must be provided.")
	//@JsonBackReference
	//@JsonManagedReference
	private UserEntity user;
	/*@JsonView(Views.Admin.class)
	@OneToOne
	@JoinColumn(name="user")
    protected UserEntity user;*/
	
	
	@JsonView(Views.Student.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="account_id")
	private Integer id;
	@Column(name="access_role")
	@JsonView(Views.Student.class)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "User role must be provided.")
	private EUserRole accessRole;
	@Column(name="username", unique=true, length=50)
	@JsonView(Views.Student.class)
	@NotNull (message = "Username must be provided.")
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	//@JsonView(Views.Student.class)
	@Column(name="password")
	@NotNull (message = "Password must be provided.")
	//@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	//@Size(min=5, message = "Password must be {min} characters long or higher.")
	private String password;
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
	
	public UserAccountEntity() {
		super();
	}

	public UserAccountEntity(//@NotNull(message = "User must be provided.") UserEntity user,
			@NotNull(message = "User role must be provided.") EUserRole accessRole,
			@NotNull(message = "Username must be provided.") @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.") @NotNull(message = "Password must be provided.") @Size(min = 5, message = "Password must be {min} characters long or higher.") String password,
			Integer createdById) {
		super();
		//this.user = user;
		this.accessRole = accessRole;
		this.username = username;
		this.password = password;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public UserAccountEntity(@NotNull(message = "User must be provided.") UserEntity user,
			@NotNull(message = "User role must be provided.") EUserRole accessRole,
			@NotNull(message = "Username must be provided.") @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.") @NotNull(message = "Password must be provided.") @Size(min = 5, message = "Password must be {min} characters long or higher.") String password,
			Integer createdById) {
		super();
		this.user = user;
		this.accessRole = accessRole;
		this.username = username;
		this.password = password;
		this.status = getStatusActive();
		this.createdById = createdById;
	}

	public EUserRole getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(EUserRole accessRole) {
		this.accessRole = accessRole;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public static Integer getStatusInactive() {
		return STATUS_INACTIVE;
	}

	public static Integer getStatusActive() {
		return STATUS_ACTIVE;
	}

	public static Integer getStatusArchived() {
		return STATUS_ARCHIVED;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
