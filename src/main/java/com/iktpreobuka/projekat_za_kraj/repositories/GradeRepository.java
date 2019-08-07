package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.entities.ParentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {
	
	public GradeEntity getById(Integer id);
	
	public List<GradeEntity> findByStudentAndStatusLike(StudentEntity studentId, Integer status);
	
	public GradeEntity findByStudentAndIdAndStatusLike(StudentEntity user, Integer grade, Integer status);

	public Iterable<GradeEntity> findByStatusLike(Integer status);
	
	public GradeEntity findByIdAndStatusLike(Integer id, Integer status);

	@Query("select g from GradeEntity g join g.student s join s.parents p where p=:parent and g.status=1 and s.status=1")
	public List<GradeEntity> findByParent(ParentEntity parent);

	@Query("select g from GradeEntity g join g.student s join s.parents p where p=:parent and g.status=1 and g.id=:grade and s.status=1")
	public GradeEntity findByParentAndGradeId(ParentEntity parent, Integer grade);

	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1 and tsd.status=1")
	public List<GradeEntity> findByTeacher(TeacherEntity teacher);

	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where tsd.teachingTeacher=:teacher and g.status=1 and g.id=:grade and tsd.status=1")
	public GradeEntity findByTeacherAndGradeId(TeacherEntity teacher, Integer grade);

	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectGradesDto(tsd.teachingSubject, g) from GradeEntity g join g.teacher_subject_department tsd where g.student.id=:student and g.status=1 and tsd.status=1")
	//@Query("select sub, s from ParentEntity p join p.students s join s.student_department sd join sd.class_department cd join cd.subjects su join su.subject sub where p=:parent")
	//public Map<SubjectEntity, StudentEntity> findByParent(UserEntity parent);
	public List<SubjectGradesDto> findGradesWithSubjectByStudent(Integer student);

	@Query("select g from GradeEntity g join g.teacher_subject_department tsd where g.student=:student and tsd.teachingSubject=:subject and g.status=1 and tsd.status=1")
	public List<GradeEntity> findByStudentAndSubject(StudentEntity student, SubjectEntity subject);

}
