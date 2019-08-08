package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.SubjectDao;

@Controller
@RestController
@RequestMapping(value= "/project/subjects")
public class SubjectController {
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private SubjectDao subjectDao;
	
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/subjects/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subjects= subjectRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/tacher/{id}")
	public ResponseEntity<?> getByTeacherId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/{id}")
	public ResponseEntity<?> getByParentId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/parent/getAllForParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			List<StudentSubjectDto> subjects= subjectRepository.findByParent(id);
			logger.info("Lista predmeta za decu.");

			Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			for (StudentSubjectDto entry : subjects) {
				SubjectEntity subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectEntity> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectEntity>> subjectsAndStudent = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<StudentEntity, List<SubjectEntity>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
			
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/{id}")
	public ResponseEntity<?> getByStudentId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/student/getAllForStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByStudent(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primaryteacher/{id}")
	public ResponseEntity<?> getByPrimaryTeacherId(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findByPrimaryTeacher(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/byclass/{id}")
	public ResponseEntity<?> getClassSubjects(@PathVariable String id, Principal principal) {
		logger.info("################ /project/class/subjectsbyclass/{id}/getClassSubjects started.");
		logger.info("Logged user: " + principal.getName());
		try {
			ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (class_==null) { 
				logger.info("---------------- Searched class not found.");
		        return new ResponseEntity<>("Searched class not found", HttpStatus.NOT_FOUND);
			}
			Iterable<SubjectEntity> classes= classRepository.findSubjectsByClass(class_);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(classes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher")
	public ResponseEntity<?> getAllForTeacher(Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectEntity> subjects= subjectRepository.findByTeacher(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.GET, value = "/primaryteacher")
	public ResponseEntity<?> getAllForPrimaryTeacher(Principal principal) {
		logger.info("################ /project/subjects/teacher/getAllForTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectEntity> subjects= subjectRepository.findByPrimaryTeacher(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Parent.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent")
	public ResponseEntity<?> getAllForParent(Principal principal) {
		logger.info("################ /project/subjects/parent/getAllForParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			List<StudentSubjectDto> subjects= subjectRepository.findByParent(loggedUser.getId());
			logger.info("Lista predmeta za decu.");

			Map<StudentEntity, List<SubjectEntity>> subjectsByStudent = new HashMap<StudentEntity, List<SubjectEntity>>();
			for (StudentSubjectDto entry : subjects) {
				SubjectEntity subject = entry.getSubject();
			    StudentEntity student = entry.getStudent();
			    List<SubjectEntity> subjectsss = subjectsByStudent.get(student);
			    if (subjectsss == null) {
			        subjectsss = new ArrayList<SubjectEntity>();
			        subjectsByStudent.put(student, subjectsss);
			    }
			    subjectsss.add(subject);
			}
			
			Map<String, List<SubjectEntity>> subjectsAndStudent = new TreeMap<String, List<SubjectEntity>>();
			for (Map.Entry<StudentEntity, List<SubjectEntity>> entry : subjectsByStudent.entrySet()) {
				subjectsAndStudent.put(entry.getKey().getFirstName() + " " + entry.getKey().getLastName(), entry.getValue());
			}			
			
			return new ResponseEntity<>(subjectsAndStudent, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<?> getAllForStudent(Principal principal) {
		logger.info("################ /project/subjects/student/getAllForStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			Iterable<SubjectEntity> subjects= subjectRepository.findByStudent(loggedUser.getId());
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody SubjectDto newSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/subjects/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors: " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newSubject == null) {
			logger.info("---------------- New subject is null.");
	        return new ResponseEntity<>("New subject is null.", HttpStatus.BAD_REQUEST);
	      }
		SubjectEntity subject = new SubjectEntity();
		try {
			if (newSubject.getSubjectName() != null && !newSubject.getSubjectName().equals("") && !newSubject.getSubjectName().equals(" ") && newSubject.getWeekClassesNumber() != null && !newSubject.getWeekClassesNumber().equals("") && !newSubject.getWeekClassesNumber().equals(" ")) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				subject = subjectDao.addNewSubject(loggedUser, newSubject);
				logger.info("Subject created.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/departments/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- New subject is null.");
	        return new ResponseEntity<>("New subject is null.", HttpStatus.BAD_REQUEST);
	      }
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not found.");
				return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
			}
			if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				subjectDao.modifySubject(loggedUser, subject, updateSubject);
				logger.info("Subject modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-class-add-subject-add-teacherswithdepartments")
	public ResponseEntity<?> addClassesAndOrTeachersAndOrTeachersWithDepartmentsToSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/add-class-add-subject-add-teacherswithdepartments/addClassesAndOrTeachersAndOrTeachersWithDepartmentsToSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		SubjectEntity subject = new SubjectEntity();
		try {
			subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachers() != null && updateSubject.getAssignmentDate() != null && teacherRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachers().iterator().next()), 1) != null) {
				subjectDao.addTeachersToSubject(loggedUser, subject, updateSubject.getTeachers());
				logger.info("Teacher/s added.");
			}
			if (updateSubject.getClasses() != null && updateSubject.getLearningProgram() != null && classRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getClasses().iterator().next()), 1) != null) {
				subjectDao.addClassToSubject(loggedUser, updateSubject.getClasses(), subject, updateSubject.getLearningProgram());
				logger.info("Class/es added.");
			}
			if (updateSubject.getTeachingDepartment()!= null && updateSubject.getTeachingTeacher()!= null && updateSubject.getSchoolYear() != null && teacherRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingTeacher()), 1) != null && departmentRepository.findByIdAndStatusLike(Integer.parseInt(updateSubject.getTeachingDepartment()), 1) != null) {
				subjectDao.addTeacherAndDepartmentToSubject(loggedUser, subject, updateSubject.getTeachingDepartment(), updateSubject.getTeachingTeacher(), updateSubject.getSchoolYear());
				logger.info("Teachers/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-class-remove-subject-remove-teacherswithdepartments")
	public ResponseEntity<?> removeClassesAndOrTeachersAndOrTeachersWithDepartmentsFromSubject(@PathVariable Integer id, @Valid @RequestBody SubjectDto updateSubject, Principal principal, BindingResult result) {
		logger.info("################ /project/teacher/{id}/remove-class-remove-subject-remove-teacherswithdepartments/removeClassesAndOrTeachersAndOrTeachersWithDepartmentsFromSubject started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateSubject == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateSubject.getSubjectName() != null || updateSubject.getWeekClassesNumber() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		SubjectEntity subject = new SubjectEntity();
		try {
			subject = subjectRepository.findByIdAndStatusLike(id, 1);
			if (subject == null) {
				logger.info("---------------- Subject not found.");
		        return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Subject identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateSubject.getTeachers() != null) {
				subjectDao.removeTeachersFromSubject(loggedUser, subject, updateSubject.getTeachers());
				logger.info("Teacher/s added.");
			}
			if (updateSubject.getClasses() != null) {
				subjectDao.removeClassFromSubject(loggedUser, updateSubject.getClasses(), subject);
				logger.info("Class/es added.");
			}
			if (updateSubject.getTeachingDepartment()!= null && updateSubject.getTeachingTeacher()!= null) {
				subjectDao.removeTeacherAndDepartmentFromSubject(loggedUser, subject, updateSubject.getTeachingDepartment(), updateSubject.getTeachingTeacher());
				logger.info("Teachers/s in department/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	/*
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/subject/{id}/teacher/{t_id}")
	public ResponseEntity<?> addTeacherToSubject(@PathVariable String id, @PathVariable String t_id, Principal principal) {
		logger.info("################ /project/subjects/modifySubjectWeekClassesNumber started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("This is an info message: Searched subject not found.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			} 
			TeacherEntity teacher = teacherRepository.findById(Integer.parseInt(t_id)).orElse(null);
			if (teacher==null) {
				logger.info("This is an info message: Searched teacher not found.");
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");

			logger.info("Subject added to teacher.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<TeacherSubjectEntity>(teacherSubject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable String id, Principal principal) {
		logger.info("################ /project/subjects/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(id), 0);
			if (subject==null || subject.getStatus()!=0) {
				logger.info("Subject not found.");
				return new ResponseEntity<>("---------------- Subject not found.", HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.undeleteSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable String id, Principal principal) {
		logger.info("################ /project/subjects/archive started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null || subject.getStatus()==-1) {
				logger.info("Subject not found.");
				return new ResponseEntity<>("---------------- Subject not found.", HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.archiveSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
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
		logger.info("################ /project/subjects/delete started.");
		logger.info("Logged user: " + principal.getName());
		try {
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (subject==null  || subject.getStatus()!=1) {
				logger.info("Subject not found.");
				return new ResponseEntity<>("Subject not found.", HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			subjectDao.deleteSubject(loggedUser, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
