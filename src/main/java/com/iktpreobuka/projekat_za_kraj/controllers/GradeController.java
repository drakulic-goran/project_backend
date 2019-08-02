package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;
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
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.GradeDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.ESemester;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.GradeRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.util.RESTError;

@Controller
@RestController
@RequestMapping(value= "/project/grades")
public class GradeController {
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private AdminRepository adminRepository;*/
	
	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;


	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_STUDENT", "ROLE_PARENT", "ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/grades/getAll started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		try {
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			if (loggedUser.getAccessRole().equals(EUserRole.ROLE_STUDENT)) {
				//StudentEntity user = studentRepository.findByUserAccountAndStatusLike(loggedUser, 1);
				//StudentEntity user= (StudentEntity) loggedUser.getUser();
				StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getUser().getId(), 1);
				List<GradeEntity> grade = gradeRepository.findByStudent(user);
				logger.info("This is an info message: getAll Student finished OK.");
				return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_PARENT)) {
				//ParentEntity user = parentRepository.findByUserAccountAndStatusLike(loggedUser, 1);
				//ParentEntity user= (ParentEntity) loggedUser.getUser();
				ParentEntity user = parentRepository.findByIdAndStatusLike(loggedUser.getUser().getId(), 1);
				List<GradeEntity> grade = gradeRepository.findByParent(user);
				logger.info("This is an info message: getAll Parent finished OK.");
				return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
			} else if (loggedUser.getAccessRole().equals(EUserRole.ROLE_TEACHER)) {
				//TeacherEntity user = teacherRepository.findByUserAccountAndStatusLike(loggedUser, 1);
				//TeacherEntity user= (TeacherEntity) loggedUser.getUser();
				TeacherEntity user = teacherRepository.findByIdAndStatusLike(loggedUser.getUser().getId(), 1);
				List<GradeEntity> grade = gradeRepository.findByTeacher(user);
				logger.info("This is an info message: getAll Primary teacher finished OK.");
				return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
			}
			Iterable<GradeEntity> grades= gradeRepository.findByStatusLike(1);
			logger.info("This is an info message: getAll finished OK.");
			return new ResponseEntity<Iterable<GradeEntity>>(grades, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/grades/addNew started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newGrade == null) {
			logger.info("This is an info message: New grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		GradeEntity grade = new GradeEntity();
		try {
			if (newGrade.getStudent() != null && newGrade.getSubject() != null && newGrade.getGradeValue() != null) {
				StudentEntity student = studentRepository.findById(Integer.parseInt(newGrade.getStudent())).orElse(null);
				if (student==null || student.getStatus()!=1) {
					logger.info("This is an info message: Searched student not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Student: " + student.getId().toString());
				SubjectEntity subject = subjectRepository.findById(Integer.parseInt(newGrade.getSubject())).orElse(null);
				if (subject==null || subject.getStatus()!=1) {
					logger.info("This is an info message: Searched subject not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Subject: " + subject.getId().toString());
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				if (teacher==null) {
					logger.info("This is an info message: Searched teacher not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Teacher: " + teacher.getId().toString());
				TeacherSubjectDepartmentEntity teacherDepartments = teacherSubjectDepartmentRepository.getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(teacher, subject, student.getStudent_department());
				if (teacherDepartments==null) {
					logger.info("This is an info message: It is not student teacher.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Teacher subject department: " + teacherDepartments.getId().toString());
				grade.setTeacher_subject_department(teacherDepartments);
				grade.setStudent(student);
				//grade.setSubjectName(subject.getSubjectName());
				grade.setStatusActive();
				grade.setSemester(ESemester.valueOf(newGrade.getSemester()));
				grade.setGradeValue(newGrade.getGradeValue());
				grade.setGradeMadeDate(new Date());
				//grade.setDepartmentLabel(student.getStudent_department().getDepartmentLabel());
				grade.setCreatedById(teacher.getId());
				//grade.setClassLabel(student.getStudent_department().getClass_department().getClassLabel());
				gradeRepository.save(grade);
				logger.info("This is an info message: Grade created.");
			}
			return new ResponseEntity<>(grade, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/grades/{id}/modify started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (result.hasErrors()) { 
			logger.info("This is an info message: Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("This is an info message: Grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (newGrade == null) {
			logger.info("This is an info message: New grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (grade==null) {
				logger.info("This is an info message: Searched department not exist.");
				return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
			}
			if (newGrade.getStudent() != null && newGrade.getSubject() != null && newGrade.getGradeValue() != null) {
				StudentEntity student = studentRepository.findById(Integer.parseInt(newGrade.getStudent())).orElse(null);
				if (student==null || student.getStatus()!=1) {
					logger.info("This is an info message: Searched student not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Student: " + student.getId().toString());
				SubjectEntity subject = subjectRepository.findById(Integer.parseInt(newGrade.getSubject())).orElse(null);
				if (subject==null || subject.getStatus()!=1) {
					logger.info("This is an info message: Searched subject not exist.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Subject: " + subject.getId().toString());
				UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
				TeacherEntity teacher = new TeacherEntity();
				//AdminEntity admin = new AdminEntity();
				Date updateDate = new Date();
				if (loggedUser.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
					teacher = grade.getTeacher_subject_department().getTeaching_teacher();
					//admin = adminRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
					updateDate.setYear(updateDate.getYear()-1);
				}
				else {
					teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
					updateDate.setMinutes(updateDate.getMinutes()-30);
				}
				if (teacher==null || teacher != grade.getTeacher_subject_department().getTeaching_teacher()) {
					logger.info("This is an info message: Searched teacher not exist or grade of other teacher.");
					return new ResponseEntity<Object>(null, HttpStatus.OK);
				}
				logger.info("Teacher: " + teacher.getId().toString());
				if (student == grade.getStudent() && teacher == grade.getTeacher_subject_department().getTeaching_teacher() && subject == grade.getTeacher_subject_department().getTeaching_subject() && updateDate.before(grade.getGradeMadeDate())) {
					grade.setGradeValue(newGrade.getGradeValue());
					grade.setGradeMadeDate(new Date());
					grade.setUpdatedById(loggedUser.getUser().getId());
				gradeRepository.save(grade);
				}
				logger.info("This is an info message: Grade created.");
			}
			return new ResponseEntity<>(grade, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/value/{value}")
	public ResponseEntity<?> modifyGradeById(@PathVariable Integer id, @PathVariable Integer value, Principal principal) {
		logger.info("This is an info message: /project/grades/{id}/modifyGradeById started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		if (value == null || value < 1 || value > 5) {
			logger.info("This is an info message: Grade is null or out of range.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null) {
				logger.info("This is an info message: Searched grade not exist.");
				return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
			}			
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			TeacherEntity teacher = new TeacherEntity();
			//AdminEntity admin = new AdminEntity();
			Date updateDate = new Date();
			if (loggedUser.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeaching_teacher();
				//admin = adminRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null || teacher != grade.getTeacher_subject_department().getTeaching_teacher()) {
				logger.info("This is an info message: Searched teacher not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			logger.info("Teacher: " + teacher.getId().toString());
			if (teacher == grade.getTeacher_subject_department().getTeaching_teacher() && updateDate.before(grade.getGradeMadeDate())) {
				grade.setGradeValue(value);
				grade.setGradeMadeDate(new Date());
				grade.setUpdatedById(loggedUser.getUser().getId());
				gradeRepository.save(grade);
			}
			logger.info("This is an info message: Grade created.");
			return new ResponseEntity<>(grade, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("This is an info message: /project/grades/undelete/{id}/unDelete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 0);
			logger.info("Grade returned.");
			if (grade==null || grade.getStatus()==1 || grade.getStatus()==-1) {
				logger.info("This is an info message: Searched grade not exist or grade is archived or active.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByID(loggedUser.get);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			grade.setStatusActive();
			grade.setUpdatedById(loggedUser.getUser().getId());
			gradeRepository.save(grade);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Grade undeleted.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
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
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("This is an info message: /project/grades/archive/{id}/archive started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 0);
			if (grade==null || grade.getStatus()==1 || grade.getStatus()==-1) {
				logger.info("This is an info message: Searched grade not exist, grade is archived or grade is active.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			//AdminEntity user= adminRepository.getByUserAccount(loggedUser);
			//AdminEntity user= (AdminEntity) loggedUser.getUser();
			grade.setStatusArchived();
			grade.setUpdatedById(loggedUser.getUser().getId());
			gradeRepository.save(grade);
			//subjectRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Grade deactivated.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("This is an info message: /project/grades/{id}/delete started.");
		String loggedUserName = principal.getName();
		logger.info("Logged user: " + loggedUserName);
		if (id == null) {
			logger.info("This is an info message: Grade is null.");
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null || grade.getStatus()==0 || grade.getStatus()==-1) {
				logger.info("This is an info message: Searched grade not exist or grade is deleted or archived.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			UserAccountEntity loggedUser = userAccountRepository.getByUsername(loggedUserName);
			TeacherEntity teacher = new TeacherEntity();
			//AdminEntity admin = new AdminEntity();
			Date updateDate = new Date();
			if (loggedUser.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeaching_teacher();
				//admin = adminRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("This is an info message: Searched teacher not exist.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			logger.info("Teacher: " + teacher.getId().toString());
			if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("This is an info message: Teacher doesn't match or time for delete expired.");
				return new ResponseEntity<Object>(null, HttpStatus.OK);
			}
			grade.setStatusInactive();
			grade.setGradeMadeDate(new Date());
			grade.setUpdatedById(loggedUser.getUser().getId());
			gradeRepository.save(grade);
			//departmentRepository.deleteById(Integer.parseInt(id));
			logger.info("This is an info message: Grade deleted.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
