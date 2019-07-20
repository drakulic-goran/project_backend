package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
	public GradeEntity getById(Integer id);
	public List<GradeEntity> findByStudentAndTeacherSubject(Integer studentId, Integer subjectId);

}
