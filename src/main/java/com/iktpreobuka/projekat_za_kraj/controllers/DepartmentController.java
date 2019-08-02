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
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;

@Controller
@RestController
@RequestMapping(value= "/project/departments")
public class DepartmentController {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/

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
		logger.info("This is an info message: /project/departments/getAll started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			Iterable<DepartmentClassDto> departments= departmentRepository.findWithClass_departmentByStatusLike(1);
			logger.info("This is an info message: getAll finished OK.");
			return new ResponseEntity<Iterable<DepartmentClassDto>>(departments, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody DepartmentDto newDepartment, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/departments/addNew started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newDepartment == null) {
			logger.info("This is an info message: New department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		DepartmentEntity department = new DepartmentEntity();
		try {
			if (newDepartment.getClass_department() != null && newDepartment.getDepartmentLabel() != null ) {
				ClassEntity class_ = classRepository.findById(Integer.parseInt(newDepartment.getClass_department().toString())).orElse(null);
				if (class_==null || class_.getStatus()!=1) {
					logger.info("This is an info message: Searched class not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				department.setClass_department(class_);
				department.setDepartmentLabel(newDepartment.getDepartmentLabel());
				/*Integer sem = Integer.parseInt(newDepartment.getSemester().toString())-1;
				if (sem < 0 || sem > 1) {
					logger.info("This is an info message: New semester is out of range, have value " + sem.toString());
			        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			      }*/
				//department.setSemester(ESemester.values()[sem]);
				department.setStatusActive();
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user= (AdminEntity) loggedUser.getUser();
				department.setCreatedById(loggedUser.getUser().getId());
				departmentRepository.save(department);
				logger.info("This is an info message: Department created.");
			}
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody DepartmentDto updateDepartment, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/departments/modify started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (updateDepartment == null) {
			logger.info("This is an info message: New department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			if (updateDepartment.getClass_department() != null || updateDepartment.getDepartmentLabel() != null /*|| updateDepartment.getSemester() != null*/) {
				ClassEntity class_ = classRepository.findById(Integer.parseInt(updateDepartment.getClass_department().toString())).orElse(null);
				if (class_==null || class_.getStatus()!=1) {
					logger.info("This is an info message: Searched class not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				department.setClass_department(class_);
			}
			if (updateDepartment.getDepartmentLabel() != null && !updateDepartment.getDepartmentLabel().equals(" ") && !updateDepartment.getDepartmentLabel().equals(""))
				department.setDepartmentLabel(updateDepartment.getDepartmentLabel());
			/*if (updateDepartment.getSemester() != null && !updateDepartment.getSemester().equals(" ") && !updateDepartment.getSemester().equals("")) {
				Integer sem = Integer.parseInt(updateDepartment.getSemester().toString())-1;
				if (sem < 0 || sem > 1) {
					logger.info("This is an info message: New semester is out of range, have value " + sem.toString());
			       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			     }
				department.setSemester(ESemester.values()[sem]);
			}*/
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("This is an info message: Department modified.");
			return new ResponseEntity<>(department, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/class/{c_id}")
	public ResponseEntity<?> modifyClassLabel(@PathVariable String id, @PathVariable String c_id, Principal principal) {
		logger.info("This is an info message: /project/departments//change/{id}/class/{c_id}/modifyClassLabel started.");
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
			department.setClass_department(class_);
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("This is an info message: Department modified.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/label/{label}")
	public ResponseEntity<?> modifyDepartmentLabel(@PathVariable String id, @PathVariable String label, Principal principal) {
		logger.info("This is an info message: /project/departments//change/{id}/label/{label}/modifyDepartmentLabel started.");
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
			logger.info("This is an info message: Department modified.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/semester/{semester_id}")
	public ResponseEntity<?> modifySemester(@PathVariable String id, @PathVariable String semester_id, Principal principal) {
		logger.info("This is an info message: /project/departments/change/{id}/semester/{semester_id}/modifySemester started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (semester_id == null) {
			logger.info("This is an info message: New semester is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()!=1) {
				logger.info("This is an info message: Searched department not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			Integer sem = Integer.parseInt(semester_id)-1;
			if (sem < 0 || sem > 1) {
				logger.info("This is an info message: New semester is out of range, have value " + sem.toString());
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		      }
			//department.setSemester(ESemester.values()[sem]);
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			logger.info("This is an info message: Department modified.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
		logger.info("This is an info message: /project/departments/unDelete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()==1 || department.getStatus()==-1) {
				logger.info("This is an info message: Searched department not exist, department is allready active or department is archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setStatusActive();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Subject activated.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable String id, Principal principal) {
		logger.info("This is an info message: /project/departments/archive started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()==1 || department.getStatus()==-1) {
				logger.info("This is an info message: Searched department not exist, department is archived or department is active.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setStatusArchived();;
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Subject deactivated.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
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
		logger.info("This is an info message: /project/departments/delete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			DepartmentEntity department = departmentRepository.findById(Integer.parseInt(id)).orElse(null);
			if (department==null || department.getStatus()==-1 || department.getStatus()==0) {
				logger.info("This is an info message: Searched department not exist or department is deleted or archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			department.setStatusInactive();
			department.setUpdatedById(loggedUser.getUser().getId());
			departmentRepository.save(department);
			//departmentRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Department deleted.");
			return new ResponseEntity<DepartmentEntity>(department, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
