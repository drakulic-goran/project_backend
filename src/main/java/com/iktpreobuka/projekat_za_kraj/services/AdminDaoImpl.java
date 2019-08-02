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
	public AdminEntity findById(Integer id) throws Exception{
		try {
			return adminRepository.getById(id);
		} catch (Exception e) {
			throw new Exception("Get admin by Id failed.");
		}
	}
	
	@Override
	public AdminEntity findByIdAndStatusLike(Integer id, Integer status) throws Exception {
		try {
			return adminRepository.findByIdAndStatusLike(id, status);
		} catch (Exception e) {
			throw new Exception("Get admin by Id and Status failed.");
		}
	}

	@Override
	public Iterable<AdminEntity> findByStatusLike(Integer status) throws Exception {
		try {
			return adminRepository.findByStatusLike(status);
		} catch (Exception e) {
			throw new Exception("Get admin by Status failed.");
		}		
	}

	@Override
	public UserEntity addNewAdmin(UserEntity loggedUser, AdminDto newAdmin) throws Exception {
			try {
				if (newAdmin.getjMBG() != null && adminRepository.getByJMBGAndStatusLike(newAdmin.getjMBG(), 1) != null) {
				     throw new Exception("JMBG already exists.");
				}
				if (newAdmin.getEmail() != null && adminRepository.getByEmailAndStatusLike(newAdmin.getEmail(), 1) != null) {
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
					//return user;
				} catch (Exception e) {
					throw new Exception("AddNewAdmin save failed.");
				}
			} else {
				//temporaryUser.setStatusActive();
				//temporaryUser.setRole(EUserRole.ROLE_ADMIN);
				//temporaryUser.setUpdatedById(loggedUser.getId());
				//userRepository.save(temporaryUser);
				adminRepository.addAdminFromExistUser(newAdmin.getMobilePhoneNumber(), newAdmin.getEmail(), temporaryUser.getId(), loggedUser.getId());
				//user = adminRepository.findByIdAndStatusLike(temporaryUser.getId(), 1);
				//user = (AdminEntity)temporaryUser;
			}
		} catch (Exception e) {
			throw new Exception("AddNewAdmin save failed.");
		}
		return temporaryUser;
	}

	@Override
	public void modifyAdmin(UserEntity loggedUser, AdminEntity admin, AdminDto updateAdmin) throws Exception {
		try {
			if (updateAdmin.getEmail() != null && adminRepository.getByEmailAndStatusLike(updateAdmin.getEmail(), 1) != null) {
			     throw new Exception("E-mail already exists.");
			}
			if (updateAdmin.getjMBG() != null && adminRepository.getByJMBGAndStatusLike(updateAdmin.getjMBG(), 1) != null) {
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
			/*if (updateAdmin.getRole() != null && EUserRole.valueOf(updateAdmin.getRole()) != admin.getRole() && (EUserRole.valueOf(updateAdmin.getRole()) == EUserRole.ROLE_ADMIN || EUserRole.valueOf(updateAdmin.getRole()) == EUserRole.ROLE_PARENT || EUserRole.valueOf(updateAdmin.getRole()) == EUserRole.ROLE_TEACHER || EUserRole.valueOf(updateAdmin.getRole()) == EUserRole.ROLE_STUDENT)) {
				admin.setRole(EUserRole.valueOf(updateAdmin.getRole()));
				i++;
				System.out.println(i+10000000);
			}*/
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
	public void archiveDeletedAdmin(UserEntity loggedUser, AdminEntity admin) throws Exception {
		try {
			admin.setStatusArchived();
			admin.setUpdatedById(loggedUser.getId());
			adminRepository.save(admin);
		} catch (Exception e) {
			throw new Exception("ArchiveDeletedAdmin failed on saving.");
		}		
	}

}
