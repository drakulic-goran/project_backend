package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.controllers.util.UserCustomValidator;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.TeacherDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@RequestMapping(value= "/project/teacher")
public class TeacherController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private TeacherDao teacherDao;

	@Autowired
	private TeacherRepository teacherRepository;
	
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

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/teacher/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/teacher/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getAllDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/teacher/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<TeacherEntity> users= teacherRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<TeacherEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getAllArchived(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			TeacherEntity user= teacherRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unused")
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewTeacher(@Valid @RequestBody TeacherDto newTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/addNewTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<>("New teacher is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newTeacher.getFirstName() == null || newTeacher.getLastName() == null || newTeacher.getCertificate() == null || newTeacher.getEmploymentDate() == null || newTeacher.getGender() == null || newTeacher.getjMBG() == null || newTeacher.getAccessRole() != "ROLE_TEACHER") {
			logger.info("---------------- Some atributes is null.");
			return new ResponseEntity<>("Some atributes is null", HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new TeacherEntity();
		try {
			if (newTeacher.getjMBG() != null && teacherRepository.getByJMBG(newTeacher.getjMBG()) != null) {
				logger.info("---------------- JMBG already exists.");
		        return new ResponseEntity<>("JMBG already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newTeacher.getAccessRole() != null && !newTeacher.getAccessRole().equals("ROLE_TEACHER")) {
				logger.info("---------------- Access role must be ROLE_TEACHER.");
		        return new ResponseEntity<>("Access role must be ROLE_TEACHER.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newTeacher.getUsername() != null && userAccountRepository.getByUsername(newTeacher.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = teacherDao.addNewTeacher(loggedUser, newTeacher);
			logger.info("Teacher created.");
			if (newTeacher.getUsername() != null && newTeacher.getPassword() != null && newTeacher.getConfirmedPassword() != null && newTeacher.getPassword().equals(newTeacher.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newTeacher.getUsername(), EUserRole.ROLE_TEACHER, newTeacher.getPassword());
				logger.info("Account created.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && teacherRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				teacherRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyTeacher(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/modifyTeacher started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<>("New teacher data is null.", HttpStatus.BAD_REQUEST);
	      }
		TeacherEntity user = new TeacherEntity();
		try {
			if (updateTeacher.getjMBG() != null && userRepository.getByJMBG(updateTeacher.getjMBG()) != null) {
				logger.info("---------------- JMBG already exists.");
		        return new ResponseEntity<>("JMBG already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateTeacher.getAccessRole() != null && !updateTeacher.getAccessRole().equals("ROLE_TEACHER")) {
				logger.info("---------------- Access role must be ROLE_TEACHER.");
		        return new ResponseEntity<>("Access role must be ROLE_TEACHER.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateTeacher.getUsername() != null && userAccountRepository.getByUsername(updateTeacher.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null) {
				teacherDao.modifyTeacher(loggedUser, user, updateTeacher);
				logger.info("Teacher modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				if (updateTeacher.getUsername() != null && !updateTeacher.getUsername().equals("") && !updateTeacher.getUsername().equals(" ") && userAccountRepository.getByUsername(updateTeacher.getUsername()) != null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateTeacher.getUsername());
					logger.info("Username modified.");					
				}
				if (updateTeacher.getPassword() != null && !updateTeacher.getPassword().equals("") && !updateTeacher.getPassword().equals(" ") && updateTeacher.getConfirmedPassword() != null && updateTeacher.getPassword().equals(updateTeacher.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateTeacher.getPassword());
					logger.info("Password modified.");
				}
				logger.info("Account modified.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/subject-primarydepartment-subjectswithdepartments")
	public ResponseEntity<?> addSubjectsAndOrPrimaryDepartmentAndOrSubjectsWithDepartments(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/subject-primarydepartment-subjectswithdepartments/addSubjectsAndOrPrimaryDepartmentAndOrSubjectsWithDepartments started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getSubjects() != null) {
				teacherDao.addSubjectsToTeacher(loggedUser, user, updateTeacher.getSubjects());
				logger.info("Subject/s added.");
			}
			if (updateTeacher.getPrimaryDepartment() != null) {
				teacherDao.addPrimaryDepartmentToTeacher(loggedUser, user, updateTeacher.getPrimaryDepartment());
				logger.info("Primary department added.");
			}
			if (updateTeacher.getSubject_at_department()!= null) {
				teacherDao.addSubjectsInDepartmentsToTeacher(loggedUser, user, updateTeacher.getSubject_at_department(), updateTeacher.getSchoolYear());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove/subject-primarydepartment-subjectswithdepartments")
	public ResponseEntity<?> removeSubjectsAndOrPrimaryDepartmentAndOrSubjectsWithDepartments(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove/subject-primarydepartment-subjectswithdepartments/addSubjectsAndOrPrimaryDepartmentAndOrSubjectsWithDepartments started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null || updateTeacher.getUsername() != null || updateTeacher.getPassword() != null || updateTeacher.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateTeacher.getSubjects() != null) {
				teacherDao.removeSubjectsFromTeacher(loggedUser, user, updateTeacher.getSubjects());
				logger.info("Subject/s added.");
			}
			if (updateTeacher.getPrimaryDepartment() != null) {
				teacherDao.removePrimaryDepartmentFromTeacher(loggedUser, user, updateTeacher.getPrimaryDepartment());
				logger.info("Primary department added.");
			}
			if (updateTeacher.getSubject_at_department()!= null) {
				teacherDao.removeSubjectsInDepartmentsFromTeacher(loggedUser, user, updateTeacher.getSubject_at_department());
				logger.info("Subject/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<>("Selected Id is ID of logged User: Cann't archive yourself.", HttpStatus.FORBIDDEN);
		      }	
			teacherDao.archiveTeacher(loggedUser, user);
			logger.info("Teacher archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.archiveAccount(loggedUser, account);
				logger.info("Account archived.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == -1) {
				user.setStatusInactive();
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			teacherDao.undeleteTeacher(loggedUser, user);
			logger.info("Teacher undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("Account undeleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 1) {
				user.setStatusInactive();
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher not found.");
		        return new ResponseEntity<>("Teacher not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Teacher for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<>("Selected Id is ID of logged User: Cann't delete yourself.", HttpStatus.FORBIDDEN);
		      }	
			teacherDao.deleteTeacher(loggedUser, user);
			logger.info("Teacher deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_TEACHER, 1);
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("Account deleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 0) {
				user.setStatusActive();
				teacherRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Teacher with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
