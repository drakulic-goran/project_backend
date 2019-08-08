package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto;

public interface SubjectRepository extends CrudRepository<SubjectEntity, Integer> {
	
	public SubjectEntity getById(Integer id);
	
	public Iterable<SubjectEntity> findByStatusLike(Integer status);

	@Query("select s from SubjectEntity s join s.teachers t where t.teacher.id=:teacher and s.status=1 and t.status=1")
	public Iterable<SubjectEntity> findByTeacher(Integer teacher);

	@Query("select sub from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where s.id=:student and s.status=1 and d.status=1 and sd.status=1 and cl.status=1 and cd.status=1 and su.status=1 and sub.status=1")
	public Iterable<SubjectEntity> findByStudent(Integer student);
	
	@Query("select sub from StudentEntity s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where s.id=:student")
	public Iterable<SubjectEntity> findActiveAndDeletedSubjectsByStudent(Integer student);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentSubjectDto(s, sub) from ParentEntity p join p.students s join s.departments d join d.department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where p.id=:parent and s.status=1 and d.status=1 and cl.status=1 and su.status=1 and sub.status=1")
	//@Query("select sub, s from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p=:parent")
	//public Map<SubjectEntity, StudentEntity> findByParent(UserEntity parent);
	public List<StudentSubjectDto> findByParent(Integer parent);

	public SubjectEntity findByIdAndStatusLike(Integer s_id, Integer status);

	@Query("select sub from TeacherEntity t join t.departments d join d.primary_department sd join sd.classes cl join cl.clas cd join cd.subjects su join su.subject sub where t.id=:teacher and t.status=1 and d.status=1 and sd.status=1 and cl.status=1 and cd.status=1 and su.status=1 and sub.status=1")
	public Iterable<SubjectEntity> findByPrimaryTeacher(Integer teacher);

}
