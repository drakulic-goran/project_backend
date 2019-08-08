package com.iktpreobuka.projekat_za_kraj.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
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
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;
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
	private DepartmentClassRepository departmentClassRepository;
	
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
				if (newTeacher.getFirstName() == null || newTeacher.getLastName() == null || newTeacher.getCertificate() == null || newTeacher.getEmploymentDate() == null || newTeacher.getGender() == null || newTeacher.getjMBG() == null ) {
				     throw new Exception("Some data is null.");
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
			if (updateTeacher.getFirstName() == null && updateTeacher.getLastName() == null && updateTeacher.getCertificate() == null && updateTeacher.getEmploymentDate() == null && updateTeacher.getGender() == null && updateTeacher.getjMBG() == null ) {
			     throw new Exception("All data is null.");
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
			for (TeacherSubjectEntity ts : teacher.getSubjects()) {
				if (ts.getStatus() == 1) {
					ts.setStatusInactive();;
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : teacher.getDepartments()) {
				if (ptd.getStatus() == 1) {
					ptd.setStatusInactive();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : teacher.getSubjects_departments()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusInactive();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
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
	public void archiveTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception {
		try {
			for (TeacherSubjectEntity ts : teacher.getSubjects()) {
				if (ts.getStatus() != -1) {
					ts.setStatusArchived();;
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : teacher.getDepartments()) {
				if (ptd.getStatus() != -1) {
					ptd.setStatusArchived();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : teacher.getSubjects_departments()) {
				if (tsd.getStatus() != -1) {
					tsd.setStatusArchived();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
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
			if (user !=null && user.getStatus() == 1 && !subjects.isEmpty()) {
				for (String s : subjects) {
					if (s !=null && !s.equals("") && !s.equals(" ")) {					
						SubjectEntity sub = subjectRepository.findByIdAndStatusLike(Integer.parseInt(s), 1);
						if (sub==null)
							throw new Exception("Subject not exist.");
						boolean contains = false;
						for (TeacherSubjectEntity ts : user.getSubjects()) {
							if (ts.getSubject() == sub && ts.getStatus() == 1) {
								contains = true;
							}
						}
						if (!contains) {
							TeacherSubjectEntity teaching = new TeacherSubjectEntity(user, sub, new Date(), loggedUser.getId());
							teacherSubjectRepository.save(teaching);
							user.getSubjects().add(teaching);
							user.setUpdatedById(loggedUser.getId());
							teacherRepository.save(user);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("addSubjectsToTeacher failed on saving.");
		}
	}
	
	@Override
	public void removeSubjectsFromTeacher(UserEntity loggedUser, TeacherEntity user, List<String> subjects) throws Exception {
		try {
			if (user !=null && user.getStatus() == 1 && !subjects.isEmpty()) {
				for (String s : subjects) {
					if (s !=null && !s.equals("") && !s.equals(" ")) {					
						SubjectEntity sub = subjectRepository.findByIdAndStatusLike(Integer.parseInt(s), 1);
						if (sub==null)
							throw new Exception("Subject not exist.");
						for (TeacherSubjectEntity ts : user.getSubjects()) {
							if (ts.getSubject() == sub && ts.getStatus() == 1) {
								ts.setStatusInactive();
								ts.setUpdatedById(loggedUser.getId());
								teacherSubjectRepository.save(ts);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removeSubjectsFromTeacher failed on saving.");
		}
	}

	@Override
	public void addPrimaryDepartmentToTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception {
		try {
			if (user !=null && user.getStatus() == 1 && primaryDepartment !=null && !primaryDepartment.equals("") && !primaryDepartment.equals(" ")) {
				DepartmentEntity dep = departmentRepository.findByIdAndStatusLike(Integer.parseInt(primaryDepartment), 1);
				if (dep==null)
					throw new Exception("Department not exist.");
				boolean contains = false;
				for (PrimaryTeacherDepartmentEntity de : user.getDepartments()) {
					if (de.getStatus() == 1) {
						if (de.getDepartment() == dep) {
							contains = true;
						} else {
							de.setStatusInactive();
							de.setUpdatedById(loggedUser.getId());
							primaryTeacherDepartmentRepository.save(de);
						}
					}
				}
				if (!contains) {
					PrimaryTeacherDepartmentEntity pt = new PrimaryTeacherDepartmentEntity(user, dep, new Date(), loggedUser.getId());
					primaryTeacherDepartmentRepository.save(pt);
					user.getDepartments().add(pt);
					user.setUpdatedById(loggedUser.getId());
					teacherRepository.save(user);
				}
			}
		} catch (Exception e) {
			throw new Exception("addPrimaryDepartmentToTeacher failed on saving.");
		}
	}

	@Override
	public void removePrimaryDepartmentFromTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception {
		try {
			if (user !=null && user.getStatus() ==1 && primaryDepartment !=null && !primaryDepartment.equals("") && !primaryDepartment.equals(" ")) {
				DepartmentEntity dep = departmentRepository.findByIdAndStatusLike(Integer.parseInt(primaryDepartment), 1);
				if (dep==null)
					throw new Exception("Department not exist.");
				for (PrimaryTeacherDepartmentEntity de : user.getDepartments()) {
					if (de.getStatus() == 1 && de.getDepartment() == dep) {
						de.setStatusInactive();
						de.setUpdatedById(loggedUser.getId());
						primaryTeacherDepartmentRepository.save(de);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removePrimaryDepartmentFromTeacher failed on saving.");
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void addSubjectsInDepartmentsToTeacher(UserEntity loggedUser, TeacherEntity user, List<Pair<String, String>> subject_at_department, String schoolYear) throws Exception {
		try {
			if (user !=null && user.getStatus() ==1 && !subject_at_department.isEmpty() && schoolYear !=null && !schoolYear.equals("") && !schoolYear.equals(" ")) {
				for (Pair<String, String> sd : subject_at_department) {
					if (sd !=null && !sd.equals("") && !sd.equals(" ")) {
						SubjectEntity sub = subjectRepository.findByIdAndStatusLike(Integer.parseInt(sd.left), 1);
						if (sub==null)
							throw new Exception("Subject not exist.");
						DepartmentEntity dep = departmentRepository.findByIdAndStatusLike(Integer.parseInt(sd.right), 1);
						if (dep==null)
							throw new Exception("Department not exist.");
						ClassEntity clas = departmentClassRepository.getByDepartmentAndStatusLike(dep, 1);
						if (clas==null)
							throw new Exception("Class for selected department not exist.");
						boolean contains = false;
						for (TeacherSubjectDepartmentEntity tsd : user.getSubjects_departments()) {
							if (tsd.getTeachingDepartment() == dep && tsd.getTeachingClass() == clas && tsd.getTeachingSubject() == sub && tsd.getStatus() == 1) {
								contains = true;
							}
						}
						if (!contains) {
							contains = true;
							for (TeacherSubjectEntity ts : user.getSubjects()) {
								if (ts.getSubject() == sub && ts.getStatus() == 1) {
									contains = false;
								}
							}
						}
						if (!contains) {
							contains = true;
							for (ClassSubjectEntity cs : clas.getSubjects()) {
								if (cs.getSubject() == sub && cs.getStatus() == 1) {
									contains = false;
								}
							}
						}
						if (!contains) {
							TeacherSubjectDepartmentEntity teaching = new TeacherSubjectDepartmentEntity(dep, clas, sub, user, schoolYear, loggedUser.getId());
							teacherSubjectDepartmentRepository.save(teaching);
							user.getSubjects_departments().add(teaching);
							user.setUpdatedById(loggedUser.getId());
							teacherRepository.save(user);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("addSubjectsInDepartmentsToTeacher failed on saving.");
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void removeSubjectsInDepartmentsFromTeacher(UserEntity loggedUser, TeacherEntity user, List<Pair<String, String>> subject_at_department) throws Exception {
		try {
			if (user !=null && user.getStatus() ==1 && !subject_at_department.isEmpty()) {
				for (Pair<String, String> sd : subject_at_department) {
					if (sd !=null && !sd.equals("") && !sd.equals(" ")) {
						SubjectEntity sub = subjectRepository.findByIdAndStatusLike(Integer.parseInt(sd.left), 1);
						if (sub==null)
							throw new Exception("Subject not exist.");
						DepartmentEntity dep = departmentRepository.findByIdAndStatusLike(Integer.parseInt(sd.right), 1);
						if (dep==null)
							throw new Exception("Department not exist.");
						ClassEntity clas = departmentClassRepository.getByDepartmentAndStatusLike(dep, 1);
						if (clas==null)
							throw new Exception("Class for selected department not exist.");
						for (TeacherSubjectDepartmentEntity tsd : user.getSubjects_departments()) {
							if (tsd.getTeachingDepartment() == dep && tsd.getTeachingClass() == clas && tsd.getTeachingSubject() == sub && tsd.getStatus() == 1) {
								tsd.setStatusInactive();
								tsd.setUpdatedById(loggedUser.getId());
								teacherSubjectDepartmentRepository.save(tsd);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removeSubjectsInDepartmentsFromTeacher failed on saving.");
		}
	}

}