package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.DepartmentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Integer> {

	public DepartmentEntity getById(Integer id);
	
	public Iterable<DepartmentEntity> findByStatusLike(Integer status);

	
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.DepartmentClassDto(c.classLabel, d.departmentLabel, d.status, d.createdById, d.updatedById) from DepartmentEntity d join d.class_department c where d.status=1")
	public Iterable<DepartmentClassDto> findWithClass_departmentByStatusLike(Integer status);
	
}
