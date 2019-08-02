package com.iktpreobuka.projekat_za_kraj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.UserAccountEntity;
import com.iktpreobuka.projekat_za_kraj.entities.UserEntity;
import com.iktpreobuka.projekat_za_kraj.entities.dto.AdminDto;
import com.iktpreobuka.projekat_za_kraj.entities.dto.ParentDto;
import com.iktpreobuka.projekat_za_kraj.enumerations.EUserRole;
import com.iktpreobuka.projekat_za_kraj.repositories.UserAccountRepository;
import com.iktpreobuka.projekat_za_kraj.util.Encryption;

@Service
public class UserAccountDaoImpl implements UserAccountDao {
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Override
	public UserEntity findUserByUsername(String username) throws Exception {
		try {
			return userAccountRepository.findUserByUsername(username);
			//return userAccountRepository.getByUsername(username);
		} catch (Exception e) {
			throw new Exception("Find User by Username failed.");
		}
	}
	
	@Override
	public UserAccountEntity addNewUserAccount(UserEntity loggedUser, UserEntity user, String username, EUserRole role, String password) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		UserAccountEntity account = new UserAccountEntity();
		try {
			account.setAccessRole(role);
			account.setUsername(username);
			account.setPassword(Encryption.getPassEncoded(password));
			account.setCreatedById(loggedUser.getId());
			account.setStatusActive();
			account.setUser(user);
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("AddNewUserAccount failed on saving.");
		}
		return account;
	}
	
	/*@Override
	public UserAccountEntity findUserAccountByUser(AdminEntity user) throws Exception {
		try {
			return userAccountRepository.getByUser(user);
		} catch (Exception e) {
			throw new Exception("User account didn't find.");
		}
	}*/
	
	@Override
	public UserAccountEntity findUserAccountByUserAndAccessRoleLike(UserEntity user, String role) throws Exception {
		try {
			return userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.valueOf(role));
		} catch (Exception e) {
			throw new Exception("Find UserAccount by User and AccessRole failed.");
		}
	}
	
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, AdminDto updateAdmin) throws Exception {
		if (updateAdmin.getUsername() != null && userAccountRepository.getByUsername(updateAdmin.getUsername()) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (updateAdmin.getAccessRole() != null && !updateAdmin.getAccessRole().equals("ROLE_PARENT")) {
	         throw new Exception("Access role must be ROLE_PARENT.");
		}		
		try {
			Integer i = 0;
			if (updateAdmin.getUsername() != null && updateAdmin.getUsername().equals(account.getUsername()) && !updateAdmin.getUsername().equals(" ") && !updateAdmin.getUsername().equals("")) {
				account.setUsername(updateAdmin.getUsername());
				i++;
			}/*if (updateAdmin.getAccessRole() != null && EUserRole.valueOf(updateAdmin.getAccessRole()) != account.getAccessRole() && (EUserRole.valueOf(updateAdmin.getAccessRole()) == EUserRole.ROLE_ADMIN || EUserRole.valueOf(updateAdmin.getAccessRole()) == EUserRole.ROLE_PARENT || EUserRole.valueOf(updateAdmin.getAccessRole()) == EUserRole.ROLE_TEACHER || EUserRole.valueOf(updateAdmin.getAccessRole()) == EUserRole.ROLE_STUDENT)) {
				account.setAccessRole(EUserRole.valueOf(updateAdmin.getAccessRole()));
				i++;
			}*/
			if (updateAdmin.getPassword() != null && !Encryption.getPassEncoded(updateAdmin.getPassword()).equals(account.getPassword()) && !updateAdmin.getPassword().equals(" ") && !updateAdmin.getPassword().equals("")) {
				account.setPassword(Encryption.getPassEncoded(updateAdmin.getPassword()));
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Admin failed on saving.");
		}
	}
	
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, ParentDto updateParent) throws Exception {
		if (updateParent.getUsername() != null && userAccountRepository.getByUsername(updateParent.getUsername()) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (updateParent.getAccessRole() != null && !updateParent.getAccessRole().equals("ROLE_PARENT")) {
	         throw new Exception("Access role must be ROLE_PARENT.");
		}		
		try {
			Integer i = 0;
			if (updateParent.getUsername() != null && updateParent.getUsername().equals(account.getUsername()) && !updateParent.getUsername().equals(" ") && !updateParent.getUsername().equals("")) {
				account.setUsername(updateParent.getUsername());
				i++;
			}/*if (updateParent.getAccessRole() != null && EUserRole.valueOf(updateParent.getAccessRole()) != account.getAccessRole() && (EUserRole.valueOf(updateParent.getAccessRole()) == EUserRole.ROLE_ADMIN || EUserRole.valueOf(updateParent.getAccessRole()) == EUserRole.ROLE_PARENT || EUserRole.valueOf(updateParent.getAccessRole()) == EUserRole.ROLE_TEACHER || EUserRole.valueOf(updateParent.getAccessRole()) == EUserRole.ROLE_STUDENT)) {
				account.setAccessRole(EUserRole.valueOf(updateParent.getAccessRole()));
				i++;
			}*/
			if (updateParent.getPassword() != null && !Encryption.getPassEncoded(updateParent.getPassword()).equals(account.getPassword()) && !updateParent.getPassword().equals(" ") && !updateParent.getPassword().equals("")) {
				account.setPassword(Encryption.getPassEncoded(updateParent.getPassword()));
				i++;
			}
			if (i>0) {
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccount for Parent failed on saving.");
		}
	}

	@Override
	public void modifyAccountUsername(UserEntity loggedUser, UserAccountEntity account, String username) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		try {
			if (!username.equals(account.getUsername()) && username != null && !username.equals(" ") && !username.equals("")) {
				account.setUsername(username);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving.");
		}
	}

	/*@Override
	public void modifyAccountRole(UserEntity loggedUser, UserAccountEntity account, EUserRole role) throws Exception {
		try {
			if (role != account.getAccessRole() && (role == EUserRole.ROLE_ADMIN || role == EUserRole.ROLE_PARENT || role == EUserRole.ROLE_TEACHER || role == EUserRole.ROLE_STUDENT)) {
				account.setAccessRole(role);
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountRole failed on saving.");
		}
	}*/
	
	
	@Override
	public void modifyAccountPassword(UserEntity loggedUser, UserAccountEntity account, String password) throws Exception {
		try {
			if (!Encryption.getPassEncoded(password).equals(account.getPassword()) && password != null && !password.equals(" ") && !password.equals("")) {
				account.setPassword(Encryption.getPassEncoded(password));
				account.setUpdatedById(loggedUser.getId());
				userAccountRepository.save(account);
			}
		} catch (Exception e) {
			throw new Exception("ModifyAccountPassword failed on saving.");
		}
	}
	
	@SuppressWarnings({ "null", "unused" })
	@Override
	public void modifyAccount(UserEntity loggedUser, UserAccountEntity account, String username, String password) throws Exception {
		if (username != null && userAccountRepository.getByUsername(username) != null) {
	         throw new Exception("Username already exists.");
	      }
		if (username == null && password == null)
			throw new Exception("Username and password are null.");
		try {
			if (!username.equals(account.getUsername()) && username != null && !username.equals(" ") && !username.equals("")) {
				account.setUsername(username);
			}
			if (!Encryption.getPassEncoded(password).equals(account.getPassword()) && password != null && !password.equals(" ") && !password.equals("")) {
				account.setPassword(Encryption.getPassEncoded(password));
			}
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("ModifyAccountUsername failed on saving.");
		}

	}

	@Override
	public void deleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusInactive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("DeleteAccount failed on saving.");
		}
	}

	@Override
	public void undeleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusActive();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("UndeleteAccount failed on saving.");
		}		
	}
	
	@Override
	public void archiveDeleteAccount(UserEntity loggedUser, UserAccountEntity account) throws Exception {
		try {
			account.setStatusArchived();
			account.setUpdatedById(loggedUser.getId());
			userAccountRepository.save(account);
		} catch (Exception e) {
			throw new Exception("ArchiveDeleteAccount failed on saving.");
		}				
	}
	   
}
