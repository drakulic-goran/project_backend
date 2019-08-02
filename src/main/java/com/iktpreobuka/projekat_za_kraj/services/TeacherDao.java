package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.TeacherDto;

public interface TeacherDao {

	public TeacherEntity findById(Integer id) throws Exception;
	
	public TeacherEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	public Iterable<TeacherEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewTeacher(UserEntity loggedUser, TeacherDto newTeacher) throws Exception;

	public void modifyTeacher(UserEntity loggedUser, TeacherEntity teacher, TeacherDto newTeacher) throws Exception;

	public void deleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void undeleteTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

	public void archiveDeletedTeacher(UserEntity loggedUser, TeacherEntity teacher) throws Exception;

}
