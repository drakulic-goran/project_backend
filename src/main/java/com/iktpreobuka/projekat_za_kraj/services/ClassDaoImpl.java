package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ClassSubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EClass;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.ClassSubjectRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.DepartmentClassRepository;

@Service
public class ClassDaoImpl implements ClassDao {

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private ClassSubjectRepository classSubjectRepository;

	@Autowired
	private DepartmentClassRepository departmentClassRepository;
	
	@Override
	public ClassEntity addNewClass(UserEntity loggedUser, ClassDto newClass) throws Exception {
		try {
			if (newClass.getClassLabel() == null) {
			     throw new Exception("Some data is null.");
			}
		} catch (Exception e) {
			throw new Exception("ClassDto check failed.");
		}
		ClassEntity clas = new ClassEntity();
		try {
			clas.setClassLabel(EClass.valueOf(newClass.getClassLabel()));
			clas.setStatusActive();
			clas.setCreatedById(loggedUser.getId());
			classRepository.save(clas);
			return clas;
		} catch (Exception e) {
			throw new Exception("addNewClass save failed.");
		}
	}
	
	@Override
	public void modifyClass(UserEntity loggedUser, ClassEntity class_, ClassDto updateClass) throws Exception {
		try {
			if (updateClass.getClassLabel() == null) {
			     throw new Exception("All data is null.");
			}
		} catch (Exception e) {
			throw new Exception("ClassDto check failed.");
		}
		try {
			if (updateClass.getClassLabel() != null && !updateClass.getClassLabel().equals(" ") && !updateClass.getClassLabel().equals("")) {
				class_.setClassLabel(EClass.valueOf(updateClass.getClassLabel()));
				class_.setUpdatedById(loggedUser.getId());
				classRepository.save(class_);
			}
		} catch (Exception e) {
			throw new Exception("modifyClass failed on saving.");
		}
	}
	
	@Override
	public ClassSubjectEntity addSubjectToClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject, String name) throws Exception {
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
			ClassSubjectEntity classSubject = null;
			if (!contains) {
				classSubject = new ClassSubjectEntity(class_, subject, name, loggedUser.getId());
				classSubjectRepository.save(classSubject);
				class_.getSubjects().add(classSubject);
				class_.setUpdatedById(loggedUser.getId());
				classRepository.save(class_);
			}
			return classSubject;
		} catch (Exception e) {
			throw new Exception("addSubjectToClass failed on saving.");
		}
	}
	
	@Override
	public ClassSubjectEntity removeSubjectFromClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject) throws Exception {
		try {
			ClassSubjectEntity cs1 = null;
			if (class_.getStatus() == 1 && subject.getStatus() == 1) {
				for (ClassSubjectEntity cs : class_.getSubjects()) {
					if (cs.getSubject() == subject && cs.getStatus() == 1) {
						cs.setStatusInactive();
						cs.setUpdatedById(loggedUser.getId());
						classSubjectRepository.save(cs);
						cs1 = cs;
					}
				}
			}
			return cs1;
		} catch (Exception e) {
			throw new Exception("removeSubjectFromClass failed on saving.");
		}

	}
	
	@Override
	public DepartmentClassEntity addDepartmentToClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear) throws Exception {
		try {
			boolean contains = false;
			if (class_.getStatus() == 1 && department.getStatus() == 1) {
				for (DepartmentClassEntity ds : class_.getDepartments()) {
					if (ds.getStatus() == 1) {
						if (ds.getDepartment() == department) {
							contains = true;
						}
					}
				}
			} else
				contains = true;
			DepartmentClassEntity departmentClass = null;
			if (!contains) {
				for (DepartmentClassEntity d : department.getClasses()) {
					d.setStatusInactive();
					d.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(d);
				}
				departmentClass = new DepartmentClassEntity(class_, department, schoolYear, loggedUser.getId());
				departmentClassRepository.save(departmentClass);
				class_.getDepartments().add(departmentClass);
				class_.setUpdatedById(loggedUser.getId());
				classRepository.save(class_);
			}
			return departmentClass;
		} catch (Exception e) {
			throw new Exception("addDepartmentToClass failed on saving.");
		}
	}
	
	public DepartmentClassEntity removeDepartmentFromClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception {
		try {
			DepartmentClassEntity dc1 = null;
			if (class_.getStatus() == 1 && department.getStatus() == 1) {
				for (DepartmentClassEntity ds : class_.getDepartments()) {
					if (ds.getStatus() == 1 && ds.getDepartment() == department) {
						ds.setStatusInactive();
						ds.setUpdatedById(loggedUser.getId());
						departmentClassRepository.save(ds);
						dc1 = ds;
					}
				}
			}
			return dc1;
		} catch (Exception e) {
			throw new Exception("addDepartmentToClass failed on saving.");
		}
	}
	
	@Override
	public void deleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception {
		try {
			for (DepartmentClassEntity ds : class_.getDepartments()) {
				if (ds.getStatus() == 1) {
					ds.setStatusInactive();
					ds.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(ds);
				}
			}
			for (ClassSubjectEntity cs : class_.getSubjects()) {
				if (cs.getStatus() == 1) {
					cs.setStatusInactive();
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			class_.setStatusInactive();
			class_.setUpdatedById(loggedUser.getId());
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("deleteClass failed on saving.");
		}
	}
	
	@Override
	public void undeleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception {
		try {	
			class_.setStatusActive();
			class_.setUpdatedById(loggedUser.getId());	
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("undeleteClass failed on saving.");
		}		
	}
	
	@Override
	public void archiveClass(UserEntity loggedUser, ClassEntity class_) throws Exception {
		try {
			for (DepartmentClassEntity ds : class_.getDepartments()) {
				if (ds.getStatus() != -1) {
					ds.setStatusArchived();;
					ds.setUpdatedById(loggedUser.getId());
					departmentClassRepository.save(ds);
				}
			}
			for (ClassSubjectEntity cs : class_.getSubjects()) {
				if (cs.getStatus() != -1) {
					cs.setStatusArchived();;
					cs.setUpdatedById(loggedUser.getId());
					classSubjectRepository.save(cs);
				}
			}
			class_.setStatusArchived();;
			class_.setUpdatedById(loggedUser.getId());
			classRepository.save(class_);
		} catch (Exception e) {
			throw new Exception("archiveClass failed on saving.");
		}		
	}
	
}
