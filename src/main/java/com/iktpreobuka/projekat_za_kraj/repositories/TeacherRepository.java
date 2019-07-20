package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {

	public TeacherEntity getById(Integer id);
	
}
