package com.hautc.finalproject.repository;

import com.hautc.finalproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    public User findByUsernameAndEnabled(String userName, short enabled);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User findByUserById(Integer id);

    public void deleteById(Integer id);

    List<User> findByUsernameContaining(String tk);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.password = ?1 where username = ?2")
    void changePassword(String password, String username);
}
