package com.iktpreobuka.projekat_za_kraj.services;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.PrimaryTeacherDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.PrimaryTeacherDepartmentRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.TeacherSubjectDepartmentRepository;

@Service
public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private DepartmentClassRepository departmentClassRepository;
	
	@Autowired
	private PrimaryTeacherDepartmentRepository primaryTeacherDepartmentRepository;
	
	@Autowired
	private TeacherSubjectDepartmentRepository teacherSubjectDepartmentRepository;
	
	@Override
	public DepartmentEntity addNewDepartment(UserEntity loggedUser, DepartmentDto newDepartment) throws Exception {
		try {
			if (newDepartment.getDepartmentLabel() != null && newDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(newDepartment.getDepartmentLabel(), newDepartment.getEnrollmentYear(), 1) != null) {
			     throw new Exception("Department label already exists.");
			}
		} catch (Exception e) {
			throw new Exception("addNewDepartment ClassDto check failed.");
		}
		DepartmentEntity department = new DepartmentEntity();
		try {
			department.setDepartmentLabel(newDepartment.getDepartmentLabel());
			department.setEnrollmentYear(newDepartment.getEnrollmentYear());
			department.setStatusActive();
			department.setCreatedById(loggedUser.getId());
			departmentRepository.save(department);
			ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(newDepartment.getDepartment_class()), 1);
			DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, newDepartment.getEnrollmentYear(), loggedUser.getId());
			departmentClassRepository.save(departmentClass);
			department.getClasses().add(departmentClass);
			departmentRepository.save(department);
			return department;
		} catch (Exception e) {
			throw new Exception("addNewStudent save failed.");
		}
	}
	
	@Override
	public void modifyDepartment(UserEntity loggedUser, DepartmentEntity department, DepartmentDto updateDepartment) throws Exception {
		try {
			if (updateDepartment.getDepartmentLabel() != null && updateDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(updateDepartment.getDepartmentLabel(), updateDepartment.getEnrollmentYear(), 1) != null) {
			     throw new Exception("Department label and enrollment year already exists.");
			}
			if (updateDepartment.getDepartmentLabel() != null && updateDepartment.getEnrollmentYear() == null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(updateDepartment.getDepartmentLabel(), department.getEnrollmentYear(), 1) != null) {
			     throw new Exception("Department label already exists.");
			}
			if (updateDepartment.getDepartmentLabel() == null && updateDepartment.getEnrollmentYear() != null && departmentRepository.findByDepartmentLabelAndEnrollmentYearAndStatusLike(department.getDepartmentLabel(), updateDepartment.getEnrollmentYear(), 1) != null) {
			     throw new Exception("Enrollment year already exists.");
			}
		} catch (Exception e) {
			throw new Exception("modifyClass ClassDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateDepartment.getDepartmentLabel() != null && !updateDepartment.getDepartmentLabel().equals(" ") && !updateDepartment.getDepartmentLabel().equals("")) {
				department.setDepartmentLabel(updateDepartment.getDepartmentLabel());
				i++;
			}
			if (updateDepartment.getDepartment_class() != null && !updateDepartment.getDepartment_class().equals(" ") && !updateDepartment.getDepartment_class().equals("")) {
				ClassEntity class_ = classRepository.findByClassLabelAndStatusLike(EClass.valueOf(updateDepartment.getDepartment_class()), 1);
				boolean contains = false;
				String schoolYear = null;
				if (class_!=null && class_.getStatus()!=1 && department.getStatus() == 1) {
					for (DepartmentClassEntity ds : department.getClasses()) {
						if (ds.getStatus() == 1) {
							if (ds.getClass_() == class_) {
								contains = true;
							} else {
								ds.setStatusInactive();
								schoolYear = ds.getSchoolYear();
								departmentClassRepository.save(ds);
							}
						}
					}
				} else
					contains = true;
				if (!contains) {
					DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, schoolYear, loggedUser.getId());
					departmentClassRepository.save(departmentClass);
					department.getClasses().add(departmentClass);
					i++;
				}
			}
			if (updateDepartment.getEnrollmentYear() != null && !updateDepartment.getEnrollmentYear().equals(" ") && !updateDepartment.getEnrollmentYear().equals("")) {
				department.setEnrollmentYear(updateDepartment.getEnrollmentYear());
				i++;
			}
			if (i>0) {
				department.setUpdatedById(loggedUser.getId());
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("modifyClass failed on saving.");
		}
	}
	
	@Override
	public void deleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {		
			department.setStatusInactive();
			department.setUpdatedById(loggedUser.getId());
			for (DepartmentClassEntity ds : department.getClasses()) {
				if (ds.getStatus() == 1) {
					ds.setStatusInactive();
					ds.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(ds);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
				if (ptd.getStatus() == 1) {
					ptd.setStatusInactive();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusInactive();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			department.getStudents().clear();			
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("deleteDepartment failed on saving.");
		}
	}
	
	@Override
	public void undeleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {
			department.setStatusActive();
			department.setUpdatedById(loggedUser.getId());
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("undeleteDepartment failed on saving.");
		}		
	}
	
	@Override
	public void archiveDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception {
		try {
			department.setStatusInactive();
			department.setUpdatedById(loggedUser.getId());
			for (DepartmentClassEntity ds : department.getClasses()) {
				if (ds.getStatus() == 1) {
					ds.setStatusArchived();
					ds.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(ds);
				}
			}
			for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
				if (ptd.getStatus() == 1) {
					ptd.setStatusArchived();
					ptd.setUpdatedById(loggedUser.getId());
					primaryTeacherDepartmentRepository.save(ptd);
				}
			}
			for (TeacherSubjectDepartmentEntity tsd : department.getTeachers_subjects()) {
				if (tsd.getStatus() == 1) {
					tsd.setStatusArchived();
					tsd.setUpdatedById(loggedUser.getId());
					teacherSubjectDepartmentRepository.save(tsd);
				}
			}
			department.getStudents().clear();			
			departmentRepository.save(department);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedStudent failed on saving.");
		}		
	}
	
/*	@Override
	public void addParentToStudent(StudentEntity student, ParentEntity parent) throws Exception {
		try {
			student.getParents().add(parent);
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("addParentToStudent failed on saving.");
		}
	}*/
	
	@Override
	public void addStudentToDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department)  throws Exception {
		try {
			boolean contains = false;
			if (student.getStatus() == 1 && department.getStatus() == 1) {
				for (StudentEntity s : department.getStudents()) {
					if (s.getStatus() == 1 && s == student) {
							contains = true;
					}
				}
			} else
				contains = true;
			if (!contains) {
				department.getStudents().add(student);
				department.setUpdatedById(loggedUser.getId());
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("addStudentToDepartment failed on saving.");
		}		
	}
	
	@Override
	public void addClassToDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear)  throws Exception {
		try {
			boolean contains = false;
			if (class_.getStatus() == 1 && department.getStatus() == 1) {
				for (DepartmentClassEntity ds : department.getClasses()) {
					if (ds.getStatus() == 1) {
						if (ds.getClass_() == class_) {
							contains = true;
						} else {
							ds.setStatusInactive();
							departmentClassRepository.save(ds);
						}
					}
				}
			} else
				contains = true;
			if (!contains) {
				DepartmentClassEntity departmentClass = new DepartmentClassEntity(class_, department, schoolYear, loggedUser.getId());
				departmentClassRepository.save(departmentClass);
				department.getClasses().add(departmentClass);
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("addClassToDepartment failed on saving.");
		}
	}
	
	@Override
	public void addPrimaryTeacherToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, String assignmentDate) throws Exception {
		try {
			boolean contains = false;
			if (teacher.getStatus() == 1 && department.getStatus() == 1) {
				for (PrimaryTeacherDepartmentEntity ptd : department.getTeachers()) {
					if (ptd.getStatus() == 1) {
						if (ptd.getTeacher() == teacher) {
							contains = true;
						} else {
							ptd.setStatusInactive();
							primaryTeacherDepartmentRepository.save(ptd);
						}
					}
				}
			} else
				contains = true;
			if (!contains) {
				PrimaryTeacherDepartmentEntity primaryTeacher = new PrimaryTeacherDepartmentEntity(teacher, department, Date.valueOf(assignmentDate), loggedUser.getId());
				primaryTeacherDepartmentRepository.save(primaryTeacher);
				department.getTeachers().add(primaryTeacher);
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("addPrimaryTeacherToDepartment failed on saving.");
		}
	}

	@Override
	public void addTeacherAndSubjectToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, SubjectEntity subject, String school_year) throws Exception {
		try {
			boolean contains = true;
			if (subject != null) {
				for (TeacherSubjectEntity ts : teacher.getSubjects()) {
					if (ts.getSubject() == subject && ts.getStatus() == 1) {
						contains = false;
					}
				}
			} 
			if (department != null && !contains) {
				for (DepartmentClassEntity ds : department.getClasses()) {
					if (ds.getStatus() == 1) {
						for (ClassSubjectEntity cs : ds.getClass_().getSubjects()) {
							if (cs.getSubject() == subject && cs.getStatus() == 1) {
								contains = false;
							}
						}
					}
				}
			}
			if (!contains) {
				for (TeacherSubjectDepartmentEntity tsd : teacher.getSubjects_departments()) {
					if (tsd.getTeaching_department() == department && tsd.getTeaching_subject() == subject && tsd.getStatus() == 1) {
						contains = true;
					}
				}
			}
			if (!contains) {
				TeacherSubjectDepartmentEntity teaching = new TeacherSubjectDepartmentEntity(department, subject, teacher, school_year, loggedUser.getId());
				teacherSubjectDepartmentRepository.save(teaching);
				department.getTeachers_subjects().add(teaching);
				departmentRepository.save(department);
			}
		} catch (Exception e) {
			throw new Exception("addSubjectsInDepartmentsToTeacher failed on saving.");
		}
	}
	
}
