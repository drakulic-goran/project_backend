package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectsDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;

@Controller
@RestController
@RequestMapping(value= "/project/subjects")
public class SubjectController {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	/*@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private StudentRepository studentRepository;*/

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/subjects/getAll started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByStatusLike(1);
			logger.info("This is an info message: Get finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher")
	public ResponseEntity<?> getAllForTeacher(Principal principal) {
		logger.info("This is an info message: /project/subjects/teacher/getAllForTeacher started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//UserEntity user = teacherRepository.getByUserAccount(loggedUser);
			//TeacherEntity user= teacherRepository.getById(loggedUser.getUser().getId());
			Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(loggedUser.getUser().getId());
			logger.info("This is an info message: Get finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent")
	public ResponseEntity<?> getAllForParent(Principal principal) {
		logger.info("This is an info message: /project/subjects/parent/getAllForParent started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//UserEntity user = parentRepository.getByUserAccount(loggedUser);
			//UserEntity user= loggedUser.getUser();
			List<StudentSubjectsDto> subjects= subjectRepository.findByParent(loggedUser.getUser().getId());
			//Map<SubjectEntity, StudentEntity> subjects = subjectRepository.findByParent(user);
			Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			//Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			for (StudentSubjectsDto  entry : subjects) {
				SubjectEntity subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectEntity> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			logger.info("This is an info message: Get finished OK.");
			//return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
			return new ResponseEntity<Map<StudentEntity, List<SubjectEntity>>>(subjectsByStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<?> getAllForStudent(Principal principal) {
		logger.info("This is an info message: /project/subjects/student/getAllForStudent started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//UserEntity user = studentRepository.getByUserAccount(loggedUser);
			//UserEntity user= loggedUser.getUser();
			Iterable<SubjectEntity> subjects= subjectRepository.findByStudent(loggedUser.getUser().getId());
			logger.info("This is an info message: Get finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody SubjectDto newSubject, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/subjects/addNew started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newSubject == null) {
			logger.info("This is an info message: New subject is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		SubjectEntity subject = new SubjectEntity();
		try {
			if (newSubject.getSubjectName() != null || newSubject.getWeekClassesNumber() != null) {
				subject.setSubjectName(newSubject.getSubjectName());
				subject.setWeekClassesNumber(newSubject.getWeekClassesNumber());
				subject.setStatusActive();
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user = (AdminEntity) loggedUser.getUser();
				subject.setCreatedById(loggedUser.getUser().getId());
				subjectRepository.save(subject);
				logger.info("This is an info message: Subject created.");
			}
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/departments/modify started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("This is an info message: New department is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
				if (updateSubject.getSubjectName() != null && !updateSubject.getSubjectName().equals(" ") && !updateSubject.getSubjectName().equals(""))
					subject.setSubjectName(updateSubject.getSubjectName());
				if (updateSubject.getWeekClassesNumber() != null || !updateSubject.getWeekClassesNumber().equals(" ") || !updateSubject.getWeekClassesNumber().equals(""))
					subject.setWeekClassesNumber(updateSubject.getWeekClassesNumber());
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				//AdminEntity user = adminRepository.getByUserAccount(loggedUser);
				//AdminEntity user = (AdminEntity) loggedUser.getUser();
				subject.setUpdatedById(loggedUser.getUser().getId());
				subjectRepository.save(subject);
				logger.info("This is an info message: Subject modified.");
			}
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/name/{name}")
	public ResponseEntity<?> modifySubjectName(@PathVariable String id, @PathVariable String name, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectName started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (name == null) {
			logger.info("This is an info message: New name is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			subject.setSubjectName(name);
			subject.setUpdatedById(loggedUser.getUser().getId());
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject modified.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/weekclassesnumber/{number}")
	public ResponseEntity<?> modifySubjectWeekClassesNumber(@PathVariable String id, @PathVariable Integer number, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectWeekClassesNumber started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (number == null) {
			logger.info("This is an info message: New number of classes in a week is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			subject.setWeekClassesNumber(number);
			subject.setUpdatedById(loggedUser.getUser().getId());
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject modified.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.POST, value = "/subject/{id}/teacher/{t_id}")
	public ResponseEntity<?> addTeacherToSubject(@PathVariable String id, @PathVariable String t_id, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectWeekClassesNumber started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			} 
			TeacherEntity teacher = teacherRepository.findById(Integer.parseInt(t_id)).orElse(null);
			if (teacher==null) {
				logger.info("This is an info message: Searched teacher not exist.");
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			}
			//Integer loggedUserId = userAccountRepository.getByUsername(loggedUserName).getId();
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			Date assignmentDate = new Date();
			/*teacher.addSubject(subject, assignmentDate);
			teacherRepository.save(teacher);
			subjectRepository.save(subject);*/
			TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity(teacher, subject, assignmentDate, loggedUser.getUser().getId());
			teacherSubjectRepository.save(teacherSubject);
			teacher.getSubjects().add(teacherSubject);
			subject.getTeachers().add(teacherSubject);
			teacherRepository.save(teacher);
			subjectRepository.save(subject);
			/* teacher.getSubjects().add(teacherSubject);
			subject.getTeachers().add(teacherSubject);*/
			// teacher.addSubject(subject, assignmentDate);
			/* TeacherSubjectEntity teacherSubject = //new TeacherSubjectEntity(teacher, subject, assignmentDate);
			teacher.addSubject(subject, assignmentDate);
			teacherSubjectRepository.save(teacherSubject);*/
			//teacherRepository.save(teacher);
			//subjectRepository.save(subject);
			logger.info("This is an info message: Subject added to teacher.");
			return new ResponseEntity<TeacherSubjectEntity>(teacherSubject, HttpStatus.OK);
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
		logger.info("This is an info message: /project/subjects/unDelete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()==1 || subject.getStatus()==-1) {
				logger.info("This is an info message: Searched subject not exist, subject is allready active or subject is archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			subject.setStatusActive();
			subject.setUpdatedById(loggedUser.getUser().getId());
			subjectRepository.save(subject);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Subject activated.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
		logger.info("This is an info message: /project/subjects/archive started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()==-1 || subject.getStatus()==1) {
				logger.info("This is an info message: Searched subject not exist, subject is archived or subject is active.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			subject.setStatusArchived();
			subject.setUpdatedById(loggedUser.getUser().getId());
			subjectRepository.save(subject);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Subject deactivated.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
		logger.info("This is an info message: /project/subjects/delete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null  || subject.getStatus()==-1) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user = (AdminEntity) loggedUser.getUser();
			subject.setStatusInactive();;
			subject.setUpdatedById(loggedUser.getUser().getId());
			subjectRepository.save(subject);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Subject deleted.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
