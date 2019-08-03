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
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.DepartmentDao;

@Controller
@RestController
@RequestMapping(value= "/project/department")
public class DepartmentController {

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ClassRepository classRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/department/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> departments= departmentRepository.findWithClass_departmentByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable String id, Principal principal) {
		logger.info("################ /project/department/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> department= departmentRepository.findWithClass_departmentByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /project/department/getAllDeleted started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> departments= departmentRepository.findWithClass_departmentByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getByIdDeleted(@PathVariable String id, Principal principal) {
		logger.info("################ /project/department/getByIdDeleted started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> department= departmentRepository.findWithClass_departmentByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /project/department/archived/getAllArchived started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> departments= departmentRepository.findWithClass_departmentByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getByIdArchived(@PathVariable String id, Principal principal) {
		logger.info("################ /project/department/archived/getByIdArchived started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<DepartmentClassDto> department= departmentRepository.findWithClass_departmentByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(department, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody DepartmentDto newDepartment, Principal principal, BindingResult result) {
		logger.info("################ /project/department/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newDepartment == null) {
			logger.info("---------------- New department is null.");
	        return new ResponseEntity<>("New department is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			if (newDepartment.getDepartment_class() != null && newDepartment.getDepartmentLabel() != null && newDepartment.getEnrollmentYear() != null) {
				ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(newDepartment.getDepartment_class()), 1);
				if (class_==null || class_.getStatus()!=1) {
					logger.info("---------------- Searched class not exist.");
			        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
				}
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				department = departmentDao.addNewDepartment(loggedUser, newDepartment);		
				logger.info("---------------- Finished OK.");
			}
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody DepartmentDto updateDepartment, Principal principal, BindingResult result) {
		logger.info("################ /project/department/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<>("Department is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateDepartment == null) {
			logger.info("---------------- New department is null.");
	        return new ResponseEntity<>("New department is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (department==null || department.getStatus()!=1) {
				logger.info("---------------- Department not exist.");
				return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
			}
			if (updateDepartment.getDepartment_class() != null && !updateDepartment.getDepartment_class().equals(" ") && !updateDepartment.getDepartment_class().equals("")) {
				ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(updateDepartment.getDepartment_class()), 1);
				if (class_==null || class_.getStatus()!=1) {
					logger.info("---------------- Class not exist.");
					return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
				}
			}
			if (updateDepartment.getDepartment_class() != null || updateDepartment.getDepartmentLabel() != null || updateDepartment.getEnrollmentYear() != null) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				departmentDao.modifyDepartment(loggedUser, department, updateDepartment);	
				logger.info("Department modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

/*	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/class/{c_id}")
	public ResponseEntity<?> modifyClassLabel(@PathVariable String id, @PathVariable String c_id, Principal principal) {
		logger.info("################ /project/departments//change/{id}/class/{c_id}/modifyClassLabel started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (c_id == null) {
			logger.info("This is an info message: New class of department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			ClassEntity class_ = classRepository.findById(Integer.parseInt(c_id)).orElse(null);
			if (class_==null || class_.getStatus()!=1) {
				logger.info("This is an info message: Searched class not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			//***************************************
			//department.setClass_department(class_);
			//***************************************
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/label/{label}")
	public ResponseEntity<?> modifyDepartmentLabel(@PathVariable String id, @PathVariable String label, Principal principal) {
		logger.info("################ /project/departments//change/{id}/label/{label}/modifyDepartmentLabel started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (label == null) {
			logger.info("This is an info message: New label of department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			department.setDepartmentLabel(label);
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/subject/{s_id}/teacher/{t_id}/schoolyear/{school_year}")
	public ResponseEntity<?> addTeacherAndSubjectToDepartment(@PathVariable Integer id, @PathVariable Integer s_id, @PathVariable Integer t_id, String school_year, Principal principal) {
		logger.info("################ /project/department/{id}/subject/{s_id}/teacher/{t_id}/schoolyear/{school_year}/addTeacherAndSubjectToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || t_id == null || s_id == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Department identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not exist.");
		        return new ResponseEntity<>("Teacher not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher identified.");
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(s_id, 1);
			if (subject == null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not exist.");
		        return new ResponseEntity<>("Subject not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.addTeacherAndSubjectToDepartment(loggedUser, teacher, department, subject, school_year);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/primaryteacher/{t_id}/assignmentdate/{assignmentDate}")
	public ResponseEntity<?> addPrimaryTeacherToDepartment(@PathVariable Integer id, @PathVariable Integer t_id, @PathVariable String assignmentDate, Principal principal) {
		logger.info("################ /project/department/{id}/primaryteacher/{t_id}/assignmentdate/{assignmentDate}/addPrimaryTeacherToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || t_id == null || assignmentDate == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Department identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(t_id, 1);
			if (teacher == null || teacher.getStatus()!=1) {
				logger.info("---------------- Teacher not exist.");
		        return new ResponseEntity<>("Teacher not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Teacher identified.");
			if (assignmentDate != null && !assignmentDate.equals(" ") && !assignmentDate.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				departmentDao.addPrimaryTeacherToDepartment(loggedUser, teacher, department, assignmentDate);
				logger.info("Primary teacher added to department.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/class/{c_id}/schoolyear/{schoolyear}")
	public ResponseEntity<?> addClassToDepartment(@PathVariable Integer id, @PathVariable Integer c_id, @PathVariable String schoolyear, Principal principal) {
		logger.info("################ /project/department/{id}/class/{c_id}/addClassToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null || c_id == null || schoolyear == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findByIdAndStatusLike(id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Department identified.");
			ClassEntity class_ = classRepository.findByIdAndStatusLike(c_id, 1);
			if (class_ == null || class_.getStatus()!=1) {
				logger.info("---------------- Class not exist.");
		        return new ResponseEntity<>("Class not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Class identified.");
			if (schoolyear != null && !schoolyear.equals(" ") && !schoolyear.equals("")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				departmentDao.addClassToDepartment(loggedUser, class_, department, schoolyear);
				logger.info("Class added to department.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/student/{s_id}")
	public ResponseEntity<?> addStudentToDepartment(@PathVariable Integer id, @PathVariable Integer s_id, Principal principal) {
		logger.info("################ /project/department/{id}/student/{s_id}/addStudentToDepartment started.");
		logger.info("Logged user: " + principal.getName());
		StudentEntity student = new StudentEntity();
		try {
			DepartmentEntity department = departmentRepository.findByIdAndStatusLike(s_id, 1);
			if (department == null || department.getStatus()!=1) {
				logger.info("---------------- Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Department identified.");
			student = studentRepository.findByIdAndStatusLike(id, 1);
			if (student == null || student.getStatus()!=1) {
				logger.info("---------------- Student didn't find.");
		        return new ResponseEntity<>("Student not exist.", HttpStatus.BAD_REQUEST);
		      }
			logger.info("Student identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.addStudentToDepartment(loggedUser, student, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(student, HttpStatus.OK);
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
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<>("Department is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=0) {
				logger.info("----------------  Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.undeleteDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<>("Department is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()==-1) {
				logger.info("----------------  Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.archiveDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
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
		logger.info("################ /project/departments/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Department is null.");
	        return new ResponseEntity<>("Department is null.", HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("----------------  Department not exist.");
		        return new ResponseEntity<>("Department not exist.", HttpStatus.BAD_REQUEST);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			departmentDao.deleteDepartment(loggedUser, department);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
