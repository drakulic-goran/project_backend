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
import com.iktpreobuka.projekat_za_kraj.controllers.util.RESTError;
import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.GradeDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.models.EmailObject;
import com.iktpreobuka.projekat_za_kraj.repositories.GradeRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ParentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.EmailService;
import com.iktpreobuka.projekat_za_kraj.services.GradeDao;

@Controller
@RestController
@RequestMapping(value= "/project/grades")
public class GradeController {
	
	@Autowired
	private UserAccountRepository userAccountRepository;

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
	private GradeDao gradeDao;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;


	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /project/grades/getAll started.");
		logger.info("Logged user: " + principal.getName());
		try {
			Iterable<GradeEntity> grades= gradeRepository.findByStatusLike(1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<GradeEntity>>(grades, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/{id}/getById started.");
		logger.info("Logged user: " + principal.getName());
		try {
			GradeEntity grade= gradeRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher")
	public ResponseEntity<?> getAllTeacher(Principal principal) {
		logger.info("################ /project/grades/teacher/getAllTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<GradeEntity> grade = gradeRepository.findByTeacher(user);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_TEACHER"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/teacher/{id}")
	public ResponseEntity<?> getByIdTeacher(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/teacher/{id}/getByIdTeacher started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity user = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByTeacherAndGradeId(user, id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent")
	public ResponseEntity<?> getAllParent(Principal principal) {
		logger.info("################ /project/grades/parent/getAllParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity user = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<GradeEntity> grade = gradeRepository.findByParent(user);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_PARENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/parent/{id}")
	public ResponseEntity<?> getByIdParent(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			ParentEntity user = parentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByParentAndGradeId(user, id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student")
	public ResponseEntity<?> getAllStudent(Principal principal) {
		logger.info("################ /project/grades/student/getAllStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<GradeEntity> grade = gradeRepository.findByStudentAndStatusLike(user, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/{id}")
	public ResponseEntity<?> getByIdStudent(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/student/{id}/getByIdStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			GradeEntity grade = gradeRepository.findByStudentAndIdAndStatusLike(user, id, 1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/groupedbysubject")
	public ResponseEntity<?> getAllStudentGroupedBySubject(Principal principal) {
		logger.info("################ /project/grades/student/groupedbysubject/getAllStudentGroupedBySubject started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			List<SubjectGradesDto> grades= gradeRepository.findGradesWithSubjectByStudent(user.getId());
			Map<SubjectEntity, List<GradeEntity>> gradesBySubject = new HashMap<SubjectEntity, List<GradeEntity>>();
			for (SubjectGradesDto  entry : grades) {
				SubjectEntity subject = entry.getSubject();
			    GradeEntity grade = entry.getGrade();
			    List<GradeEntity> gradess = gradesBySubject.get(subject);
			    if (gradess == null) {
			        gradess = new ArrayList<GradeEntity>();
			        gradesBySubject.put(subject, gradess);
			    }
			    gradess.add(grade);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Map<SubjectEntity, List<GradeEntity>>>(gradesBySubject, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_STUDENT"})
	@JsonView(Views.Student.class)
	@RequestMapping(method = RequestMethod.GET, value = "/student/bysubject/{id}")
	public ResponseEntity<?> getByStudentAndSubjectStudent(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/student/bysubject/{id}/getByStudentAndSubjectStudent started.");
		logger.info("Logged user: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			StudentEntity user = studentRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(id, 1);
			List<GradeEntity> grade = null;
			if (user!=null && subject!=null)
				grade = gradeRepository.findByStudentAndSubject(user, subject);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<GradeEntity>>(grade, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("################ /project/grades/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newGrade == null) {
			logger.info("---------------- New grade is null.");
	        return new ResponseEntity<>("New grade is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newGrade.getStudent() == null && newGrade.getSubject() == null && newGrade.getGradeValue() == null && newGrade.getSemester() == null) {
			logger.info("---------------- Some data is null.");
	        return new ResponseEntity<>("Some data is null", HttpStatus.BAD_REQUEST);
		}
		GradeEntity grade = new GradeEntity();
		try {
			StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getStudent()), 1);
			if (student==null || student.getStatus()!=1) {
				logger.info("---------------- Student not exist.");
				return new ResponseEntity<>("Student not exist.", HttpStatus.NOT_FOUND);
			}
			logger.info("Student: " + student.getId().toString());
			SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getSubject()), 1);
			if (subject==null || subject.getStatus()!=1) {
				logger.info("---------------- Subject not exist.");
				return new ResponseEntity<>("Subject not exist.", HttpStatus.NOT_FOUND);
			}
			logger.info("Subject: " + subject.getId().toString());
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
			if (teacher==null) {
				logger.info("---------------- Teacher not exist.");
				return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
			}
			logger.info("Teacher: " + teacher.getId().toString());
			TeacherSubjectDepartmentEntity teacherDepartments = teacherSubjectDepartmentRepository.getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(teacher, subject, student);
			if (teacherDepartments==null) {
				logger.info("---------------- Not student teacher.");
				return new ResponseEntity<>("Not student teacher.", HttpStatus.NOT_ACCEPTABLE);
			}
			logger.info("Teacher subject department: " + teacherDepartments.getId().toString());
			grade = gradeDao.addNewGrade(teacher, student, teacherDepartments, newGrade);		
			if (grade != null) {
				for (ParentEntity p : student.getParents()) 
					if (p.getStatus() == 1) {
						EmailObject email = new EmailObject(p.getEmail(), "Nova ocena ucenika " + student.getFirstName() + " " + student.getLastName(), "Ucenik " + student.getFirstName() + " " + student.getLastName() + " je dobio ocenu " + grade.getGradeValue().toString() + " iz predmeta " + subject.getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
						emailService.sendSimpleMessage(email);
					}
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable String id, @Valid @RequestBody GradeDto newGrade, Principal principal, BindingResult result) {
		logger.info("################ /project/grades/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<>("Grade is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newGrade == null) {
			logger.info("---------------- New grade is null.");
	        return new ResponseEntity<>("New grade is null.", HttpStatus.BAD_REQUEST);
	      }
		GradeEntity ge = new GradeEntity();
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(Integer.parseInt(id), 1);
			if (grade==null) {
				logger.info("---------------- Grade not exist.");
				return new ResponseEntity<>("Grade not exist.", HttpStatus.NOT_FOUND);
			}
			if (newGrade.getStudent() != null && newGrade.getSubject() != null && newGrade.getSemester() != null && newGrade.getGradeValue() != null) {
				StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getStudent()), 1);
				if (student==null || student.getStatus()!=1) {
					logger.info("---------------- Student not exist.");
					return new ResponseEntity<>("Student not exist.", HttpStatus.NOT_FOUND);
				}
				logger.info("Student identified.");
				SubjectEntity subject = subjectRepository.findByIdAndStatusLike(Integer.parseInt(newGrade.getSubject()), 1);
				if (subject==null || subject.getStatus()!=1) {
					logger.info("---------------- Subject not exist.");
					return new ResponseEntity<>("Subject not exist.", HttpStatus.NOT_FOUND);
				}
				logger.info("Subject identified.");
				UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user identified.");
				UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
				logger.info("Logged user Access role identified.");
				TeacherEntity teacher = new TeacherEntity();
				Date updateDate = new Date();
				if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
					teacher = grade.getTeacher_subject_department().getTeaching_teacher();
					updateDate.setYear(updateDate.getYear()-1);
				}
				else {
					teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
					updateDate.setMinutes(updateDate.getMinutes()-30);
				}
				if (teacher==null) {
					logger.info("---------------- Teacher not exist.");
					return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
				}
				if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
					logger.info("---------------- Teacher doesn't match or time for change expired.");
					return new ResponseEntity<>("Teacher doesn't match or time for change expired.", HttpStatus.NOT_ACCEPTABLE);
				}
				logger.info("Teacher identified.");
				if (student == grade.getStudent() && teacher == grade.getTeacher_subject_department().getTeaching_teacher() && subject == grade.getTeacher_subject_department().getTeaching_subject() && updateDate.before(grade.getGradeMadeDate()) && !newGrade.getGradeValue().equals(grade.getGradeValue()))				
					ge = gradeDao.modifyGrade(loggedUser, grade, newGrade.getGradeValue());
				else {
					logger.info("---------------- Teacher, subject adn/or student doesn't match or time for change expired.");
					return new ResponseEntity<>("Teacher, subject adn/or student doesn't match or time for change expired.", HttpStatus.NOT_ACCEPTABLE);
				}
				if (ge != null) {
					for (ParentEntity p : student.getParents()) 
						if (p.getStatus() == 1) {
							EmailObject email = new EmailObject(p.getEmail(), "Promena ocene ucenika " + student.getFirstName() + " " + student.getLastName(), "Uceniku " + student.getFirstName() + " " + student.getLastName() + " je promenjena ocena na " + grade.getGradeValue().toString() + " iz predmeta " + subject.getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
							emailService.sendSimpleMessage(email);
						}
				}
				logger.info("---------------- Finished OK.");
			}
			return new ResponseEntity<>(ge, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/value/{value}")
	public ResponseEntity<?> modifyGradeById(@PathVariable Integer id, @PathVariable Integer value, Principal principal) {
		logger.info("################ /project/grades/{id}/modifyGradeById started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<>("Grade is null.", HttpStatus.BAD_REQUEST);
	      }
		if (value == null || value < 1 || value > 5) {
			logger.info("---------------- Grade value is null or out of range.");
	        return new ResponseEntity<>(" Grade value is null or out of range.", HttpStatus.BAD_REQUEST);
	      }
		GradeEntity ge = new GradeEntity();
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null) {
				logger.info("---------------- Grade not exist.");
				return new ResponseEntity<>("Grade not exist.", HttpStatus.NOT_FOUND);
			}			
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeaching_teacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not exist.");
				return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
			}
			if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for change expired.");
				return new ResponseEntity<>("Teacher doesn't match or time for change expired.", HttpStatus.NOT_ACCEPTABLE);
			}
			logger.info("Teacher identified.");
			if (teacher == grade.getTeacher_subject_department().getTeaching_teacher() && updateDate.before(grade.getGradeMadeDate()) && !value.equals(grade.getGradeValue()))
				ge = gradeDao.modifyGrade(loggedUser, grade, value);
			else {
				logger.info("---------------- Teacher, subject adn/or student doesn't match or time for change expired.");
				return new ResponseEntity<>("Teacher, subject adn/or student doesn't match or time for change expired.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (ge != null) {
				for (ParentEntity p : grade.getStudent().getParents()) 
					if (p.getStatus() == 1) {
						EmailObject email = new EmailObject(p.getEmail(), "Promena ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je promenjena ocena na " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeaching_subject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
						emailService.sendSimpleMessage(email);
					}
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(ge, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<>("Grade is null.", HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 0);
			if (grade==null || grade.getStatus()!=0) {
				logger.info("---------------- Grade not exist.");
				return new ResponseEntity<>("Grade not exist.", HttpStatus.NOT_FOUND);
			}
			logger.info("Grade identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeaching_teacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not exist.");
				return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
			}
			if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for delete expired.");
				return new ResponseEntity<>("Teacher doesn't match or time for delete expired.", HttpStatus.NOT_ACCEPTABLE);
			}
			logger.info("Teacher identified.");
			if (teacher == grade.getTeacher_subject_department().getTeaching_teacher() && updateDate.before(grade.getGradeMadeDate())) {
				gradeDao.setStatusActive(loggedUser, grade);
				for (ParentEntity p : grade.getStudent().getParents()) 
					if (p.getStatus() == 1) {
						EmailObject email = new EmailObject(p.getEmail(), "Aktiviranje obrisane ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je ponovo aktivirana ocena " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeaching_subject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
						emailService.sendSimpleMessage(email);
					}
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/archive/{id}/archive started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<>("Grade is null.", HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.getById(id);
			if (grade==null || grade.getStatus()==-1) {
				logger.info("---------------- Grade not exist.");
				return new ResponseEntity<>("Grade not exist.", HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			TeacherEntity teacher = grade.getTeacher_subject_department().getTeaching_teacher();
			Date updateDate = new Date();
			updateDate.setYear(updateDate.getYear()-1);
			if (teacher==null) {
				logger.info("---------------- Teacher not exist.");
				return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
			}
			if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for delete expired.");
				return new ResponseEntity<>("Teacher doesn't match or time for delete expired.", HttpStatus.NOT_ACCEPTABLE);
			}
			logger.info("Teacher identified.");
			if (teacher == grade.getTeacher_subject_department().getTeaching_teacher() && updateDate.before(grade.getGradeMadeDate()))
				gradeDao.setStatusArchived(loggedUser, grade);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("deprecation")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /project/grades/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		if (id == null) {
			logger.info("---------------- Grade is null.");
	        return new ResponseEntity<>("Grade is null.", HttpStatus.BAD_REQUEST);
	      }
		try {
			GradeEntity grade = gradeRepository.findByIdAndStatusLike(id, 1);
			if (grade==null || grade.getStatus()!=1) {
				logger.info("---------------- Grade not exist.");
				return new ResponseEntity<Object>("Grade not exist.", HttpStatus.NOT_FOUND);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user Access role identified.");
			TeacherEntity teacher = new TeacherEntity();
			Date updateDate = new Date();
			if (loggedUserAccount.getAccessRole().equals(EUserRole.ROLE_ADMIN)) {
				teacher = grade.getTeacher_subject_department().getTeaching_teacher();
				updateDate.setYear(updateDate.getYear()-1);
			}
			else {
				teacher = teacherRepository.findByIdAndStatusLike(loggedUser.getId(), 1);
				updateDate.setMinutes(updateDate.getMinutes()-30);
			}
			if (teacher==null) {
				logger.info("---------------- Teacher not exist.");
				return new ResponseEntity<>("Teacher not exist.", HttpStatus.NOT_FOUND);
			}
			if (teacher != grade.getTeacher_subject_department().getTeaching_teacher() || !updateDate.before(grade.getGradeMadeDate())) {
				logger.info("---------------- Teacher doesn't match or time for delete expired.");
				return new ResponseEntity<>("Teacher doesn't match or time for delete expired.", HttpStatus.NOT_ACCEPTABLE);
			}
			logger.info("Teacher identified.");
			gradeDao.setStatusDeleted(loggedUser, grade);
			for (ParentEntity p : grade.getStudent().getParents()) 
				if (p.getStatus() == 1) {
					EmailObject email = new EmailObject(p.getEmail(), "Brisanje ocene ucenika " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName(), "Uceniku " + grade.getStudent().getFirstName() + " " + grade.getStudent().getLastName() + " je obrisana ocena " + grade.getGradeValue().toString() + " iz predmeta " + grade.getTeacher_subject_department().getTeaching_subject().getSubjectName() + " kod profesora " + teacher.getFirstName() + " " + teacher.getLastName() + ".");
					emailService.sendSimpleMessage(email);
				}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<GradeEntity>(grade, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
