package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.UserDao;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.util.UserCustomValidator;

@Controller
@RestController
@RequestMapping(value= "/project/users")
public class UserController {
	
	@Autowired
	private UserDao userDao;

	/*@Autowired
	private AdminRepository adminRepository;*/
	
	/*@Autowired
	private StudentRepository studentRepository;*/
	
	/*@Autowired
	private ParentRepository parentRepository;*/
	
	/*@Autowired
	private TeacherRepository teacherRepository;*/
	
	/*@Autowired
	private UserRepository userRepository;*/
	
	/*@Autowired
	private UserAccountRepository userAccountRepository;*/
	
	@Autowired 
	private UserCustomValidator userValidator;

	@InitBinder
	protected void initBinder(final WebDataBinder binder) { 
		binder.addValidators(userValidator); 
		}

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings("unused")
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/admin/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserEntity> users= userDao.findAllActiveUsers();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("---------------- This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*########################
	@Secured({"ROLE_STUDENT", "ROLE_PARENT", "ROLE_TEACHER", "ROLE_ADMIN"})
	//@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/users/getAll started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			if (loggedUser.getAccessRole().equals(EUserRole.ROLE_STUDENT)) {
				//StudentEntity user= studentRepository.getByUserAccount(loggedUser);
				StudentEntity user= studentRepository.getById(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Student finished OK.");
				return new ResponseEntity<StudentEntity>(user, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_PARENT)) {
				List<StudentEntity> user= studentRepository.findByParent(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Parent finished OK.");
				return new ResponseEntity<List<StudentEntity>>(user, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_TEACHER)) {
				//TeacherEntity teacher = teacherRepository.getById(loggedUser.getUser().getId());
				List<StudentDto> user= studentRepository.findByPrimaryTeacher(loggedUser.getUser().getId());
				logger.info("This is an info message: getAll Primary teacher finished OK." + loggedUser.getUser().getId());
				return new ResponseEntity<List<StudentDto>>(user, HttpStatus.OK);
			}
			Iterable<UserEntity> users= userRepository.findAll();
			logger.info("This is an info message: getAll Admin finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
		
	/* @RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		try {
			UserEntity user=  userRepository.findById(Integer.parseInt(id)).orElse(null);
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} */
	
	/*#########################
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody AdminDto newAdmin, Principal principal, BindingResult result) {
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
		//if (newAdmin.getFirstName() != null && !newAdmin.getFirstName().equals(" ") && !newAdmin.getFirstName().equals("") &&
		//newAdmin.getLastName() != null && !newAdmin.getLastName().equals(" ") && !newAdmin.getLastName().equals("") &&
		//newAdmin.getjMBG() != null && !newAdmin.getjMBG().equals(" ") && !newAdmin.getjMBG().equals("") &&
		//newAdmin.getGender() != null && !newAdmin.getGender().equals(" ") && !newAdmin.getGender().equals("") &&
		//newAdmin.getEmail() != null && !newAdmin.getEmail().equals(" ") && !newAdmin.getEmail().equals("") &&
		//newAdmin.getMobilePhoneNumber() != null && !newAdmin.getMobilePhoneNumber().equals(" ") && !newAdmin.getMobilePhoneNumber().equals("") &&
		//newAdmin.getUsername() != null && !newAdmin.getUsername().equals(" ") && !newAdmin.getUsername().equals("") &&
		//newAdmin.getPassword() != null && !newAdmin.getPassword().equals(" ") && !newAdmin.getPassword().equals("") &&
		//newAdmin.getConfirmedPassword() != null && !newAdmin.getConfirmedPassword().equals(" ") && !newAdmin.getConfirmedPassword().equals("")) {
		//	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		//}
		AdminEntity user = new AdminEntity();
		try {
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			if (newAdmin.getFirstName() != null || newAdmin.getLastName() != null || newAdmin.getjMBG() != null || newAdmin.getGender() != null || newAdmin.getUsername() != null || newAdmin.getPassword() != null) {
				user.setFirstName(newAdmin.getFirstName());
				user.setLastName(newAdmin.getLastName());
				user.setjMBG(newAdmin.getjMBG());
				user.setGender(EGender.valueOf(newAdmin.getGender()));
				user.setMobilePhoneNumber(newAdmin.getMobilePhoneNumber());
				user.setEmail(newAdmin.getEmail());
				user.setRole(EUserRole.ROLE_ADMIN);
				user.setStatusActive();
				user.setCreatedById(loggedUserId);
				//user.setUserAccount(account);
				adminRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setAccessRole(EUserRole.ROLE_ADMIN);
				account.setUsername(newAdmin.getUsername());
				account.setPassword(Encryption.getPassEncoded(newAdmin.getPassword()));
				account.setCreatedById(loggedUserId);
				account.setStatusActive();
				account.setUser(user);
				userAccountRepository.save(account);
				user.getAccounts().add(account);
				adminRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			AdminEntity user1 = adminRepository.getByJMBG(newAdmin.getjMBG());
			if (user1 !=null)
				adminRepository.delete(user1);
			logger.error("This is an exception message: User deleted.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*@Secured("ROLE_ADMIN")
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
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			if (newTeacher.getFirstName() != null || newTeacher.getLastName() != null || newTeacher.getjMBG() != null || newTeacher.getCertificate() != null || newTeacher.getEmploymentDate() != null || newTeacher.getGender() != null || newTeacher.getUsername() != null || newTeacher.getPassword() != null) {
				user.setFirstName(newTeacher.getFirstName());
				user.setLastName(newTeacher.getLastName());
				user.setjMBG(newTeacher.getjMBG());
				user.setGender(EGender.valueOf(newTeacher.getGender()));
				user.setRole(EUserRole.ROLE_TEACHER);
				user.setStatusActive();
				user.setCreatedById(loggedUserId);
				user.setCertificate(newTeacher.getCertificate());
				user.setEmploymentDate(Date.valueOf(newTeacher.getEmploymentDate()));
				//user.setUserAccount(account);
				teacherRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setAccessRole(EUserRole.ROLE_TEACHER);
				account.setUsername(newTeacher.getUsername());
				account.setPassword(Encryption.getPassEncoded(newTeacher.getPassword()));
				account.setCreatedById(loggedUserId);
				account.setStatusActive();
				account.setUser(user);
				userAccountRepository.save(account);
				//user.getAccounts().add(account);
				//teacherRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			TeacherEntity user1 = teacherRepository.getByJMBG(newTeacher.getjMBG());
			if (user1 !=null)
				teacherRepository.delete(user1);
			logger.error("This is an exception message: User deleted.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	/*@Secured("ROLE_TEACHER")
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
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			if (newStudent.getFirstName() != null || newStudent.getLastName() != null || newStudent.getjMBG() != null || newStudent.getEnrollmentDate() != null || newStudent.getGender() != null || newStudent.getStudent_department() != null || newStudent.getUsername() != null || newStudent.getPassword() != null) {
				user.setFirstName(newStudent.getFirstName());
				user.setLastName(newStudent.getLastName());
				user.setjMBG(newStudent.getjMBG());
				user.setGender(EGender.valueOf(newStudent.getGender()));
				user.setRole(EUserRole.ROLE_STUDENT);
				user.setStatusActive();
				user.setCreatedById(loggedUserId);
				user.setEnrollmentDate(Date.valueOf(newStudent.getEnrollmentDate()));
				user.setSchoolIdentificationNumber(newStudent.getSchoolIdentificationNumber());
				//user.setUserAccount(account);
				studentRepository.save(user);
				//user.setStudent_class(newStudent.getStudent_class());
				UserAccountEntity account = new UserAccountEntity();
				account.setAccessRole(EUserRole.ROLE_STUDENT);
				account.setUsername(newStudent.getUsername());
				account.setPassword(Encryption.getPassEncoded(newStudent.getPassword()));
				account.setCreatedById(loggedUserId);
				account.setStatusActive();
				account.setUser(user);
				userAccountRepository.save(account);
				//user.getAccounts().add(account);
				//userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			StudentEntity user1 = studentRepository.getByJMBG(newStudent.getjMBG());
			if (user1 !=null)
				studentRepository.delete(user1);
			logger.error("This is an exception message: User deleted.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*@Secured("ROLE_TEACHER")
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
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			if (newParent.getFirstName() != null || newParent.getLastName() != null || newParent.getjMBG() != null || newParent.getGender() != null || newParent.getEmail() != null || newParent.getUsername() != null || newParent.getPassword() != null) {
				user.setFirstName(newParent.getFirstName());
				user.setLastName(newParent.getLastName());
				user.setjMBG(newParent.getjMBG());
				user.setGender(EGender.valueOf(newParent.getGender()));
				user.setRole(EUserRole.ROLE_PARENT);
				user.setStatusActive();
				user.setCreatedById(loggedUserId);
				user.setEmail(newParent.getEmail());
				//user.setUserAccount(account);
				parentRepository.save(user);
				UserAccountEntity account = new UserAccountEntity();
				account.setAccessRole(EUserRole.ROLE_PARENT);
				account.setUsername(newParent.getUsername());
				account.setPassword(Encryption.getPassEncoded(newParent.getPassword()));
				account.setCreatedById(loggedUserId);
				account.setStatusActive();
				account.setUser(user);
				userAccountRepository.save(account);
				//user.getAccounts().add(account);
				//userRepository.save(user);
				logger.info("This is an info message: Entity created.");
			}
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			ParentEntity parent = parentRepository.getByJMBG(newParent.getjMBG());
			if (parent !=null)
				parentRepository.delete(parent);
			logger.error("This is an exception message: User account deleted.");
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

		
	/*#################################
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody AdminDto updateAdmin, Principal principal, BindingResult result) {
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
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			UserEntity user = adminRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			if (updateAdmin.getFirstName() != null || !updateAdmin.getFirstName().equals(" ") || !updateAdmin.getFirstName().equals("")) {
				user.setFirstName(updateAdmin.getFirstName());
				user.setUpdatedById(loggedUserId);
				logger.info("This is an info message: First name modified.");
			}
			if (updateAdmin.getLastName() != null || !updateAdmin.getLastName().equals(" ") || !updateAdmin.getLastName().equals("")) {
				user.setLastName(updateAdmin.getLastName());
				user.setUpdatedById(loggedUserId);
				logger.info("This is an info message: Last name modified.");
			}
			if (updateAdmin.getjMBG() != null || !updateAdmin.getjMBG().equals(" ") || !updateAdmin.getjMBG().equals("")) {
				user.setjMBG(updateAdmin.getjMBG());
				user.setUpdatedById(loggedUserId);
				logger.info("This is an info message: JMBG modified.");
			}
			if (updateAdmin.getGender() != null || !updateAdmin.getGender().equals(" ") || !updateAdmin.getGender().equals("")) {
				user.setGender(EGender.valueOf(updateAdmin.getGender()));
				user.setUpdatedById(loggedUserId);
				logger.info("This is an info message: Gender modified.");
			}
			UserAccountEntity re = userAccountRepository.getByUser(user);
			if (updateAdmin.getUsername() != null || !updateAdmin.getUsername().equals(" ") || !updateAdmin.getUsername().equals("")) {
				//for (UserAccountEntity re : user.getAccounts()) {
					//if (re.getRole()==EUserRole.ROLE_ADMIN) {
						//UserAccountEntity re = user.getUserAccount();
				re.setUsername(updateAdmin.getUsername());
				re.setUpdatedById(loggedUserId);
				logger.info("This is an info message: Username modified.");
					//}
				//}
			}
			if (updateAdmin.getPassword() != null || !updateAdmin.getPassword().equals(" ") || !updateAdmin.getPassword().equals("")) {
				//for (UserAccountEntity re : user.getAccounts()) {
					//if (re.getRole()==EUserRole.ROLE_ADMIN) {
						//UserAccountEntity re = user.getUserAccount();
				re.setPassword(Encryption.getPassEncoded(updateAdmin.getPassword()));
				re.setUpdatedById(loggedUserId);
				logger.info("This is an info message: Password modified.");
					//}
				//}
			}
			userAccountRepository.save(re);
			adminRepository.save(user);	
			logger.info("This is an info message: Entity modified.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	/*@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/role/{role}")
	public ResponseEntity<?> modifyUserRole(@PathVariable String id, @PathVariable String role, Principal principal) {
		logger.info("This is an info message: /project/users/modifyUserRole started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
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
			user.setUpdatedById(loggedUserId);
			userRepository.save(user);
			logger.info("This is an info message: Entity modified.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*@Secured("ROLE_STUDENT")
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/changePassword/{id}")
	public ResponseEntity<?> changePassword(@PathVariable String id, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal) {
		logger.info("This is an info message: /project/users/changePassword started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//UserEntity user = userRepository.findById(Integer.parseInt(id)).orElse(null);
			if (loggedUser==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserAccountEntity>(loggedUser, HttpStatus.OK);
			}
			//for (UserAccountEntity re : user.getAccounts()) {
				if(loggedUser.getPassword().equals(oldPassword)) {
					loggedUser.setPassword(Encryption.getPassEncoded(newPassword));
					userAccountRepository.save(loggedUser);
					//user.setUpdatedById(loggedUser);
					//userRepository.save(user);
					logger.info("This is an info message: Password modified.");
					return new ResponseEntity<UserAccountEntity>(loggedUser, HttpStatus.OK);
				}
			//}
			logger.info("This is an info message: Bad input.");
			return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	/*##################################################
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/users/delete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			//Integer loggedUserId = userAccountRepository.getByUsername(loggedUser).getId();
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			if (Integer.parseInt(id) == loggedUser.getId()) {
				logger.info("This is an info message: Cann't delete yourself.");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }	
			UserEntity user = studentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null)
				user = parentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null)
				user = teacherRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null)
				user = adminRepository.findById(Integer.parseInt(id)).orElse(null);
			if (user==null) {
				logger.info("This is an info message: Searched entity not exist.");
				return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
			}
			user.setStatusInactive();
			user.setUpdatedById(loggedUser.getId());
			logger.info("This is an info message: Modified to Inactive.");
			if (user instanceof StudentEntity) {
				studentRepository.save(user);
				logger.info("This is an info message: Student deleted.");
			} else if (user instanceof ParentEntity) {
				parentRepository.save(user);
				logger.info("This is an info message: Parent deleted.");
			} else if (user instanceof TeacherEntity) {
				teacherRepository.save(user);
				logger.info("This is an info message: Teacher deleted.");
			} else if (user instanceof AdminEntity) {
				adminRepository.save(user);
				logger.info("This is an info message: Admin deleted.");
			}
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
/*	@RequestMapping(method= RequestMethod.GET, value="/by-email/{email}")
	public ResponseEntity<?> byUsername(@PathVariable String email) {
		try {
			UserEntity user = userRepository.findByEmail(email);
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	} */

}
