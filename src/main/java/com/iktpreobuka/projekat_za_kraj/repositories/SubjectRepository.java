package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectsDto;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	
	public SubjectEntity getById(Integer id);
	
	public Iterable<SubjectEntity> findByStatusLike(Integer status);

	@Query("select s from SubjectEntity s join s.teachers t where t.teacher.id=:teacher and s.status=1")
	public Iterable<SubjectEntity> findByTeacher(Integer teacher);

	@Query("select sub from StudentEntity s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where s.id=:student and sub.status=1")
	public Iterable<SubjectEntity> findByStudent(Integer student);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectsDto(s, sub) from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p.id=:parent and sub.status=1")
	//@Query("select sub, s from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p=:parent")
	//public Map<SubjectEntity, StudentEntity> findByParent(UserEntity parent);
	public List<StudentSubjectsDto> findByParent(Integer parent);

}
