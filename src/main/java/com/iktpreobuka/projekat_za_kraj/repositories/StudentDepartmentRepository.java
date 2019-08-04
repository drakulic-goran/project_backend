package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.StudentDepartmentEntity;

public interface StudentDepartmentRepository extends CrudRepository<StudentDepartmentEntity, Integer> {

	public StudentDepartmentEntity getByStudentAndDepartment(Integer id, Integer id1);

}
