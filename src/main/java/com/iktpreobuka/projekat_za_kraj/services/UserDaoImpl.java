package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class UserDaoImpl implements UserDao {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Iterable<UserEntity> findAllActiveUsers() throws Exception {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			throw new Exception("User didn't find.");
		}
	}

	/*@Override
	public UserEntity addNewUser(UserAccountEntity loggedUser, String firstname, String lastname, EUserRole role, String gender, String jmbg) throws Exception {
		if (userRepository.findByJMBG(jmbg) != null) {
	         throw new Exception("User already exists.");
		}
		UserEntity user = new UserEntity();
		try {
			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setjMBG(jmbg);
			user.setRole(role);
			user.setGender(EGender.valueOf(gender));
			user.setStatusActive();
			user.setCreatedById(loggedUser.getId());
			userRepository.save(user);
		} catch (Exception e) {
			throw new Exception("User haven't been added");
		}
		return user;
	}*/

}
