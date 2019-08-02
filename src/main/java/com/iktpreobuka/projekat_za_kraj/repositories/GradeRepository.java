package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
	public GradeEntity getById(Integer id);
	
	public List<GradeEntity> findByStudent(StudentEntity studentId);
	
	public Iterable<GradeEntity> findByStatusLike(Integer status);
	
	public GradeEntity findByIdAndStatusLike(Integer id, Integer status);

	@Query("select g from GradeEntity g join g.student s join s.parents p where p=:parent and g.status=1")
	public List<GradeEntity> findByParent(ParentEntity parent);

	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1")
	public List<GradeEntity> findByTeacher(TeacherEntity teacher);

}
