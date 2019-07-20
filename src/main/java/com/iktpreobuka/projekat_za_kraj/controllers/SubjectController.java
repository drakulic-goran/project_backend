package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.Date;
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
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
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
	private TeacherSubjectRepository teacherSubjectRepository;*/

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("This is an info message: /project/users/getAll started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			Iterable<SubjectEntity> subjects= subjectRepository.findAll();
			logger.info("This is an info message: getAll finished OK.");
			return new ResponseEntity<Iterable<SubjectEntity>>(subjects, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody SubjectDto newSubject, Principal principal, BindingResult result) {
		logger.info("This is an info message: /project/subjects/addNew started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
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
				subjectRepository.save(subject);
				logger.info("This is an info message: Subject created.");
			}
			return new ResponseEntity<>(subject, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/name/{name}")
	public ResponseEntity<?> modifySubjectName(@PathVariable String id, @PathVariable String name, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectName started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			}
			if (name != null)
				subject.setSubjectName(name);
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject modified.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/change/{id}/name/{number}")
	public ResponseEntity<?> modifySubjectWeekClassesNumber(@PathVariable String id, @PathVariable Integer number, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectWeekClassesNumber started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			if (subject==null) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			}
			if (number != null)
				subject.setWeekClassesNumber(number);
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject modified.");
			return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/subject/{id}/teacher/{t_id}")
	public ResponseEntity<?> addTeacherToSubject(@PathVariable String id, @PathVariable String t_id, Principal principal) {
		logger.info("This is an info message: /project/subjects/modifySubjectWeekClassesNumber started.");
		String loggedUser = principal.getName();
		logger.info("Logged user: " + loggedUser);
		try {
			SubjectEntity subject = subjectRepository.findById(Integer.parseInt(id)).orElse(null);
			TeacherEntity teacher = teacherRepository.findById(Integer.parseInt(t_id)).orElseGet(null);
			if (subject==null) {
				logger.info("This is an info message: Searched subject not exist.");
				return new ResponseEntity<SubjectEntity>(subject, HttpStatus.OK);
			} else if (teacher==null) {
				logger.info("This is an info message: Searched teacher not exist.");
				return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
			}
			Date assignmentDate = new Date();
			/*TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity(teacher, subject, assignmentDate);
			teacher.getSubjects().add(teacherSubject);
			subject.getTeachers().add(teacherSubject);*/
			teacher.addSubject(subject, assignmentDate);
			/* TeacherSubjectEntity teacherSubject = //new TeacherSubjectEntity(teacher, subject, assignmentDate);
			teacher.addSubject(subject, assignmentDate);
			teacherSubjectRepository.save(teacherSubject);*/
			teacherRepository.save(teacher);
			subjectRepository.save(subject);
			logger.info("This is an info message: Subject added to teacher.");
			return new ResponseEntity<TeacherEntity>(teacher, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("This is an number format exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
