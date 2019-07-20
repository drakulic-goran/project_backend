package com.iktpreobuka.projekat_za_kraj.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {
	
	@NotNull (message = "First name must be provided.")
	private String firstName;
	@NotNull (message = "Last name must be provided.")
	private String lastName;
	@Pattern(regexp = "^[0-9]*$", message="JMBG is not valid.")
	@Size(min=13, message = "JMBG must be {min} characters long.")
	@Size(max=13, message = "JMBG must be {min} characters long.")
	@NotNull (message = "JMBG must be provided.")
	private String jMBG;
	@NotNull (message = "Gender must be provided.")
	private String gender;
	@NotNull (message = "Username must be provided.")
	@Size(min=5, max=20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	@NotNull (message = "Password must be provided.")
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String password;
	@NotNull (message = "Repeated password must be provided.")
	@Size(min=5, message = "Password must be {min} characters long or higher.")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="Password is not valid, must contin only letters and numbers.")
	private String confirmedPassword;
	
	public UserDto() {
		super();
	}
	
	public UserDto(@NotNull(message = "First name must be provided.") String firstName,
			@NotNull(message = "Last name must be provided.") String lastName,
			@Pattern(regexp = "^[0-9]*$", message = "JMBG is not valid.") @Size(min = 13, message = "JMBG must be {min} characters long.") @NotNull(message = "JMBG must be provided.") String jMBG,
			@NotNull(message = "Sex must be provided.") String gender,
			@NotNull(message = "Username must be provided.") String username,
			@NotNull(message = "Password must be provided.") @Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String password,
			@NotNull(message = "Repeated password must be provided.") @Size(min = 5, message = "Password must be {min} characters long or higher.") @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Password is not valid, must contin only letters and numbers.") String confirmedPassword) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jMBG = jMBG;
		this.gender = gender;
		this.username = username;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public String getjMBG() {
		return jMBG;
	}

	public void setjMBG(String jMBG) {
		this.jMBG = jMBG;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
