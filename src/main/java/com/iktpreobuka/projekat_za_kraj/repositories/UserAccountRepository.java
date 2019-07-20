package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer> {
	
	public UserAccountEntity getById(Integer id);
	public UserAccountEntity getByUsername(String username);
	
	
}
