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
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.TeacherDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.util.UserCustomValidator;

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
			Iterable<TeacherEntity> users= teacherDao.findByStatusLike(1);
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
			TeacherEntity users= teacherDao.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(users, HttpStatus.OK);
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
			Iterable<TeacherEntity> users= teacherDao.findByStatusLike(0);
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
			TeacherEntity users= teacherDao.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(users, HttpStatus.OK);
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
			Iterable<TeacherEntity> users= teacherDao.findByStatusLike(-1);
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
			TeacherEntity users= teacherDao.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherEntity>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unused")
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody TeacherDto newTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/addNewAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (newTeacher.getFirstName() == null || newTeacher.getLastName() == null || newTeacher.getCertificate() == null || newTeacher.getEmploymentDate() == null|| newTeacher.getGender() == null || newTeacher.getjMBG() == null) {
			logger.info("---------------- Some or all Teacher atributes is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new TeacherEntity();
		try {
			UserEntity loggedUser = userAccountDao.findUserByUsername(principal.getName());
			logger.info("Logged user identified.");
			user = teacherDao.addNewTeacher(loggedUser, newTeacher);
			logger.info("Teacher created.");
			if (newTeacher.getUsername() != null && newTeacher.getPassword() != null && newTeacher.getConfirmedPassword() != null && newTeacher.getPassword().equals(newTeacher.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newTeacher.getUsername(), EUserRole.ROLE_TEACHER, newTeacher.getPassword());
				logger.info("Account created.");
				//return new ResponseEntity<>(account, HttpStatus.OK);
			}
			user.getAccounts();
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
	public ResponseEntity<?> modifyAdmin(@PathVariable Integer id, @Valid @RequestBody TeacherDto updateTeacher, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/modifyAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateTeacher == null) {
			logger.info("---------------- New teacher is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherDao.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher identified.");
			UserEntity loggedUser = userAccountDao.findUserByUsername(principal.getName());
			logger.info("Logged user identified.");
			if (updateTeacher.getFirstName() != null || updateTeacher.getLastName() != null || updateTeacher.getCertificate() != null || updateTeacher.getEmploymentDate() != null|| updateTeacher.getGender() != null || updateTeacher.getjMBG() != null) {
				teacherDao.modifyTeacher(loggedUser, user, updateTeacher);
				logger.info("Teacher modified.");
			}
			UserAccountEntity account = userAccountDao.findUserAccountByUserAndAccessRoleLike(user, "ROLE_ADMIN");
			logger.info("Admin's user account identified.");
			if (account != null && (updateTeacher.getUsername() != null || (updateTeacher.getPassword() != null && updateTeacher.getConfirmedPassword() != null && updateTeacher.getPassword().equals(updateTeacher.getConfirmedPassword())))) {
				userAccountDao.modifyAccount(loggedUser, account, updateTeacher.getUsername(), updateTeacher.getPassword());
				logger.info("Account modified.");
				//return new ResponseEntity<>(account, HttpStatus.OK);
			}
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archivedeleted/{id}")
	public ResponseEntity<?> archiveDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/teacher/archivedeleted/archiveDeleted started.");
		logger.info("Logged user: " + principal.getName());
		TeacherEntity user = new TeacherEntity();
		try {
			user = teacherDao.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Teacher didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher for archiving identified.");
			UserEntity loggedUser = userAccountDao.findUserByUsername(principal.getName());
			logger.info("Logged user identified.");
			teacherDao.archiveDeletedTeacher(loggedUser, user);
			logger.info("Teacher archived.");
			UserAccountEntity account = userAccountDao.findUserAccountByUserAndAccessRoleLike(user, "ROLE_ADMIN");
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.archiveDeleteAccount(loggedUser, account);
				logger.info("Account archived.");
				//return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			user.getAccounts();
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
			user = teacherDao.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Teacher didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher for undeleting identified.");
			UserEntity loggedUser = userAccountDao.findUserByUsername(principal.getName());
			logger.info("Logged user identified.");
			teacherDao.undeleteTeacher(loggedUser, user);
			logger.info("Teacher undeleted.");
			UserAccountEntity account = userAccountDao.findUserAccountByUserAndAccessRoleLike(user, "ROLE_ADMIN");
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("Account undeleted.");
				//return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			user.getAccounts();
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
			user = teacherDao.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Teacher didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher for deleting identified.");
			UserEntity loggedUser = userAccountDao.findUserByUsername(principal.getName());
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }	
			teacherDao.deleteTeacher(loggedUser, user);
			logger.info("Teacher deleted.");
			UserAccountEntity account = userAccountDao.findUserAccountByUserAndAccessRoleLike(user, "ROLE_ADMIN");
			logger.info("Teacher's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("Account deleted.");
				//return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			user.getAccounts();
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