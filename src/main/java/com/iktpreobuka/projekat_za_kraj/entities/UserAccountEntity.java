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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "useraccount", uniqueConstraints=@UniqueConstraint(columnNames= {"username"}))
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class UserAccountEntity {

	/* @JsonIgnore
	@OneToMany(mappedBy= "user_role", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	private List<UserEntity> users= new ArrayList<>(); */
	
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	@NotNull (message = "User must be provided.")
	@JsonBackReference
	private UserEntity user;
	
	/* @JsonIgnore
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
    private List<UserEntity> users = new ArrayList<>();
	// private Set<UserEntity> users = new HashSet<>(); */
	
	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="role_id")
	private Integer id;
	@Column(name="access_role")
	@JsonView(Views.Admin.class)
	@Enumerated(EnumType.STRING)
	@NotNull (message = "User role must be provided.")
	private EUserRole accessRole;
	@Column(name="username", unique=true, length=50)
	@JsonView(Views.Admin.class)
	@NotNull (message = "Username must be provided.")
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	@Column(name="password")
	@NotNull (message = "Password must be provided.")
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	private String password;
	@JsonIgnore
	@Version
	private Integer version;
	
	public UserAccountEntity() {
		super();
	}

	public UserAccountEntity(EUserRole role) {
		super();
	}

	public UserAccountEntity(@NotNull(message = "User role must be provided.") EUserRole role,
			@NotNull(message = "Username must be provided.") String username,
			@NotNull(message = "Password must be provided.") @Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password) {
		super();
		this.accessRole = role;
		this.username = username;
		this.password = password;
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

	public EUserRole getAccessRole() {
		return accessRole;
	}

	public void setAccessRole(EUserRole accessRole) {
		this.accessRole = accessRole;
	}

	public EUserRole getRole() {
		return accessRole;
	}

	public void setRole(EUserRole role) {
		this.accessRole = role;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

}
