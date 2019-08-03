package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;

public interface ClassDao {

	//public void addStudentToDepartment(StudentEntity student, DepartmentEntity department)  throws Exception;

	public ClassEntity addNewClass(UserEntity loggedUser, ClassDto newClass) throws Exception;

	public void modifyClass(UserEntity loggedUser, ClassEntity class_, ClassDto updateClass) throws Exception;

	public void addSubjectToClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject, String name) throws Exception;

	public void addDepartmentToClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception;

	//public void modifyStudent(UserEntity loggedUser, StudentEntity student, StudentDto newParent) throws Exception;

	public void deleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception;

	public void undeleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception;

	//public void archiveDeletedStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	//public void addParentToStudent(StudentEntity student, ParentEntity parent) throws Exception;

	//public void addDepartmentToStudent(StudentEntity student, DepartmentEntity department) throws Exception;

}
