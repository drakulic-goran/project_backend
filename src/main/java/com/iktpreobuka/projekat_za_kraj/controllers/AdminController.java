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
import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.AdminRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.AdminDao;
import com.iktpreobuka.projekat_za_kraj.services.UserAccountDao;

@Controller
@RestController
@RequestMapping(value= "/project/admin")
public class AdminController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private AdminRepository adminRepository;
	
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
		logger.info("################ /project/admin/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, 1);
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<AdminEntity>(user, HttpStatus.OK);
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
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
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
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, 0);
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<AdminEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/admin/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<AdminEntity> users= adminRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<AdminEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getAllArchived(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			AdminEntity user= adminRepository.findByIdAndStatusLike(id, -1);
			user.getAccounts();
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<AdminEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody AdminDto newAdmin, Principal principal, BindingResult result) {
		logger.info("################ /project/admin/addNewAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newAdmin == null) {
			logger.info("---------------- New admin is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (newAdmin.getFirstName() == null || newAdmin.getLastName() == null || newAdmin.getEmail() == null || newAdmin.getMobilePhoneNumber() == null|| newAdmin.getGender() == null || newAdmin.getjMBG() == null) {
			logger.info("---------------- Some or all Admin atributes is null.");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new AdminEntity();
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			user = adminDao.addNewAdmin(loggedUser, newAdmin);
			logger.info("Admin created.");
			if (newAdmin.getUsername() != null && newAdmin.getPassword() != null && newAdmin.getConfirmedPassword() != null && newAdmin.getPassword().equals(newAdmin.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(loggedUser, user, newAdmin.getUsername(), EUserRole.ROLE_ADMIN, newAdmin.getPassword());
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && adminRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				adminRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyAdmin(@PathVariable Integer id, @Valid @RequestBody AdminDto updateAdmin, Principal principal, BindingResult result) {
		logger.info("################ /project/admin/{id}/modifyAdmin started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateAdmin == null) {
			logger.info("---------------- New Aadmin is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Admin didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Admin identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateAdmin.getFirstName() != null || updateAdmin.getLastName() != null || updateAdmin.getEmail() != null || updateAdmin.getMobilePhoneNumber() != null|| updateAdmin.getGender() != null || updateAdmin.getjMBG() != null) {
				adminDao.modifyAdmin(loggedUser, user, updateAdmin);
				logger.info("Admin modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null && (updateAdmin.getUsername() != null || (updateAdmin.getPassword() != null && updateAdmin.getConfirmedPassword() != null && updateAdmin.getPassword().equals(updateAdmin.getConfirmedPassword())))) {
				userAccountDao.modifyAccount(loggedUser, account, updateAdmin.getUsername(), updateAdmin.getPassword());
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<>(account, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/archivedeleted/archive started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Admin didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Admin for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			adminDao.archiveAdmin(loggedUser, user);
			logger.info("Admin archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				userAccountDao.archiveDeleteAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Admin didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Admin for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			adminDao.undeleteAdmin(loggedUser, user);
			logger.info("Admin undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/admin/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		AdminEntity user = new AdminEntity();
		try {
			user = adminRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Admin didn't find.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			logger.info("Admin for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }	
			adminDao.deleteAdmin(loggedUser, user);
			logger.info("Admin deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_ADMIN, 1);
			logger.info("Admin's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
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
				adminRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Admin with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
