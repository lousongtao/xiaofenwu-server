/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.account.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.shuishou.retailer.ConstantValue;
import com.shuishou.retailer.account.models.IPermissionDataAccessor;
import com.shuishou.retailer.account.models.IUserDataAccessor;
import com.shuishou.retailer.account.models.IUserPermissionDataAccessor;
import com.shuishou.retailer.account.models.Permission;
import com.shuishou.retailer.account.models.UserData;
import com.shuishou.retailer.account.models.UserPermission;
import com.shuishou.retailer.account.views.GetAccountsResult;
import com.shuishou.retailer.account.views.LoginResult;
import com.shuishou.retailer.log.models.LogData;
import com.shuishou.retailer.log.services.ILogService;
import com.shuishou.retailer.views.ObjectListResult;
import com.shuishou.retailer.views.ObjectResult;
import com.shuishou.retailer.views.Result;

@Service("accountService")
@Transactional(readOnly = true)
public class AccountService implements IAccountService {

	/**
	 * the logger.
	 */
	private final static Logger logger = LogManager.getLogger(AccountService.class);

	
	/**
	 * the log service.
	 */
	@Autowired
	private ILogService logService;
	
	@Autowired
	private IPermissionDataAccessor permissionDA;
	
	@Autowired
	private IUserPermissionDataAccessor userPermissionDA;

	/**
	 * the user accessor.
	 */
	@Autowired
	private IUserDataAccessor userDA;



	/**
	 * @param data
	 *            the data to be hashed.
	 * @return the hashed string.
	 * @throws NoSuchAlgorithmException
	 */
	private static String toSHA1(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			logger.error("Can't get SHA-1 algorithm message digest.");
			throw ex;
		}
		return toHex(md.digest(data));
	}

	/**
	 * Bytes array to string.
	 * 
	 * @param digest
	 * @return final string.
	 */
	private static String toHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%1$02X", b));
		}

		return sb.toString();
	}

	@Override
	@Transactional(readOnly = false)
	public UserData createRoot() {
		UserData root = createUser("admin", "admin");

		userDA.updateUser(root);

		return root;
	}

	@Override
	@Transactional(readOnly = false)
	public UserData createUser(String username, String password) {
		UserData user = new UserData();
		user.setUsername(username);
		String hashedPassword = password;
		try {
			hashedPassword = toSHA1(password.getBytes());
		} catch (Exception ex) {
			logger.error("Can't hash the password. Use plain store!!!");
		}
		user.setHashedPassword(hashedPassword);
		userDA.persistUser(user);
		
		return user;
	}

	@Override
	public UserData getUserById(long id) {
		UserData user = userDA.getUserById(id);
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public LoginResult auth(String username, String password) {
		// check username.
		UserData user = userDA.getUserByUsername(username);
		if (user == null)
			return new LoginResult("invalid_user", "", "");

		// check password.
		try {
			String hashedPassword = toSHA1(password.getBytes());
			if (!user.getHashedPassword().equals(hashedPassword))
				return new LoginResult("invalid_password", "", "");
		} catch (NoSuchAlgorithmException ex) {
			logger.error("check user password failed.", ex);
			return new LoginResult("invalid_password", "", "");
		}


		// write log.
		logService.write(user, LogData.LogType.ACCOUNT_LOGIN.toString(),
				"User " + user + " login.");

		return new LoginResult(Result.OK, Long.toString(user.getId()), user.getUsername());
	}



	@Override
	@Transactional(readOnly = true)
	public ObjectListResult getAccounts() {

		// do list.
		String listStmt = "select u from UserData u";
		Query listQuery = userDA.getSession().createQuery(listStmt);
		
		@SuppressWarnings("unchecked")
		List<UserData> users = (List<UserData>) listQuery.list();
		for (int i = 0; i < users.size(); i++) {
			UserData user = users.get(i);
			Hibernate.initialize(user);
			Hibernate.initialize(user.permissions);
		}
		return new ObjectListResult(Result.OK, true, users);
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectResult addAccount(long userId, String username, String password, String permission) {
		UserData user = userDA.getUserByUsername(username);
		if (user != null)
			return new ObjectResult("account_existing", false);

		user = new UserData();
		user.setUsername(username);
		String hashedPassword = password;
		try {
			hashedPassword = toSHA1(password.getBytes());
		} catch (Exception ex) {
			logger.error("Can't hash the password. Use plain store!!!");
		}
		user.setHashedPassword(hashedPassword);
		userDA.saveUser(user);
		
		//add permission
		List<Permission> allPermission = permissionDA.queryAllPermission();
		String[] grandPermIds = permission.split("/");
		for(String pid : grandPermIds){
			for(Permission p : allPermission){
				if (pid.equals(p.getId()+"")){
					UserPermission up = new UserPermission();
					up.setUser(user);
					up.setPermission(p);
					userPermissionDA.save(up);
				}
			}
		}
		
		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.ACCOUNT_ADD.toString(), "User " + selfUser + " add user " + user + " account.");

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectResult changePassword(long userId, int accountId, String oldPassword, String newPassword) {
		
		// check user valid
//		long lUserId = Long.parseLong(userId);
		UserData user = userDA.getUserById(accountId);
		if (user == null)
			return new ObjectResult("cannot find user by id "+ accountId, false);

		// check password.
		try {
			String hashedPassword = toSHA1(oldPassword.getBytes());
			if (!user.getHashedPassword().equals(hashedPassword))
				return new ObjectResult("old password is wrong", false);
		} catch (NoSuchAlgorithmException ex) {
			logger.error("check user password failed.", ex);
			return new ObjectResult("invalid_password", false);
		}

		// change password
		try {
			String newHashedPassword = toSHA1(newPassword.getBytes());
			user.setHashedPassword(newHashedPassword);
		} catch (NoSuchAlgorithmException ex) {
			logger.error("set new password failed.", ex);
			return new ObjectResult("invalid_password", false);
		}

		// save.
		userDA.saveOrUpdateUser(user);

		// write log.
		UserData operator = userDA.getUserById(userId);
		logService.write(operator, LogData.LogType.ACCOUNT_MODIFY.toString(),
				"User " + operator + " change " + user + "'s password.");

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectResult modifyAccount(long operateUserId, long userId, String username, String permission) {
		UserData user = userDA.getUserById(userId);
		if (user == null)
			return new ObjectResult("not_found_account", false);

		user.setUsername(username);
		//remove old permissions and renew the new ones
		userPermissionDA.deleteByUserId(userId);
		//add permission
		List<Permission> allPermission = permissionDA.queryAllPermission();
		String[] grandPermIds = permission.split("/");
		for(String pid : grandPermIds){
			for(Permission p : allPermission){
				if (pid.equals(p.getId()+"")){
					UserPermission up = new UserPermission();
					up.setUser(user);
					up.setPermission(p);
					userPermissionDA.save(up);
				}
			}
		}		
				
		// write log.
		UserData selfUser = userDA.getUserById(operateUserId);
		logService.write(selfUser, LogData.LogType.ACCOUNT_MODIFY.toString(), "User "+ selfUser + " modify user "+ user + " account information to (******).");

		return new ObjectResult(Result.OK, true);
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectResult removeAccount(long userId, long id) {
		UserData user = userDA.getUserById(id);
		if (user == null)
			return new ObjectResult("not_found_account", false);
//		userPermissionDA.deleteByUserId(id);
		userDA.deleteUser(user);

		// write log.
		UserData selfUser = userDA.getUserById(userId);
		logService.write(selfUser, LogData.LogType.ACCOUNT_DELETE.toString(),
				"User " + selfUser + " remove user " + user + ".");

		return new ObjectResult(Result.OK, true);
	}

}
