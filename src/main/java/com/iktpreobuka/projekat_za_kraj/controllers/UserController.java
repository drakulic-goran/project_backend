package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.UserDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.Encryption;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.util.UserCustomValidator;

@Controller
@RestController
@RequestMapping(value= "/project/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired 
	private UserCustomValidator userValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) { 
		binder.addValidators(userValidator); 
		}

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured({"ROLE_STUDENT", "ROLE_PARENT", "ROLE_TEACHER", "ROLE_ADMIN"})
	//@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/users/getAll started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			EUserRole role = userAccountRepository.getByUsername(loggedUser).getAccessRole();
			if (role.equals(EUserRole.ROLE_STUDENT)) {
				UserEntity user= userRepository.getById(userAccountRepository.getByUsername(loggedUser).getUser().getId());
				logger.info("This is an info message: getAll Student finished OK.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			} else if (role.equals(EUserRole.ROLE_PARENT)) {
				List<UserEntity> user= userRepository.findByParent(userAccountRepository.getByUsername(loggedUser).getUser().getId());
				logger.info("This is an info message: getAll Parent finished OK.");
				return new ResponseEntity<List<UserEntity>>(user, HttpStatus.OK);
			}
			Iterable<UserEntity> users= userRepository.findAll();
			logger.info("This is an info message: getAll Admin finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	/* @RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		try {
			UserEntity user=  userRepository.findById(Integer.parseInt(id)).orElse(null);
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} */
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody UserDto newAdmin, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/users/addNewAdmin started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newAdmin == null) {
			logger.info("This is an info message: New entity is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		UserEntity user = new UserEntity();
		try {
			if (newAdmin.getFirstName() != null || newAdmin.getLastName() != null || newAdmin.getjMBG() != null || newAdmin.getGender() != null || newAdmin.getUsername() != null || newAdmin.getPassword() != null) {
				user.setFirstName(newAdmin.getFirstName());
				user.setLastName(newAdmin.getLastName());
				user.setjMBG(newAdmin.getjMBG());
				user.setGender(EGender.valueOf(newAdmin.getGender()));
				user.setRole(EUserRole.ROLE_ADMIN);
				userRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setRole(EUserRole.ROLE_ADMIN);
				account.setUsername(newAdmin.getUsername());
				account.setPassword(Encryption.getPassEncoded(newAdmin.getPassword()));
				account.setUser(user);
				userAccountRepository.save(account);
				user.getAccounts().add(account);
				userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value="/teacher")
	public ResponseEntity<?> addNewTeacher(@Valid @RequestBody TeacherDto newTeacher, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/users/addNewTeacher started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newTeacher == null) {
			logger.info("This is an info message: New entity is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		TeacherEntity user = new TeacherEntity();
		try {
			if (newTeacher.getFirstName() != null || newTeacher.getLastName() != null || newTeacher.getjMBG() != null || newTeacher.getCertificate() != null || newTeacher.getEmploymentDate() != null || newTeacher.getGender() != null || newTeacher.getUsername() != null || newTeacher.getPassword() != null) {
				user.setFirstName(newTeacher.getFirstName());
				user.setLastName(newTeacher.getLastName());
				user.setjMBG(newTeacher.getjMBG());
				user.setGender(EGender.valueOf(newTeacher.getGender()));
				user.setRole(EUserRole.ROLE_TEACHER);
				user.setCertificate(newTeacher.getCertificate());
				user.setEmploymentDate(newTeacher.getEmploymentDate());
				userRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setRole(EUserRole.ROLE_TEACHER);
				account.setUsername(newTeacher.getUsername());
				account.setPassword(Encryption.getPassEncoded(newTeacher.getPassword()));
				account.setUser(user);
				userAccountRepository.save(account);
				user.getAccounts().add(account);
				userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST, value="/student")
	public ResponseEntity<?> addNewStudent(@Valid @RequestBody StudentDto newStudent, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/users/addNewStudent started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newStudent == null) {
			logger.info("This is an info message: New entity is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		StudentEntity user = new StudentEntity();
		try {
			if (newStudent.getFirstName() != null || newStudent.getLastName() != null || newStudent.getjMBG() != null || newStudent.getEnrollmentDate() != null || newStudent.getGender() != null || newStudent.getStudent_class() != null || newStudent.getUsername() != null || newStudent.getPassword() != null) {
				user.setFirstName(newStudent.getFirstName());
				user.setLastName(newStudent.getLastName());
				user.setjMBG(newStudent.getjMBG());
				user.setGender(EGender.valueOf(newStudent.getGender()));
				user.setRole(EUserRole.ROLE_STUDENT);
				user.setEnrollmentDate(newStudent.getEnrollmentDate());
				user.setSchoolIdentificationNumber(newStudent.getSchoolIdentificationNumber());
				userRepository.save(user);
				//user.setStudent_class(newStudent.getStudent_class());
				UserAccountEntity account = new UserAccountEntity();
				account.setRole(EUserRole.ROLE_STUDENT);
				account.setUsername(newStudent.getUsername());
				account.setPassword(Encryption.getPassEncoded(newStudent.getPassword()));
				account.setUser(user);
				userAccountRepository.save(account);
				user.getAccounts().add(account);
				userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST, value="/parent")
	public ResponseEntity<?> addNewParent(@Valid @RequestBody ParentDto newParent, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/users/addNewParent started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newParent == null) {
			logger.info("This is an info message: New entity is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		ParentEntity user = new ParentEntity();
		try {
			if (newParent.getFirstName() != null || newParent.getLastName() != null || newParent.getjMBG() != null || newParent.getGender() != null || newParent.getEmail() != null || newParent.getUsername() != null || newParent.getPassword() != null) {
				user.setFirstName(newParent.getFirstName());
				user.setLastName(newParent.getLastName());
				user.setjMBG(newParent.getjMBG());
				user.setGender(EGender.valueOf(newParent.getGender()));
				user.setRole(EUserRole.ROLE_PARENT);
				user.setEmail(newParent.getEmail());
				userRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setRole(EUserRole.ROLE_PARENT);
				account.setUsername(newParent.getUsername());
				account.setPassword(Encryption.getPassEncoded(newParent.getPassword()));
				account.setUser(user);
				userAccountRepository.save(account);
				user.getAccounts().add(account);
				userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

		
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody UserDto updateAdmin, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/users/modify started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateAdmin == null) {
			logger.info("This is an info message: New entity is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			UserEntity user = userRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			if (updateAdmin.getFirstName() != null || !updateAdmin.getFirstName().equals(" ") || !updateAdmin.getFirstName().equals("")) {
				user.setFirstName(updateAdmin.getFirstName());
				logger.info("This is an info message: First name modified.");
			}
			if (updateAdmin.getLastName() != null || !updateAdmin.getLastName().equals(" ") || !updateAdmin.getLastName().equals("")) {
				user.setLastName(updateAdmin.getLastName());
				logger.info("This is an info message: Last name modified.");
			}
			if (updateAdmin.getjMBG() != null || !updateAdmin.getjMBG().equals(" ") || !updateAdmin.getjMBG().equals("")) {
				user.setjMBG(updateAdmin.getjMBG());
				logger.info("This is an info message: JMBG modified.");
			}
			if (updateAdmin.getGender() != null || !updateAdmin.getGender().equals(" ") || !updateAdmin.getGender().equals("")) {
				user.setGender(EGender.valueOf(updateAdmin.getGender()));
				logger.info("This is an info message: Gender modified.");
			}
			if (updateAdmin.getUsername() != null || !updateAdmin.getUsername().equals(" ") || !updateAdmin.getUsername().equals("")) {
				for (UserAccountEntity re : user.getAccounts()) {
					if (re.getRole()==EUserRole.ROLE_ADMIN) {
						re.setUsername(updateAdmin.getUsername());;
						userAccountRepository.save(re);
						logger.info("This is an info message: Role username modified.");
					}
				}
			}
			if (updateAdmin.getPassword() != null || !updateAdmin.getPassword().equals(" ") || !updateAdmin.getPassword().equals("")) {
				for (UserAccountEntity re : user.getAccounts()) {
					if (re.getRole()==EUserRole.ROLE_ADMIN) {
						re.setPassword(Encryption.getPassEncoded(updateAdmin.getPassword()));;
						userAccountRepository.save(re);
						logger.info("This is an info message: Role password modified.");
					}
				}
			}
			userRepository.save(user);	
			logger.info("This is an info message: Entity modified.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/role/{role}")
	public ResponseEntity<?> modifyUserRole(@PathVariable String id, @PathVariable String role, Principal principal) {
		logger.info("This is an info message: /project/users/modifyUserRole started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			UserEntity user = userRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			EUserRole ur=EUserRole.valueOf(role);
			for (UserAccountEntity re : user.getAccounts()) {
				if (re.getRole()==user.getRole()) {
					re.setRole(ur);
					userAccountRepository.save(re);
					logger.info("This is an info message: Role modified.");
				}
			}
			user.setRole(ur);
			userRepository.save(user);
			logger.info("This is an info message: Entity modified.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/changePassword/{id}")
	public ResponseEntity<?> changePassword(@PathVariable String id, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal) {
		logger.info("This is an info message: /project/users/changePassword started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			UserEntity user = userRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			for (UserAccountEntity re : user.getAccounts()) {
				if(re.getPassword().equals(oldPassword)) {
					re.setPassword(Encryption.getPassEncoded(newPassword));
					userAccountRepository.save(re);
					logger.info("This is an info message: Password modified.");
					return new ResponseEntity<UserAccountEntity>(re, HttpStatus.OK);
				}
			}
			logger.info("This is an info message: Searched password not exist.");
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/users/delete started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			UserEntity user = userRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			userRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Entity deleted.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
/*	@RequestMapping(method= RequestMethod.GET, value="/by-email/{email}")
	public ResponseEntity<?> byUsername(@PathVariable String email) {
		try {
			UserEntity user = userRepository.findByEmail(email);
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} */

}
