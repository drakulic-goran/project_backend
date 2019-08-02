package com.iktpreobuka.projekat_za_kraj.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class TeacherDaoImpl implements TeacherDao {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public TeacherEntity findById(Integer id) throws Exception{
		try {
			return teacherRepository.getById(id);
		} catch (Exception e) {
			throw new Exception("Get teacher by Id failed.");
		}
	}
	
	@Override
	public TeacherEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception {
		try {
			return teacherRepository.findByIdAndStatusLike(id, status);
		} catch (Exception e) {
			throw new Exception("Get teacher by Id and Status failed.");
		}
	}

	@Override
	public Iterable<TeacherEntity> findByStatusLike(Integer status) throws Exception {
		try {
			return teacherRepository.findByStatusLike(status);
		} catch (Exception e) {
			throw new Exception("Get teacher by Status failed.");
		}		
	}

	@Override
	public UserEntity addNewTeacher(UserEntity loggedUser, TeacherDto newTeacher) throws Exception {
			try {
				if (newTeacher.getjMBG() != null && teacherRepository.getByJMBGAndStatusLike(newTeacher.getjMBG(), 1) != null) {
				     throw new Exception("JMBG already exists.");
				}
			} catch (Exception e) {
				throw new Exception("addNewTeacher TeacherDto check failed.");
			}
			UserEntity temporaryUser = new TeacherEntity();
			try {
				temporaryUser = userRepository.findByJMBG(newTeacher.getjMBG());
				if (temporaryUser != null && (!temporaryUser.getFirstName().equals(newTeacher.getFirstName()) || !temporaryUser.getLastName().equals(newTeacher.getLastName()) || !temporaryUser.getGender().toString().equals(newTeacher.getGender()) || !temporaryUser.getjMBG().equals(newTeacher.getjMBG()))) {
					throw new Exception("User exists, but import data not same as exist user data.");
				}
			} catch (Exception e1) {
				throw new Exception("addNewTeacher Exist user check failed.");
			}
			TeacherEntity user = new TeacherEntity();
		try {
			if (temporaryUser == null) {
				try {
					user.setFirstName(newTeacher.getFirstName());
					user.setLastName(newTeacher.getLastName());
					user.setjMBG(newTeacher.getjMBG());
					user.setGender(EGender.valueOf(newTeacher.getGender()));
					user.setEmploymentDate(Date.valueOf(newTeacher.getEmploymentDate()));
					user.setCertificate(newTeacher.getCertificate());
					user.setRole(EUserRole.ROLE_TEACHER);
					user.setStatusActive();
					user.setCreatedById(loggedUser.getId());
					teacherRepository.save(user);
					temporaryUser = user;
				} catch (Exception e) {
					throw new Exception("addNewTeacher save failed.");
				}
			} else {
				teacherRepository.addAdminFromExistUser(newTeacher.getCertificate(), newTeacher.getEmploymentDate(), temporaryUser.getId(), loggedUser.getId());
			}
		} catch (Exception e) {
			throw new Exception("addNewTeacher save failed.");
		}
		return temporaryUser;
	}

	@Override
	public void modifyTeacher(UserEntity loggedUser, TeacherEntity teacher, TeacherDto updateTeacher) throws Exception {
		try {
			if (updateTeacher.getjMBG() != null && teacherRepository.getByJMBGAndStatusLike(updateTeacher.getjMBG(), 1) != null) {
			     throw new Exception("JMBG already exists.");
			}
			if (updateTeacher.getAccessRole() != null && updateTeacher.getAccessRole() != "ROLE_TEACHER") {
			     throw new Exception("Access role must be ROLE_TEACHER.");
			}
		} catch (Exception e1) {
			throw new Exception("modifyTeacher TeacherDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateTeacher.getFirstName() != null && !updateTeacher.getFirstName().equals(" ") && !updateTeacher.getFirstName().equals("") && !updateTeacher.getFirstName().equals(teacher.getFirstName())) {
				teacher.setFirstName(updateTeacher.getFirstName());
				i++;
			}
			if (updateTeacher.getLastName() != null && !updateTeacher.getLastName().equals(teacher.getLastName()) && !updateTeacher.getLastName().equals(" ") && !updateTeacher.getLastName().equals("")) {
				teacher.setLastName(updateTeacher.getLastName());
				i++;
			}
			if (updateTeacher.getjMBG() != null && !updateTeacher.getjMBG().equals(teacher.getjMBG()) && !updateTeacher.getjMBG().equals(" ") && !updateTeacher.getjMBG().equals("")) {
				teacher.setjMBG(updateTeacher.getjMBG());
				i++;
			}
			if (updateTeacher.getGender() != null && EGender.valueOf(updateTeacher.getGender()) != teacher.getGender() && (EGender.valueOf(updateTeacher.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updateTeacher.getGender()) == EGender.GENDER_MALE)) {
				teacher.setGender(EGender.valueOf(updateTeacher.getGender()));
				i++;
			}
			if (updateTeacher.getCertificate() != null && !updateTeacher.getCertificate().equals(teacher.getCertificate()) && !updateTeacher.getCertificate().equals(" ") && !updateTeacher.getCertificate().equals("")) {
				teacher.setCertificate(updateTeacher.getCertificate());
				i++;
			}
			if (updateTeacher.getEmploymentDate() != null && !updateTeacher.getEmploymentDate().equals(teacher.getEmploymentDate()) && !updateTeacher.getEmploymentDate().equals(" ") && !updateTeacher.getEmploymentDate().equals("")) {
				teacher.setEmploymentDate(Date.valueOf(updateTeacher.getEmploymentDate()));
				i++;
			}
			if (i>0) {
				teacher.setUpdatedById(loggedUser.getId());
				teacherRepository.save(teacher);
			}
		} catch (Exception e) {
			throw new Exception("modifyTeacher faild on saving.");
		}
	}

	@Override
	public void deleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception {
		try {
			teacher.setStatusInactive();
			teacher.setUpdatedById(loggedUser.getId());
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new Exception("deleteTeacher failed on saving.");
		}
	}
	
	@Override
	public void undeleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception {
		try {
			teacher.setStatusActive();
			teacher.setUpdatedById(loggedUser.getId());
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new Exception("undeleteTeacher failed on saving.");
		}		
	}
	
	@Override
	public void archiveDeletedTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception {
		try {
			teacher.setStatusArchived();
			teacher.setUpdatedById(loggedUser.getId());
			teacherRepository.save(teacher);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedAdmin failed on saving.");
		}		
	}

}