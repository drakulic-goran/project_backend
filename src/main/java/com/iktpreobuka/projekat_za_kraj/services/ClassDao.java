package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ClassDto;

public interface ClassDao {

	public ClassEntity addNewClass(UserEntity loggedUser, ClassDto newClass) throws Exception;

	public void modifyClass(UserEntity loggedUser, ClassEntity class_, ClassDto updateClass) throws Exception;

	public void addSubjectToClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject, String name) throws Exception;

	public void addDepartmentToClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear) throws Exception;

	public void deleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception;

	public void undeleteClass(UserEntity loggedUser, ClassEntity class_) throws Exception;

	public void archiveClass(UserEntity loggedUser, ClassEntity class_) throws Exception;

	public void removeSubjectFromClass(UserEntity loggedUser, ClassEntity class_, SubjectEntity subject) throws Exception;

	public void removeDepartmentFromClass(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolyear) throws Exception;

}
