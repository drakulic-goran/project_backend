package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;

public interface DepartmentClassRepository extends CrudRepository<DepartmentClassEntity, Integer> {

	public DepartmentClassEntity getByClasAndDepartment(Integer id, Integer id1);

}
