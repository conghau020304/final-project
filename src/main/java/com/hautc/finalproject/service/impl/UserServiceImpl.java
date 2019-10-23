package com.hautc.finalproject.service.impl;

import com.hautc.finalproject.model.Role;
import com.hautc.finalproject.model.User;
import com.hautc.finalproject.repository.IRoleRepository;
import com.hautc.finalproject.repository.IUserRepository;
import com.hautc.finalproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    private static final short ENABLED = 1;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndEnabled(username, ENABLED);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Role> roles = user.getRoles();
        roles.forEach(r -> grantedAuthorities.add(new SimpleGrantedAuthority(r.getName())));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUsernameAndEnabled(userName, ENABLED);
    }

    @Override
    public List<User> getAllActiveUserInfo() {
        return userRepository.findAll();
    }

    @Override
    public User getUserInfoById(Integer id) {
        return userRepository.findByUserById(id);
    }

    @Override
    public User addUser(User user) {
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_MEMBER"));
        user.setRoles(roles);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User userRecord) {
        User u = userRepository.findByUserById(id);
        u.setUsername(userRecord.getUsername());
        u.setPassword(new BCryptPasswordEncoder().encode(userRecord.getPassword()));
        u.setEnabled(userRecord.getEnabled());
        return userRepository.save(u);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByUsername(String tk) {
        return userRepository.findByUsernameContaining(tk);
    }

    @Override
    public void changePassword(String password, String username) {
        userRepository.changePassword(password, username);
    }
}
