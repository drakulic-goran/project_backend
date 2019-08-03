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
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.ParentDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@RequestMapping(value= "/project/parent")
public class ParentController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private ParentDao parentDao;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ParentRepository parentRepository;
	
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
		logger.info("################ /project/parent/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/admin/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getAllDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/parent/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ParentEntity> users= parentRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ParentEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getAllArchived(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ParentEntity user= parentRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ParentEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unused")
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewParent(@Valid @RequestBody ParentDto newParent, Principal principal, BindingResult result) {
		logger.info("################ /project/parent/addNewParent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newParent == null) {
			logger.info("---------------- New parent is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (newParent.getFirstName() == null || newParent.getLastName() == null || newParent.getEmail() == null || newParent.getGender() == null || newParent.getjMBG() == null) {
			logger.info("---------------- Some or all Parent atributes is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new ParentEntity();
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = parentDao.addNewParent(loggedUser, newParent);
			logger.info("Parent created.");
			if (newParent.getUsername() != null && newParent.getPassword() != null && newParent.getConfirmedPassword() != null && newParent.getPassword().equals(newParent.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newParent.getUsername(), EUserRole.ROLE_PARENT, newParent.getPassword());
				logger.info("Account created.");
				//return new ResponseEntity<>(account, HttpStatus.OK);
			}
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && parentRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				parentRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyParent(@PathVariable Integer id, @Valid @RequestBody ParentDto updateParent, Principal principal, BindingResult result) {
		logger.info("################ /project/parent/{id}/modifyParent started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateParent == null) {
			logger.info("---------------- New parent is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Parent identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateParent.getFirstName() != null || updateParent.getLastName() != null || updateParent.getEmail() != null || updateParent.getGender() != null || updateParent.getjMBG() != null) {
				parentDao.modifyParent(loggedUser, user, updateParent);
				logger.info("Parent modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
			if (account != null && (updateParent.getUsername() != null || (updateParent.getPassword() != null && updateParent.getConfirmedPassword() != null && updateParent.getPassword().equals(updateParent.getConfirmedPassword())))) {
				userAccountDao.modifyAccount(loggedUser, account, updateParent);
				logger.info("Account modified.");
				//return new ResponseEntity<>(account, HttpStatus.OK);
			}
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/child/{c_id}")
	public ResponseEntity<?> addChild(@PathVariable Integer id, @PathVariable Integer c_id, Principal principal) {
		logger.info("################ /project/parent/{id}/child/{c_id}/addChild started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Parent identified.");
			StudentEntity student = studentRepository.findByIdAndStatusLike(c_id, 1);
			if (student == null) {
				logger.info("---------------- Student didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Student identified.");
			for (StudentEntity s : user.getStudents()) {
				if (s.equals(student))
					return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			parentDao.addStudentToParent(user, student);		
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archivedeleted/{id}")
	public ResponseEntity<?> archiveDeleted(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/archivedeleted/archiveDeleted started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Parent didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Parent for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			parentDao.archiveDeletedParent(loggedUser, user);
			logger.info("Parent archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
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
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Parent didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Parent for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			parentDao.undeleteParent(loggedUser, user);
			logger.info("Parent undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
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
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/parent/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		ParentEntity user = new ParentEntity();
		try {
			user = parentRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Parent didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Parent for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }	
			parentDao.deleteParent(loggedUser, user);
			logger.info("Parent deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_PARENT, 1);
			logger.info("Parent's user account identified.");
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
				parentRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Parent with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
