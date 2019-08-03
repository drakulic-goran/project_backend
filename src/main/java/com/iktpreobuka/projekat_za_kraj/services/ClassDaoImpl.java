package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentRepository;

@Service
public class ClassDaoImpl implements ClassDao {

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private ClassSubjectRepository classSubjectRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	
	@Override
	public ClassEntity addNewClass(UserEntity loggedUser, ClassDto newClass) throws Exception {
		try {
			if (newClass.getClassLabel() != null && classRepository.getByClassLabel(EClass.valueOf(newClass.getClassLabel())) != null) {
			     throw new Exception("Class label already exists.");
			}
		} catch (Exception e) {
			throw new Exception("addNewClass ClassDto check failed.");
		}
		ClassEntity clas = new ClassEntity();
		try {
			clas.setClassLabel(EClass.valueOf(newClass.getClassLabel()));
			clas.setStatusActive();
			clas.setCreatedById(loggedUser.getId());
			classRepository.save(clas);
			return clas;
		} catch (Exception e) {
			throw new Exception("addNewStudent save failed.");
		}
	}
	
	@Override
	public void modifyClass(UserEntity loggedUser, ClassEntity class_, ClassDto updateClass) throws Exception {
		try {
			if (updateClass.getClassLabel() != null && classRepository.getByClassLabel(EClass.valueOf(updateClass.getClassLabel())) != null) {
			     throw new Exception("Class label already exists.");
			}
		} catch (Exception e) {
			throw new Exception("modifyClass ClassDto check failed.");
		}
		try {
			class_.setClassLabel(EClass.valueOf(updateClass.getClassLabel()));
			class_.setUpdatedById(loggedUser.getId());
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("modifyClass failed on saving.");
		}
	}
	
	public void addSubjectToClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject, String name) throws Exception {
		try {
			boolean contains = false;
			if (name != null && class_.getStatus() == 1 && subject.getStatus() == 1) {
				for (ClassSubjectEntity cs : class_.getSubjects()) {
					if (cs.getSubject() == subject && cs.getStatus() == 1) {
						contains = true;
					}
				}
			} else
				contains = true;
			if (!contains) {
				ClassSubjectEntity classSubject = new ClassSubjectEntity(class_, subject, name, loggedUser.getId());
				classSubjectRepository.save(classSubject);
				class_.getSubjects().add(classSubject);
				classRepository.save(class_);
			}
		} catch (Exception e) {
			throw new Exception("addSubjectToClass failed on saving.");
		}
	}
	
	public void addDepartmentToClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception {
		try {
			boolean contains = false;
			if (class_.getStatus() == 1 && department.getStatus() == 1) {
				for (DepartmentEntity s : class_.getDepartments()) {
					if (s == department && s.getStatus() == 1) {
						contains = true;
					}
				}
			} else
				contains = true;
			if (!contains) {
				class_.getDepartments().add(department);
				classRepository.save(class_);
			}
		} catch (Exception e) {
			throw new Exception("addDepartmentToClass failed on saving.");
		}
	}
	
	@Override
	public void deleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception {
		try {
			class_.setStatusInactive();
			class_.setUpdatedById(loggedUser.getId());
			for (DepartmentEntity s : class_.getDepartments()) {
				if (s.getStatus() == 1) {
					class_.getDepartments().remove(s);
					classRepository.save(class_);
					//s.setClass_department(null);
					//s.setUpdatedById(loggedUser.getId());
					//departmentRepository.save(s);
				}
			}
			for (ClassSubjectEntity cs : class_.getSubjects()) {
				if (cs.getStatus() == 1) {
					cs.setStatusInactive();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("DeleteStudent failed on saving.");
		}
	}
	
	@Override
	public void undeleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception {
		try {	
			class_.setStatusActive();
			class_.setUpdatedById(loggedUser.getId());	
			for (ClassSubjectEntity cs : class_.getSubjects()) {
				if (cs.getStatus() == 0) {
					cs.setStatusActive();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("UndeleteStudent failed on saving.");
		}		
	}
	
/*		@Override
	public void archiveDeletedStudent(UserEntity loggedUser, StudentEntity student) throws Exception {
		try {
			student.setStatusArchived();
			student.setUpdatedById(loggedUser.getId());
			studentRepository.save(student);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedStudent failed on saving.");
		}		
	}
	
	}*/
	
	
}
