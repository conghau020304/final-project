package com.hautc.finalproject.service;

import com.hautc.finalproject.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    User getUserByUserName(String userName);

    List<User> getAllActiveUserInfo();

    User getUserInfoById(Integer id);

    User addUser(User user);

    User updateUser(Integer id, User userRecord);

    void deleteUser(Integer id);

    List<User> findByUsername(String tk);

    void changePassword(String password, String username);
}
