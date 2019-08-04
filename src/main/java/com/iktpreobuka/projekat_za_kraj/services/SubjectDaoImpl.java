package com.iktpreobuka.projekat_za_kraj.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.SubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectRepository;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;

@Service
public class SubjectDaoImpl implements SubjectDao {

	
	@Autowired
	private ClassSubjectRepository classSubjectRepository;
	
	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;
	
	
	
	@Override
	public SubjectEntity addNewSubject(UserEntity loggedUser, SubjectDto newSubject) throws Exception {
		try {
			if (newSubject.getSubjectName() == null || newSubject.getWeekClassesNumber() == null) {
			     throw new Exception("Some data is null.");
			}
		} catch (Exception e) {
			throw new Exception("SubjectDto check failed.");
		}
		SubjectEntity subject = new SubjectEntity();
		try {	
			subject.setSubjectName(newSubject.getSubjectName());
			subject.setWeekClassesNumber(newSubject.getWeekClassesNumber());
			subject.setStatusActive();
			subject.setCreatedById(loggedUser.getId());
			subjectRepository.save(subject);
			return subject;
		} catch (Exception e) {
			throw new Exception("addNewSubject save failed.");
		}
	}
	
	@Override
	public void modifySubject(UserEntity loggedUser, SubjectEntity subject, SubjectDto updateSubject) throws Exception {
		try {
			if (updateSubject.getSubjectName() == null && updateSubject.getWeekClassesNumber() == null) {
			     throw new Exception("All data is null.");
			}
		} catch (Exception e) {
			throw new Exception("SubjectDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateSubject.getSubjectName() != null && !updateSubject.getSubjectName().equals(" ") && !updateSubject.getSubjectName().equals("")) {
				subject.setSubjectName(updateSubject.getSubjectName());
				i++;
			}
			if (updateSubject.getWeekClassesNumber() != null || !updateSubject.getWeekClassesNumber().equals(" ") || !updateSubject.getWeekClassesNumber().equals("")) {
				subject.setWeekClassesNumber(updateSubject.getWeekClassesNumber());
				i++;
			}
			if (i>0) {
				subject.setUpdatedById(loggedUser.getId());
				subjectRepository.save(subject);
			}
		} catch (Exception e) {
			throw new Exception("modifySubject failed on saving.");
		}
	}
	
	@Override
	public void deleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {		
			for (ClassSubjectEntity cs : subject.getClasses()) {
				if (cs.getStatus() == 1) {
					cs.setStatusInactive();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			for (TeacherSubjectEntity ts : subject.getTeachers()) {
				if (ts.getStatus() == 1) {
					ts.setStatusInactive();
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusInactive();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			subject.setStatusInactive();
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("deleteSubject failed on saving.");
		}
	}
	
	@Override
	public void undeleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {
			subject.setStatusActive();
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("undeleteSubject failed on saving.");
		}		
	}
	
	@Override
	public void archiveSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception {
		try {
			for (ClassSubjectEntity cs : subject.getClasses()) {
				if (cs.getStatus() == 1) {
					cs.setStatusArchived();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			for (TeacherSubjectEntity ts : subject.getTeachers()) {
				if (ts.getStatus() == 1) {
					ts.setStatusArchived();
					ts.setUpdatedById(loggedUser.getId());
					teacherSubjectRepository.save(ts);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusArchived();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			subject.setStatusArchived();;
			subject.setUpdatedById(loggedUser.getId());
			subjectRepository.save(subject);
		} catch (Exception e) {
			throw new Exception("archiveSubject failed on saving.");
		}		
	}
	
	@Override
	public void addClassToSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject, String learningProgram) throws Exception {
		try {
			for (String t : classes) {
				ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
				if (class_==null)
					throw new Exception("Class not exist.");
				boolean contains = false;
				if (learningProgram != null && class_.getStatus() == 1 && subject.getStatus() == 1) {
					for (ClassSubjectEntity cs : subject.getClasses()) {
						if (cs.getClass_() == class_ && cs.getStatus() == 1) {
							contains = true;
						}
					}
				} else
					contains = true;
				if (!contains) {
					ClassSubjectEntity classSubject = new ClassSubjectEntity(class_, subject, learningProgram, loggedUser.getId());
					classSubjectRepository.save(classSubject);
					subject.getClasses().add(classSubject);
					subject.setUpdatedById(loggedUser.getId());
					subjectRepository.save(subject);
				}
			}
			} catch (Exception e) {
				throw new Exception("addClassToSubject failed on saving.");
			}
		}
		
	@Override
	public void removeClassFromSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject) throws Exception {
		try {
			for (String t : classes) {
				ClassEntity class_ = classRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
				if (class_==null)
					throw new Exception("Class not exist.");
				if (class_.getStatus() == 1 && subject.getStatus() == 1) {
					for (ClassSubjectEntity cs : subject.getClasses()) {
						if (cs.getClass_() == class_ && cs.getStatus() == 1) {
							cs.setStatusInactive();
							cs.setUpdatedById(loggedUser.getId());
							classSubjectRepository.save(cs);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removeSubjectFromClass failed on saving.");
		}

	}

	@Override
	public void addTeachersToSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception {
		try {
			if (subject !=null && subject.getStatus() ==1 && teachers !=null && !teachers.equals("") && !teachers.equals(" ")) {
				for (String t : teachers) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					boolean contains = false;
					for (TeacherSubjectEntity ts : subject.getTeachers()) {
						if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
							contains = true;
						}
					}
					if (!contains) {
						TeacherSubjectEntity teaching = new TeacherSubjectEntity(teacher, subject, new Date(), loggedUser.getId());
						teacherSubjectRepository.save(teaching);
						subject.getTeachers().add(teaching);
						subject.setUpdatedById(loggedUser.getId());
						subjectRepository.save(subject);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("addTeachersToSubject failed on saving.");
		}
	}
	
	@Override
	public void removeTeachersFromSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception {
		try {
			if (subject !=null && subject.getStatus() ==1 && teachers !=null && !teachers.equals("") && !teachers.equals(" ")) {
				for (String t : teachers) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(t), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					for (TeacherSubjectEntity ts : subject.getTeachers()) {
						if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
							ts.setStatusInactive();
							ts.setUpdatedById(loggedUser.getId());
							teacherSubjectRepository.save(ts);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removeTeachersFromSubject failed on saving.");
		}
	}

	@Override
	public void addTeacherAndDepartmentToSubject(UserEntity loggedUser, SubjectEntity subject, List<Pair<String, String>> teacher_at_department, String school_year) throws Exception {
		try {
			if (subject !=null && subject.getStatus() ==1 && teacher_at_department !=null && !teacher_at_department.equals("") && !teacher_at_department.equals(" ") && school_year !=null && !school_year.equals("") && !school_year.equals(" ")) {
				for (Pair<String, String> sd : teacher_at_department) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(sd.left), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(sd.right), 1);
					if (department==null)
						throw new Exception("Department not exist.");
					boolean contains = false;
					for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
						if (tsd.getTeaching_teacher() == teacher && tsd.getTeaching_subject() == subject && tsd.getStatus() == 1) {
							contains = true;
						}
					}
					if (!contains) {
						contains = true;
						for (TeacherSubjectEntity ts : subject.getTeachers()) {
							if (ts.getTeacher() == teacher && ts.getStatus() == 1) {
								contains = false;
							}
						}
					}
					if (!contains) {
						contains = true;
						for (ClassSubjectEntity ds : subject.getClasses()) {
							if (ds.getStatus() == 1) {
								for (DepartmentClassEntity cs : ds.getClass_().getDepartments()) {
									if (cs.getDepartment() == department && cs.getStatus() == 1) {
										contains = false;
									}
								}
							}
						}
					}
					if (!contains) {
						TeacherSubjectDepartmentEntity teaching = new TeacherSubjectDepartmentEntity(department, subject, teacher, school_year, loggedUser.getId());
						teacherSubjectDepartmentRepository.save(teaching);
						subject.getTeachers_departments().add(teaching);
						subject.setUpdatedById(loggedUser.getId());
						subjectRepository.save(subject);
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("addTeacherAndDepartmentToSubject failed on saving.");
		}
	}
	
	@Override
	public void removeTeacherAndDepartmentFromSubject(UserEntity loggedUser, SubjectEntity subject, List<Pair<String, String>> teacher_at_department) throws Exception {
		try {
			if (subject !=null && subject.getStatus() ==1 && teacher_at_department !=null && !teacher_at_department.equals("") && !teacher_at_department.equals(" ")) {
				for (Pair<String, String> sd : teacher_at_department) {
					TeacherEntity teacher = teacherRepository.findByIdAndStatusLike(Integer.parseInt(sd.left), 1);
					if (teacher==null)
						throw new Exception("Teacher not exist.");
					DepartmentEntity department = departmentRepository.findByIdAndStatusLike(Integer.parseInt(sd.right), 1);
					if (department==null)
						throw new Exception("Department not exist.");
					for (TeacherSubjectDepartmentEntity tsd : subject.getTeachers_departments()) {
						if (tsd.getTeaching_teacher() == teacher && tsd.getTeaching_department() == department && tsd.getStatus() == 1) {
							tsd.setStatusInactive();
							tsd.setUpdatedById(loggedUser.getId());
							teacherSubjectDepartmentRepository.save(tsd);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("removeTeacherAndDepartmentFromSubject failed on saving.");
		}
	}
	
}
