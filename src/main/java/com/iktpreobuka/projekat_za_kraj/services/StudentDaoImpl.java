package com.iktpreobuka.projekat_za_kraj.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.StudentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class StudentDaoImpl implements StudentDao {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public StudentEntity findById(Integer id) throws Exception {
		try {
			return studentRepository.getById(id);
		} catch (Exception e) {
			throw new Exception("Get student by Id failed.");
		}
	}
	
	@Override
	public StudentEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception {
		try {
			return studentRepository.findByIdAndStatusLike(id, status);
		} catch (Exception e) {
			throw new Exception("Get student by Id and Status failed.");
		}
	}

	@Override
	public Iterable<StudentEntity> findByStatusLike(Integer status) throws Exception {
		try {
			return studentRepository.findByStatusLike(status);
		} catch (Exception e) {
			throw new Exception("Get student by Status failed.");
		}		
	}
	
	@Override
	public UserEntity addNewStudent(UserEntity loggedUser, StudentDto newStudent) throws Exception {
		try {
			if (newStudent.getjMBG() != null && studentRepository.getByJMBGAndStatusLike(newStudent.getjMBG(), 1) != null) {
			     throw new Exception("JMBG already exists.");
			}
			if (newStudent.getSchoolIdentificationNumber() != null && studentRepository.getBySchoolIdentificationNumberAndStatusLike(newStudent.getSchoolIdentificationNumber(), 1) != null) {
		         throw new Exception("E-mail already exists.");
			}
		} catch (Exception e) {
			throw new Exception("addNewStudent StudentDto check failed.");
		}
		UserEntity temporaryUser = new TeacherEntity();
		try {
			temporaryUser = userRepository.findByJMBG(newStudent.getjMBG());
			if (temporaryUser != null && (!temporaryUser.getFirstName().equals(newStudent.getFirstName()) || !temporaryUser.getLastName().equals(newStudent.getLastName()) || !temporaryUser.getGender().toString().equals(newStudent.getGender()) || !temporaryUser.getjMBG().equals(newStudent.getjMBG()))) {
				throw new Exception("User exists, but import data not same as exist user data.");
			}
		} catch (Exception e1) {
			throw new Exception("addNewStudent Exist user check failed.");
		}
		StudentEntity user = new StudentEntity();
	try {
		if (temporaryUser == null) {
			try {
				user.setFirstName(newStudent.getFirstName());
				user.setLastName(newStudent.getLastName());
				user.setjMBG(newStudent.getjMBG());
				user.setGender(EGender.valueOf(newStudent.getGender()));
				user.setEnrollmentDate(Date.valueOf(newStudent.getEnrollmentDate()));
				user.setSchoolIdentificationNumber(newStudent.getSchoolIdentificationNumber());
				user.setRole(EUserRole.ROLE_STUDENT);
				user.setStatusActive();
				user.setCreatedById(loggedUser.getId());
				studentRepository.save(user);
				temporaryUser = user;
			} catch (Exception e) {
				throw new Exception("addNewStudent save failed.");
			}
		} else {
			studentRepository.addAdminFromExistUser(newStudent.getEnrollmentDate(), newStudent.getSchoolIdentificationNumber(), temporaryUser.getId(), loggedUser.getId());
		}
	} catch (Exception e) {
		throw new Exception("addNewStudent save failed.");
	}
	return temporaryUser;
	}

	@Override
	public void modifyStudent(UserEntity loggedUser, StudentEntity student, StudentDto updateStudent) throws Exception {
		if (updateStudent.getjMBG() != null && studentRepository.getByJMBG(updateStudent.getjMBG()) != null) {
	         throw new Exception("JMBG already exists.");
		}
		if (updateStudent.getAccessRole() != null && !updateStudent.getAccessRole().equals("ROLE_STUDENT")) {
	         throw new Exception("Access role must be ROLE_STUDENT.");
		}		
		try {
			Integer i = 0;
			System.out.println(i);
			if (updateStudent.getFirstName() != null && !updateStudent.getFirstName().equals(" ") && !updateStudent.getFirstName().equals("") && !updateStudent.getFirstName().equals(student.getFirstName())) {
				student.setFirstName(updateStudent.getFirstName());
				i++;
			}
			if (updateStudent.getLastName() != null && !updateStudent.getLastName().equals(student.getLastName()) && !updateStudent.getLastName().equals(" ") && !updateStudent.getLastName().equals("")) {
				student.setLastName(updateStudent.getLastName());
				i++;
			}
			if (updateStudent.getjMBG() != null && !updateStudent.getjMBG().equals(student.getjMBG()) && !updateStudent.getjMBG().equals(" ") && !updateStudent.getjMBG().equals("")) {
				student.setjMBG(updateStudent.getjMBG());
				i++;
			}
			if (updateStudent.getGender() != null && EGender.valueOf(updateStudent.getGender()) != student.getGender() && (EGender.valueOf(updateStudent.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updateStudent.getGender()) == EGender.GENDER_MALE)) {
				student.setGender(EGender.valueOf(updateStudent.getGender()));
				i++;
			}
			if (updateStudent.getSchoolIdentificationNumber() != null && !updateStudent.getSchoolIdentificationNumber().equals(student.getSchoolIdentificationNumber()) && !updateStudent.getSchoolIdentificationNumber().equals(" ") && !updateStudent.getSchoolIdentificationNumber().equals("")) {
				student.setSchoolIdentificationNumber(updateStudent.getSchoolIdentificationNumber());
				i++;
			}
			if (updateStudent.getEnrollmentDate() != null && !Date.valueOf(updateStudent.getEnrollmentDate()).equals(student.getEnrollmentDate()) && !updateStudent.getEnrollmentDate().equals(" ") && !updateStudent.getEnrollmentDate().equals("")) {
				student.setEnrollmentDate(Date.valueOf(updateStudent.getEnrollmentDate()));
				i++;
			}
			if (i>0) {
				student.setUpdatedById(loggedUser.getId());
				studentRepository.save(student);
			}
		} catch (Exception e) {
			throw new Exception("ModifyStudent failed on saving.");
		}
	}

	@Override
	public void deleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			student.setStatusInactive();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("DeleteStudent failed on saving.");
		}
	}
	
	@Override
	public void undeleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			student.setStatusActive();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("UndeleteStudent failed on saving.");
		}		
	}
	
	@Override
	public void archiveDeletedStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			student.setStatusArchived();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedStudent failed on saving.");
		}		
	}
	
	@Override
	public void addParentToStudent(StudentEntity student, ParentEntity parent) throws Exception {
		try {
			student.getParents().add(parent);
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("addParentToStudent failed on saving.");
		}

	}
	
}
