package com.iktpreobuka.projekat_za_kraj.services;

import java.util.List;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public interface TeacherDao {

	//public TeacherEntity findById(Integer id) throws Exception;
	
	//public TeacherEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	//public Iterable<TeacherEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewTeacher(UserEntity loggedUser, TeacherDto newTeacher) throws Exception;

	public void modifyTeacher(UserEntity loggedUser, TeacherEntity teacher, TeacherDto newTeacher) throws Exception;

	public void deleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void undeleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void archiveDeletedTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void addSubjectsToTeacher(UserEntity loggedUser, TeacherEntity user, List<String> subjects) throws Exception;

	public void addPrimaryDepartmentToTeacher(UserEntity loggedUser, TeacherEntity user, String primaryDepartment) throws Exception;

	public void addSubjectsInDepartmentsToTeacher(UserEntity loggedUser, TeacherEntity user, List<Pair<String, String>> subject_at_department, String schoolYear) throws Exception;

}
