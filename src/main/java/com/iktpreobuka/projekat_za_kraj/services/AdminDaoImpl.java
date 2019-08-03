package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.AdminEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EGender;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.AdminRepository;
import com.iktpreobuka.projekat_za_kraj.repositories.UserRepository;

@Service
public class AdminDaoImpl implements AdminDao {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private UserRepository userRepository;


	@Override
	public UserEntity addNewAdmin(UserEntity loggedUser, AdminDto newAdmin) throws Exception {
			try {
				if (newAdmin.getjMBG() != null && adminRepository.getByJMBG(newAdmin.getjMBG()) != null) {
				     throw new Exception("JMBG already exists.");
				}
				if (newAdmin.getEmail() != null && adminRepository.getByEmail(newAdmin.getEmail()) != null) {
				     throw new Exception("E-mail already exists.");
				}
			} catch (Exception e) {
				throw new Exception("AddNewAdmin AdminDto check failed.");
			}
			UserEntity temporaryUser = new AdminEntity();
			try {
				temporaryUser = userRepository.findByJMBG(newAdmin.getjMBG());
				if (temporaryUser != null && (!temporaryUser.getFirstName().equals(newAdmin.getFirstName()) || !temporaryUser.getLastName().equals(newAdmin.getLastName()) || !temporaryUser.getGender().toString().equals(newAdmin.getGender()) || !temporaryUser.getjMBG().equals(newAdmin.getjMBG()))) {
					throw new Exception("User exists, but import data not same as exist user data.");
				}
			} catch (Exception e1) {
				throw new Exception("AddNewAdmin Exist user check failed.");
			}
			AdminEntity user = new AdminEntity();
		try {
			if (temporaryUser == null) {
				try {
					user.setFirstName(newAdmin.getFirstName());
					user.setLastName(newAdmin.getLastName());
					user.setjMBG(newAdmin.getjMBG());
					user.setGender(EGender.valueOf(newAdmin.getGender()));
					user.setMobilePhoneNumber(newAdmin.getMobilePhoneNumber());
					user.setEmail(newAdmin.getEmail());
					user.setRole(EUserRole.ROLE_ADMIN);
					user.setStatusActive();
					user.setCreatedById(loggedUser.getId());
					adminRepository.save(user);
					temporaryUser = user;
				} catch (Exception e) {
					throw new Exception("AddNewAdmin save failed.");
				}
			} else {
				adminRepository.addAdminFromExistUser(newAdmin.getMobilePhoneNumber(), newAdmin.getEmail(), temporaryUser.getId(), loggedUser.getId());
			}
			return temporaryUser;
		} catch (Exception e) {
			throw new Exception("AddNewAdmin save failed.");
		}
	}

	@Override
	public void modifyAdmin(UserEntity loggedUser, AdminEntity admin, AdminDto updateAdmin) throws Exception {
		try {
			if (updateAdmin.getEmail() != null && adminRepository.getByEmail(updateAdmin.getEmail()) != null) {
			     throw new Exception("E-mail already exists.");
			}
			if (updateAdmin.getjMBG() != null && !updateAdmin.getjMBG().equals(" ") && !updateAdmin.getjMBG().equals("") && userRepository.getByJMBG(updateAdmin.getjMBG()) != null) {
			     throw new Exception("JMBG already exists.");
			}
			if (updateAdmin.getAccessRole() != null && updateAdmin.getAccessRole() != "ROLE_ADMIN") {
			     throw new Exception("Access role must be ROLE_ADMIN.");
			}
		} catch (Exception e1) {
			throw new Exception("modifyAdmin AdminDto check failed.");
		}
		try {
			Integer i = 0;
			if (updateAdmin.getFirstName() != null && !updateAdmin.getFirstName().equals(" ") && !updateAdmin.getFirstName().equals("") && !updateAdmin.getFirstName().equals(admin.getFirstName())) {
				admin.setFirstName(updateAdmin.getFirstName());
				i++;
			}
			if (updateAdmin.getLastName() != null && !updateAdmin.getLastName().equals(admin.getLastName()) && !updateAdmin.getLastName().equals(" ") && !updateAdmin.getLastName().equals("")) {
				admin.setLastName(updateAdmin.getLastName());
				i++;
			}
			if (updateAdmin.getjMBG() != null && !updateAdmin.getjMBG().equals(admin.getjMBG()) && !updateAdmin.getjMBG().equals(" ") && !updateAdmin.getjMBG().equals("")) {
				admin.setjMBG(updateAdmin.getjMBG());
				i++;
			}
			if (updateAdmin.getGender() != null && EGender.valueOf(updateAdmin.getGender()) != admin.getGender() && (EGender.valueOf(updateAdmin.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updateAdmin.getGender()) == EGender.GENDER_MALE)) {
				admin.setGender(EGender.valueOf(updateAdmin.getGender()));
				i++;
			}
			if (updateAdmin.getMobilePhoneNumber() != null && !updateAdmin.getMobilePhoneNumber().equals(admin.getMobilePhoneNumber()) && !updateAdmin.getMobilePhoneNumber().equals(" ") && !updateAdmin.getMobilePhoneNumber().equals("")) {
				admin.setMobilePhoneNumber(updateAdmin.getMobilePhoneNumber());
				i++;
			}
			if (updateAdmin.getEmail() != null && !updateAdmin.getEmail().equals(admin.getEmail()) && !updateAdmin.getEmail().equals(" ") && !updateAdmin.getEmail().equals("")) {
				admin.setEmail(updateAdmin.getEmail());
				i++;
			}
			if (i>0) {
				admin.setUpdatedById(loggedUser.getId());
				adminRepository.save(admin);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAdmin faild on saving.");
		}
	}

	@Override
	public void deleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception {
		try {
			admin.setStatusInactive();
			admin.setUpdatedById(loggedUser.getId());
			adminRepository.save(admin);
		} catch (Exception e) {
			throw new Exception("DeleteAdmin failed on saving.");
		}
	}
	
	@Override
	public void undeleteAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception {
		try {
			admin.setStatusActive();
			admin.setUpdatedById(loggedUser.getId());
			adminRepository.save(admin);
		} catch (Exception e) {
			throw new Exception("UndeleteAdmin failed on saving.");
		}		
	}
	
	@Override
	public void archiveAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception {
		try {
			admin.setStatusArchived();
			admin.setUpdatedById(loggedUser.getId());
			adminRepository.save(admin);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedAdmin failed on saving.");
		}		
	}

}
