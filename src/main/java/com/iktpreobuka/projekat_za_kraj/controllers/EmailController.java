package com.iktpreobuka.projekat_za_kraj.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.models.EmailObject;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.security.Views;
import com.iktpreobuka.projekat_za_kraj.services.EmailService;

@Controller
@RestController
@RequestMapping(path = "/project/email")
public class EmailController{

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	//private static String PATH_TO_ATTACHMENT= "D://proba//slika.jpg";
	
	private final Logger logger= (Logger) LoggerFactory.getLogger(this.getClass());


	@Secured("ROLE_TEACHER")
	@JsonView(Views.Teacher.class)
	@RequestMapping(method = RequestMethod.POST, value = "/teacher")
	public String sendMessageToParents(@RequestBody EmailObject object, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());
		UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
		logger.info("Logged user identified.");

		if(object==null|| object.getSubject()==null|| object.getText()==null) {
			return null;
		}
		
		if (object.getTo() == null) {
			List<StudentEntity> students = studentRepository.findByPrimaryTeacherId(loggedUser.getId());
			for (StudentEntity student : students) {
				for (ParentEntity p : student.getParents()) 
					if (p.getStatus() == 1) {
						object.setTo(p.getEmail());
						emailService.sendSimpleMessage(object);
					}
			}
		} else {
			StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(object.getTo()), 1);
			for (ParentEntity p : student.getParents()) 
					if (p.getStatus() == 1) {
						object.setTo(p.getEmail());
						emailService.sendSimpleMessage(object);
					}
		}
		
		return "Your mail has been sent!";
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/admin")
	public String sendMessageToAll(@RequestBody EmailObject object, Principal principal) {
		logger.info("################ /project/grades/parent/{id}/getByIdParent started.");
		logger.info("Logged user: " + principal.getName());

		if(object==null|| object.getSubject()==null|| object.getText()==null) {
			return null;
		}
		
		if (object.getTo() == null) {
			Iterable<StudentEntity> students = studentRepository.findByStatusLike(1);
			for (StudentEntity student : students) {
				for (ParentEntity p : student.getParents()) 
					if (p.getStatus() == 1) {
						object.setTo(p.getEmail());
						emailService.sendSimpleMessage(object);
					}
			}
		} else {
			StudentEntity student = studentRepository.findByIdAndStatusLike(Integer.parseInt(object.getTo()), 1);
			for (ParentEntity p : student.getParents())
					if (p.getStatus() == 1) {
						object.setTo(p.getEmail());
						emailService.sendSimpleMessage(object);
					}
		}
		
		return "Your mail has been sent!";
	}

/*	@RequestMapping(method = RequestMethod.POST, value = "/templateEmail")
	public String sendTemplateMessage(@RequestBody EmailObject object) throws Exception {
		if(object==null|| object.getTo()==null|| object.getText()==null) {
			return null;
		}
		emailService.sendTemplateMessage(object);
		return "Your mail has been sent!";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/emailWithAttachment")
	public String sendMessageWithAttachment(@RequestBody EmailObject object) throws Exception {
		if(object==null|| object.getTo()==null|| object.getText()==null) {
			return null;
		}
		emailService.sendMessageWithAttachment(object, PATH_TO_ATTACHMENT);
		return "Your mail has been sent!";
	}
*/
	
}