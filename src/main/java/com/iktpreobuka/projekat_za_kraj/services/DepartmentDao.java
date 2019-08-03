package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;

public interface DepartmentDao {

	public void addStudentToDepartment(StudentEntity student, DepartmentEntity department)  throws Exception;

	//public UserEntity addNewStudent(UserEntity loggedUser, StudentDto newParent) throws Exception;

	//public void modifyStudent(UserEntity loggedUser, StudentEntity student, StudentDto newParent) throws Exception;

	//public void deleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	//public void undeleteStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	//public void archiveDeletedStudent(UserEntity loggedUser, StudentEntity student) throws Exception;

	//public void addParentToStudent(StudentEntity student, ParentEntity parent) throws Exception;

	//public void addDepartmentToStudent(StudentEntity student, DepartmentEntity department) throws Exception;

}
