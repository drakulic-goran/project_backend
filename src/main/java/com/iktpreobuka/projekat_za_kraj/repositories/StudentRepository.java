package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktpreobuka.projekat_za_kraj.entities.StudentEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	public Optional<StudentEntity> findById(Integer id);
	public StudentEntity getById(Integer id);
	//public StudentEntity getByUserAccount(UserAccountEntity id);
	
	//public StudentEntity findByUserAccountAndStatusLike(UserAccountEntity loggedUser, Integer status);

	public StudentEntity getByJMBG(String getjMBG);

	/* public UserEntity findByEmail(String email);
	public List<UserEntity> findByFirstNameOrderByEmailAsc(String name); */
	// public List<UserEntity> findByNameStartingWith(String letter);
	// @Query("select distinct u.name from UserEntity u where (u.name) like ?1%")
	/* @Query("select distinct u.firstname from UserEntity u where u.firstname like :letter%")
	public List<UserEntity> findAllByFirstLetter(String letter); */
	@Query("select s from StudentEntity s join s.parents ps where ps.id=:parentId and s.status=1")
	public List<StudentEntity> findByParent(Integer parentId);
	
	// starije @Query("select s from StudentEntity s join s.student_department sd join PrimaryTeacherEntity pt join TeacherEntity t where pt.primary_department=d.id and pt.primary_teacher=t.id and t.id=:teacherId")
	@Query("select new com.iktpreobuka.projekat_za_kraj.entities.dto.StudentDto(s.firstName, s.lastName, s.schoolIdentificationNumber, c.classLabel, de.departmentLabel) from StudentEntity s join s.student_department de join de.class_department c join de.teachers d join d.primary_teacher t where t.id=:teacher and s.status=1")
	public List<StudentDto> findByPrimaryTeacher(Integer teacher);

	public void save(@Valid StudentDto newUser);
	public void save(UserEntity user);
	public StudentEntity findByIdAndStatusLike(Integer loggedUser, Integer i);
	public Iterable<StudentEntity> findByStatusLike(Integer status);
	public StudentEntity getBySchoolIdentificationNumber(String schoolIdentificationNumber);
	@Transactional
    @Modifying
    @Query (value ="INSERT INTO student (user_id, enrollment_date, school_identification_number, status, created_by) VALUES (:user, :enrollment, :number, 1, :logged)", nativeQuery = true)
	public void addAdminFromExistUser(String enrollment, String number, Integer user, Integer logged);
	public Object getBySchoolIdentificationNumberAndStatusLike(String schoolIdentificationNumber, Integer status);
	public Object getByJMBGAndStatusLike(String jMBG, Integer status);

}