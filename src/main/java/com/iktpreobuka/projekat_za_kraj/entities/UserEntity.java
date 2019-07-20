package com.iktpreobuka.projekat_za_kraj.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Entity
@Table (name = "user")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)  
@DiscriminatorColumn(name="type",discriminatorType=DiscriminatorType.STRING)  
@DiscriminatorValue(value="user")  
public class UserEntity {
	
	@JsonView(Views.Admin.class)
	//@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	@JsonManagedReference
	private List<UserAccountEntity> accounts = new ArrayList<>();
	
	/* @JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_role")
	private RoleEntity user_role; */
	
	/* @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private List<UserAccountEntity> roles = new ArrayList<>();
	// private Set<RoleEntity> roles = new HashSet<>(); */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Views.Admin.class)
	@Column(name="user_id")
	private Integer id;
	@JsonView(Views.Student.class)
	@Column(name="first_name")
	@NotNull (message = "First name must be provided.")
	private String firstName;
	@JsonView(Views.Student.class)
	@Column(name="last_name")
	@NotNull (message = "Last name must be provided.")
	private String lastName;
	@JsonView(Views.Admin.class)
	@Column(name="jmbg", unique=true, length=13)
	@Pattern(regexp = "^[0-9]*$", message="JMBG is not valid.")
	@Size(min=13, message = "JMBG must be {min} characters long.")
	@NotNull (message = "JMBG must be provided.")
	private String jMBG;
	@JsonView(Views.Student.class)
	@Column(name="gender")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "Gender must be provided.")
	private EGender gender;
	@JsonView(Views.Admin.class)
	@Column(name="role")
	@Enumerated(EnumType.STRING)
	@NotNull (message = "User role must be provided.")
	private EUserRole role;
	@JsonIgnore
	@Version
	private Integer version;
	
	// private final Logger logger= (Logger) LoggerFactory.getLogger(this.getClass());
	
	public UserEntity() { 
		super();
	}

	public UserEntity(@NotNull(message = "First name must be provided.") String firstName,
			@NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]*$", message = "JMBG is not valid.") @Size(min = 13, message = "JMBG must be {min} characters long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Sex must be provided.") EGender gender,
			@NotNull(message = "User role must be provided.") EUserRole role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public EGender getGender() {
		return gender;
	}

	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public List<UserAccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<UserAccountEntity> accounts) {
		this.accounts = accounts;
	}

	public EUserRole getRole() {
		return role;
	}

	public void setRole(EUserRole role) {
		this.role = role;
	}

}
