package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.ClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentClassEntity;
import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;

public interface DepartmentClassRepository extends CrudRepository<DepartmentClassEntity, Integer> {

	public DepartmentClassEntity getByClasAndDepartment(Integer id, Integer id1);

	public ClassEntity getByDepartmentAndStatusLike(DepartmentEntity dep, Integer Status);

}
