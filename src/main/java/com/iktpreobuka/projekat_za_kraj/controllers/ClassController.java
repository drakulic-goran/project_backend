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
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.ClassDao;

@Controller
@RestController
@RequestMapping(value= "/project/class")
public class ClassController {
	
	@Autowired
	private ClassDao classDao;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/

	@Autowired
	private SubjectRepository subjectRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getByIdDeleted(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/class/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<ClassEntity> classes= classRepository.findByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<ClassEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getByIdArchived(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), -1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/subjectsbyclass/{id}")
	public ResponseEntity<?> getClassSubjects(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/subjectsbyclass/{id}/getClassSubjects started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("---------------- Searched class not exist.");
		        return new ResponseEntity<>("Searched class not exist", HttpStatus.BAD_REQUEST);
			}
			Iterable<SubjectEntity> classes= classRepository.findSubjectsByClass(class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody ClassDto newClass, Principal principal, BindingResult result) {
		logger.info("################ /project/class/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newClass == null || newClass.getClassLabel() == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			if (newClass.getClassLabel() != null && classRepository.getByClassLabel(EClass.valueOf(newClass.getClassLabel())) != null) {
				logger.info("---------------- Class label already exists.");
		        return new ResponseEntity<>("Class label already exists.", HttpStatus.BAD_REQUEST);
			}
			if (newClass.getClassLabel() != null && !newClass.getClassLabel().equals(" ") && !newClass.getClassLabel().equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				class_ = classDao.addNewClass(loggedUser, newClass);
				logger.info("Class created.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(class_, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody ClassDto updateClass, Principal principal, BindingResult result) {
		logger.info("################ /project/class/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null || updateClass == null || updateClass.getClassLabel() == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			if (updateClass.getClassLabel() != null && classRepository.getByClassLabel(EClass.valueOf(updateClass.getClassLabel())) != null) {
				logger.info("---------------- Class label already exists.");
		        return new ResponseEntity<>("Class label already exists.", HttpStatus.BAD_REQUEST);
			}
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			}
			if (updateClass.getClassLabel() != null && !updateClass.getClassLabel().equals(" ") && !updateClass.getClassLabel().equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				classDao.modifyClass(loggedUser, class_, updateClass);
				logger.info("Class modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(class_, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

/*	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/classlabel/{name}")
	public ResponseEntity<?> modifyClassLabel(@PathVariable String id, @PathVariable String name, Principal principal) {
		logger.info("################ /project/class/modifyClassLabel started.");
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
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-subject/{s_id}/learningprogram/{name}")
	public ResponseEntity<?> addSubjectToClass(@PathVariable String id, @PathVariable String s_id, @PathVariable String name, Principal principal) {
		logger.info("################ /project/class/{id}/subject/{s_id}/learningprogram/{name}/addSubjectToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || s_id == null || name == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassSubjectEntity cse = new ClassSubjectEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			} 
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(s_id)).orElse(null);
			if (subject==null) {
				logger.info("---------------- Subject not exist.");
		        return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
			}
			if (name != null && !name.equals(" ") && !name.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				cse = classDao.addSubjectToClass(loggedUser, class_, subject, name);
				logger.info("Subject added to class.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassSubjectEntity>(cse, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-subject/{s_id}")
	public ResponseEntity<?> removeSubjectFromClass(@PathVariable String id, @PathVariable String s_id, Principal principal) {
		logger.info("################ /project/class/{id}/remove-subject/{s_id}/removeSubjectFromClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || s_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassSubjectEntity cse = new ClassSubjectEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			} 
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(s_id)).orElse(null);
			if (subject==null) {
				logger.info("---------------- Subject not exist.");
		        return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			cse = classDao.removeSubjectFromClass(loggedUser, class_, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassSubjectEntity>(cse, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-department/{d_id}/schoolyear/{schoolyear}")
	public ResponseEntity<?> addDepartmentToClass(@PathVariable String id, @PathVariable String d_id, @PathVariable String schoolyear, Principal principal) {
		logger.info("################ /project/class/{id}/add-department/{d_id}/schoolyear/{schoolyear}/addDepartmentToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null || schoolyear == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dce = new DepartmentClassEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			} 
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(d_id), 1);
			if (department==null) {
				logger.info("---------------- Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
			}
			if (schoolyear != null && !schoolyear.equals(" ") && !schoolyear.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				dce = classDao.addDepartmentToClass(loggedUser, class_, department, schoolyear);
				logger.info("Department added to class.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentClassEntity>(dce, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-department/{d_id}")
	public ResponseEntity<?> removeDepartmentToClass(@PathVariable String id, @PathVariable String d_id, Principal principal) {
		logger.info("################ /project/class/{id}/remove-department/{d_id}/removeDepartmentToClass started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || d_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentClassEntity dc = new DepartmentClassEntity();
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			} 
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(d_id), 1);
			if (department==null) {
				logger.info("---------------- Departmnet not exist.");
		        return new ResponseEntity<>("Departmnet not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			dc = classDao.removeDepartmentFromClass(loggedUser, class_, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentClassEntity>(dc, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/arhive/{id}")
	public ResponseEntity<?> arhive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/arhive/{id}/arhive started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<>("Class is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.getById(Integer.parseInt(id));
			if (class_==null || class_.getStatus()==-1) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.archiveClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<>("Class is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (class_==null || class_.getStatus()!=0) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.undeleteClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Class is null.");
	        return new ResponseEntity<>("Class is null.", HttpStatus.BAD_REQUEST);
	      }
		ClassEntity class_ = new ClassEntity();
		try {
			class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null || class_.getStatus()!=1) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			classDao.deleteClass(loggedUser, class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<ClassEntity>(class_, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
