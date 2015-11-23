package com.mkyong.helloworld.dao;

import com.mkyong.helloworld.model.UserAttempts;

public interface UserAttemptsDao
{
  void updateFailAttempts(String username);
  void resetFailAttempts(String username);
  UserAttempts getUserAttempts(String username);
}
