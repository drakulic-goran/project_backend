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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;

@Controller
@RestController
@RequestMapping(value= "/project/class")
public class ClassController {
	
	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ClassSubjectRepository classSubjectRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/class/getAll started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(1);
			logger.info("This is an info message: getAll finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/subjectsbyclass/{id}")
	public ResponseEntity<?> getSubjectsByClass(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/class//subjectsbyclass/{id}/getSubjectsByClass started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("This is an info message: Searched class not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			Iterable<SubjectEntity> classes= classRepository.findSubjectsByClass(class_);
			logger.info("This is an info message: getAll finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody ClassDto newClass, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/class/addNew started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newClass == null) {
			logger.info("This is an info message: New class is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			if (newClass.getClassLabel() != null ) {
				class_.setClassLabel(EClass.valueOf(newClass.getClassLabel()));
				class_.setStatusActive();
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user = (AdminEntity) loggedUser.getUser();
				class_.setCreatedById(loggedUser.getUser().getId());
				classRepository.save(class_);
				logger.info("This is an info message: Class created.");
			}
			return new ResponseEntity<>(class_, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody ClassDto updateClass, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/class/modify started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("This is an info message: Class id is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (updateClass == null) {
			logger.info("This is an info message: New class is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("This is an info message: Searched class not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			if (updateClass.getClassLabel() != null && !updateClass.getClassLabel().equals(" ") && !updateClass.getClassLabel().equals("")) {
				class_.setClassLabel(EClass.valueOf(updateClass.getClassLabel()));
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user = (AdminEntity) loggedUser.getUser();
				class_.setUpdatedById(loggedUser.getUser().getId());
				classRepository.save(class_);
				logger.info("This is an info message: Class modified.");
			}
			return new ResponseEntity<>(class_, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/classlabel/{name}")
	public ResponseEntity<?> modifyClassLabel(@PathVariable String id, @PathVariable String name, Principal principal) {
		logger.info("This is an info message: /project/class/modifyClassLabel started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Class id is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (name == null) {
			logger.info("This is an info message: Class name is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("This is an info message: Searched class not exist.");
				return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
			}
			if (name != null && !name.equals(" ") && !name.equals("")) {
				class_.setClassLabel(EClass.valueOf(name));
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user = (AdminEntity) loggedUser.getUser();
				class_.setUpdatedById(loggedUser.getUser().getId());
				classRepository.save(class_);
				logger.info("This is an info message: Class modified.");
			}
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/class/{id}/subject/{s_id}/learningprogram/{name}")
	public ResponseEntity<?> addSubjectToClass(@PathVariable String id, @PathVariable String s_id, @PathVariable String name, Principal principal) {
		logger.info("This is an info message: /project/class/{id}/subject/{s_id}/learningprogram/{name}/addSubjectToClass started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Class id is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (s_id == null) {
			logger.info("This is an info message: Class id is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (name == null) {
			logger.info("This is an info message: Class name is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity calss_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (calss_==null) {
				logger.info("This is an info message: Searched class not exist.");
				return new ResponseEntity<ClassEntity>(calss_, HttpStatus.OK);
			} 
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(s_id)).orElse(null);
			if (subject==null) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			ClassSubjectEntity classSubject = new ClassSubjectEntity(calss_, subject, name, loggedUser.getUser().getId());
			classSubjectRepository.save(classSubject);
			subject.getClasses().add(classSubject);
			calss_.getSubjects().add(classSubject);
			classRepository.save(calss_);
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject added to class.");
			return new ResponseEntity<ClassSubjectEntity>(classSubject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/arhive/{id}")
	public ResponseEntity<?> arhive(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/class/arhive/{id}/arhive started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Class is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (class_==null || class_.getStatus()==-1 || class_.getStatus()==1) {
				logger.info("This is an info message: Searched class not exist or class is active or archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			//classRepository.deleteById(Integer.parseInt(id));
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			class_.setStatusArchived();
			class_.setUpdatedById(loggedUser.getUser().getId());
			classRepository.save(class_);
			logger.info("This is an info message: Class undeleted.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/class/undelete/{id}/unDelete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Class is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (class_==null || class_.getStatus()==-1 || class_.getStatus()==1) {
				logger.info("This is an info message: Searched class not exist or class is active or archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			//classRepository.deleteById(Integer.parseInt(id));
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			class_.setStatusActive();
			class_.setUpdatedById(loggedUser.getUser().getId());
			classRepository.save(class_);
			logger.info("This is an info message: Class undeleted.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/class/delete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Class is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null || class_.getStatus()==-1 || class_.getStatus()==0) {
				logger.info("This is an info message: Searched class not exist or class is deleted or archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			//classRepository.deleteById(Integer.parseInt(id));
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			class_.setStatusInactive();;
			class_.setUpdatedById(loggedUser.getUser().getId());
			classRepository.save(class_);
			logger.info("This is an info message: Class deleted.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
