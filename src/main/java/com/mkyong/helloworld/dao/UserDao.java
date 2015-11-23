package com.mkyong.helloworld.dao;

import com.mkyong.helloworld.model.User;

public interface UserDao
{
  User findByUserName(String username);
}
