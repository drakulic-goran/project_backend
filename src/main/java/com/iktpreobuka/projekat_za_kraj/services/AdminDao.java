package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;

public interface AdminDao {

	//public AdminEntity findById(Integer id) throws Exception;
	
	//public AdminEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception;

	//public Iterable<AdminEntity> findByStatusLike(Integer status) throws Exception;

	public UserEntity addNewAdmin(UserEntity loggedUser, AdminDto newAdmin) throws Exception;

	public void modifyAdmin(UserEntity loggedUser, AdminEntity admin, AdminDto newAdmin) throws Exception;

	public void deleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

	public void undeleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

	public void archiveDeletedAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception;

}
