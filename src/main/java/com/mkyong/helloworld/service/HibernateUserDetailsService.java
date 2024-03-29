package com.mkyong.helloworld.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mkyong.helloworld.dao.UserDao;
import com.mkyong.helloworld.model.UserRole;

public class HibernateUserDetailsService implements UserDetailsService
{

  private UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
  {

    com.mkyong.helloworld.model.User user = userDao.findByUserName(username);
    List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRole());

    return buildUserForAuthentication(user, authorities);

  }

  // Converts com.mkyong.users.model.User user to
  // org.springframework.security.core.userdetails.User
  private User buildUserForAuthentication(com.mkyong.helloworld.model.User user,
      List<GrantedAuthority> authorities)
  {
    return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), 
        user.isCredentialsNonExpired(), user.isAccountNonLocked(),authorities);
  }

  private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles)
  {

    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

    // Build user's authorities
    for (UserRole userRole : userRoles)
    {
      setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
    }

    List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

    return Result;
  }

  public UserDao getUserDao()
  {
    return userDao;
  }

  public void setUserDao(UserDao userDao)
  {
    this.userDao = userDao;
  }

}
