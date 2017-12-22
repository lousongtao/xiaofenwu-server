/**
 * Copyright 2012 - 2013 Fglinxun Studios, Inc.
 * All rights reserved.
 */
package com.shuishou.retailer.account.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface IUserDataAccessor {

  Session getSession();

  void persistUser(UserData user);

  void updateUser(UserData user);

  void deleteUser(UserData user);

  Serializable saveUser(UserData user);

  void saveOrUpdateUser(UserData user);

  UserData getUserById(long id);

  UserData getUserByUsername(String username);

  List<UserData> getAllUsers();
  
}
