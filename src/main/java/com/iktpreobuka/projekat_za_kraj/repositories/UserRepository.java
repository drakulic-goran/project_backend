package com.iktpreobuka.projekat_za_kraj.repositories;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.UserDto;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	public Optional<UserEntity> findById(Integer id);
	public UserEntity getById(Integer id);
	/* public UserEntity findByEmail(String email);
	public List<UserEntity> findByFirstNameOrderByEmailAsc(String name); */
	// public List<UserEntity> findByNameStartingWith(String letter);
	// @Query("select distinct u.name from UserEntity u where (u.name) like ?1%")
	/* @Query("select distinct u.firstname from UserEntity u where u.firstname like :letter%")
	public List<UserEntity> findAllByFirstLetter(String letter); */
	@Query("select s from StudentEntity s join s.parents ps where ps.id=:parentId")
	public List<UserEntity> findByParent(Integer parentId);

	public void save(@Valid UserDto newUser);

}
