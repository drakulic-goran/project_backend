package com.iktpreobuka.projekat_za_kraj.services;

import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;

public interface UserDao {

	public Iterable<UserEntity> findAllActiveUsers() throws Exception;
	
	//public UserEntity addNewUser(UserAccountEntity loggedUser, String firstname, String lastname, EUserRole role, String gender, String jmbg) throws Exception;

}
