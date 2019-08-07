package com.iktpreobuka.projekat_za_kraj.services;

import java.util.List;

import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.SubjectEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.SubjectDto;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public interface SubjectDao {

	//public void addStudentToDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department, String transfer_date) throws Exception;

	public SubjectEntity addNewSubject(UserEntity loggedUser, SubjectDto newSubject) throws Exception;

	public void modifySubject(UserEntity loggedUser, SubjectEntity subject, SubjectDto updateSubject) throws Exception;

	public void deleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception;

	public void undeleteSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception;

	public void archiveSubject(UserEntity loggedUser, SubjectEntity subject) throws Exception;
	
	public void addClassToSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject, String learningProgram) throws Exception;
	
	public void removeClassFromSubject(UserEntity loggedUser, List<String> classes, SubjectEntity subject) throws Exception;

	public void addTeachersToSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception;
	
	public void removeTeachersFromSubject(UserEntity loggedUser, SubjectEntity subject, List<String> teachers) throws Exception;
	
	//public void addClassToDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department, String schoolYear)  throws Exception;

	//public void addPrimaryTeacherToDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department, String assignmentDate) throws Exception;

	public void addTeacherAndDepartmentToSubject(UserEntity loggedUser, SubjectEntity subject, String teachingDepartment, String teachingTeacher, String schoolYear) throws Exception;

	//public void removePrimaryTeacherFromDepartment(UserEntity loggedUser, TeacherEntity teacher, DepartmentEntity department) throws Exception;

	public void removeTeacherAndDepartmentFromSubject(UserEntity loggedUser, SubjectEntity subject,	String teachingDepartment, String teachingTeacher) throws Exception;

	public List<SubjectEntity> getSubjectListByStudent(List<Pair<StudentEntity, List<SubjectEntity>>> subjectsByStudent, StudentEntity student) throws Exception;

	//public void removeClassFromDepartment(UserEntity loggedUser, ClassEntity class_, DepartmentEntity department) throws Exception;

	//public void removeStudentFromDepartment(UserEntity loggedUser, StudentEntity student, DepartmentEntity department) throws Exception;

}
