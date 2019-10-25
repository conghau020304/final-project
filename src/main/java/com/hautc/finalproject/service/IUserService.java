package com.hautc.finalproject.service;

import com.hautc.finalproject.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {

    User findByUsername(String userName);

    List<User> getAllUserInfo();

    Optional<User> getUserInfoById(Integer id);

    User addUser(User user);

    User updateUser(Integer id, User userRecord);

    void deleteUser(Integer id);

    List<User> searchByUsername(String username);

    void changePassword(String password, String username);

    boolean checkDuplicateUsernameBeforeUpdate(String oldUsername, String newUsername);
}
