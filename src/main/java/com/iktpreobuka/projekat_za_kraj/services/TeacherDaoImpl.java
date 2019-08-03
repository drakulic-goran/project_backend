package com.iktpreobuka.projekat_za_kraj.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.PrimaryTeacherDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;

@Service
public class TeacherDaoImpl implements TeacherDao {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;
	
	@Autowired
	private PrimaryTeacherDepartmentRepository primaryTeacherDepartmentRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;

	@Autowired
	private UserRepository userRepository;

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	/*@Override
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
	}*/

	@Override
	public UserEntity addNewTeacher(UserEntity loggedUser, TeacherDto newTeacher) throws Exception {
			try {
				if (newTeacher.getjMBG() != null && teacherRepository.getByJMBG(newTeacher.getjMBG()) != null) {
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
				    Date employmentDate = formatter.parse(newTeacher.getEmploymentDate());
				    user.setEmploymentDate(employmentDate);
					//user.setEmploymentDate(Date.valueOf(newTeacher.getEmploymentDate()));
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
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("addNewTeacher save failed.");
		}
	}

	@Override
	public void modifyTeacher(UserEntity loggedUser, TeacherEntity teacher, TeacherDto updateTeacher) throws Exception {
		try {
			if (updateTeacher.getjMBG() != null && !updateTeacher.getjMBG().equals(" ") && !updateTeacher.getjMBG().equals("") && userRepository.getByJMBG(updateTeacher.getjMBG()) != null) {
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
			if (updateTeacher.getEmploymentDate() != null && !formatter.parse(updateTeacher.getEmploymentDate()).equals(teacher.getEmploymentDate()) && !updateTeacher.getEmploymentDate().equals(" ") && !updateTeacher.getEmploymentDate().equals("")) {
			    Date employmentDate = formatter.parse(updateTeacher.getEmploymentDate());
			    teacher.setEmploymentDate(employmentDate);
				//teacher.setEmploymentDate(Date.valueOf(updateTeacher.getEmploymentDate()));
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

	@Override
	public void addSubjectsToTeacher(UserEntity loggedUser, TeacherEntity user, List<String> subjects) throws Exception {
		try {
			for (String s : subjects) {
				SubjectEntity sub = subjectRepository.getById(Integer.parseInt(s));
				boolean contains = false;
				if (sub != null) {
					for (TeacherSubjectEntity ts : user.getSubjects()) {
						if (ts.getSubject() == sub && ts.getStatus() == 1) {
							contains = true;
						}
					}
				} else
					contains = true;
				if (!contains) {
					TeacherSubjectEntity teaching = new TeacherSubjectEntity(user, sub, new Date(), loggedUser.getId());
					teacherSubjectRepository.save(teaching);
					user.getSubjects().add(teaching);
					teacherRepository.save(user);
				}
			}
		} catch (Exception e) {
			throw new Exception("addSubjectsToTeacher failed on saving.");
		}
	}
	
	@Override
	public void addPrimaryDepartmentToTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception {
		try {
			DepartmentEntity dep = departmentRepository.getById(Integer.parseInt(primaryDepartment));
			boolean contains = false;
			if (dep != null) {
				for (PrimaryTeacherDepartmentEntity de : user.getDepartments()) {
					if (de.getDepartment() == dep && de.getStatus() == 1) {
						contains = true;
					}
				}
			} else
				contains = true;
			if (!contains) {
				PrimaryTeacherDepartmentEntity pt = new PrimaryTeacherDepartmentEntity(user, dep, new Date(), loggedUser.getId());
				primaryTeacherDepartmentRepository.save(pt);
				user.getDepartments().add(pt);
				teacherRepository.save(user);
			}
		} catch (Exception e) {
			throw new Exception("addPrimaryDepartmentToTeacher failed on saving.");
		}
	}
	
	@Override
	public void addSubjectsInDepartmentsToTeacher(UserEntity loggedUser, TeacherEntity user, List<Pair<String, String>> subject_at_department, String schoolYear) throws Exception {
		try {
			for (Pair<String, String> sd : subject_at_department) {
				SubjectEntity sub = subjectRepository.getById(Integer.parseInt(sd.left));
				DepartmentEntity dep = departmentRepository.getById(Integer.parseInt(sd.right));
				boolean contains = true;
				if (sub != null) {
					for (TeacherSubjectEntity ts : user.getSubjects()) {
						if (ts.getSubject() == sub && ts.getStatus() == 1) {
							contains = false;
						}
					}
				} 
				if (dep != null && !contains) {
					for (DepartmentClassEntity ds : dep.getClasses()) {
						if (ds.getStatus() == 1) {
							for (ClassSubjectEntity cs : ds.getClass_().getSubjects()) {
								if (cs.getSubject() == sub && cs.getStatus() == 1) {
									contains = false;
								}
							}
						}
					}
				}
				if (!contains) {
					for (TeacherSubjectDepartmentEntity tsd : user.getSubjects_departments()) {
						if (tsd.getTeaching_department() == dep && tsd.getTeaching_subject() == sub && tsd.getStatus() == 1) {
							contains = true;
						}
					}
				}
				if (!contains) {
					TeacherSubjectDepartmentEntity teaching = new TeacherSubjectDepartmentEntity(dep, sub, user, schoolYear, loggedUser.getId());
					teacherSubjectDepartmentRepository.save(teaching);
					user.getSubjects_departments().add(teaching);
					teacherRepository.save(user);
				}
			}
		} catch (Exception e) {
			throw new Exception("addSubjectsInDepartmentsToTeacher failed on saving.");
		}
	}

}