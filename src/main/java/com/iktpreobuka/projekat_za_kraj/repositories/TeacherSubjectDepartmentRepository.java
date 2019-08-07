package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherEntity;
import com.iktpreobuka.projekat_za_kraj.entities.TeacherSubjectDepartmentEntity;

public interface TeacherSubjectDepartmentRepository extends CrudRepository<TeacherSubjectDepartmentEntity, Integer> {

	/*@Query("select tsd from TeacherSubjectDepartmentEntity tsd where tsd.teachingTeacher=:teacher and tsd.teachingSubject=:subject and tsd.teachingDepartment=:department and tsd.status=1")
	public TeacherSubjectDepartmentEntity getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(TeacherEntity teacher, SubjectEntity subject, DepartmentEntity department);
*/
	
	@Query("select tsd from TeacherSubjectDepartmentEntity tsd join tsd.teachingDepartment td join td.students s where tsd.teachingTeacher=:teacher and tsd.teachingSubject=:subject and s.student=:student and td.status=1 and tsd.status=1 and s.status=1")
	public TeacherSubjectDepartmentEntity getByTeachingTeacherAndTeachingSubjectAndTeachingDepartment(TeacherEntity teacher, SubjectEntity subject, StudentEntity student);

}
