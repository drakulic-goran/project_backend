package com.iktpreobuka.projekat_za_kraj.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, Integer> {
	
	public UserAccountEntity getById(Integer id);
	public UserAccountEntity getByUsername(String username);
	public UserAccountEntity getByUser(UserEntity user);
	@Query("select ua.user from UserAccountEntity ua where ua.username=:username")
	public UserEntity findUserByUsername(String username);
	public UserAccountEntity findByUserAndAccessRoleLike(UserEntity user, EUserRole eUserRole);

}
