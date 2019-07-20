package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	
	public SubjectEntity getById(Integer id);

}
