package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentDto;

public interface DepartmentDao {

	public void addStudentToDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception;

	public DepartmentEntity addNewDepartment(UserEntity loggedUser, DepartmentDto newDepartment) throws Exception;

	public void modifyDepartment(UserEntity loggedUser, DepartmentEntity department, DepartmentDto updateDepartment) throws Exception;

	public void deleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public void undeleteDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public void archiveDepartment(UserEntity loggedUser, DepartmentEntity department) throws Exception;

	public void addClassToDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear)  throws Exception;

	public void addPrimaryTeacherToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, String assignmentDate) throws Exception;

	public void addTeacherAndSubjectToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, SubjectEntity subject, String school_year) throws Exception;

	//public void addParentToStudent(StudentEntity student, ParentEntity parent) throws Exception;

	//public void addDepartmentToStudent(StudentEntity student, DepartmentEntity department) throws Exception;

}
