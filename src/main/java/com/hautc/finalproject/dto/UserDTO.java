package com.hautc.finalproject.dto;

import com.hautc.finalproject.model.Role;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private Integer id;

    @Size(min = 6, message = "{user.Username.Size.min}")
    private String username;

    @Size(min = 6, message = "{user.Password.Size.min}")
    private String password;
    private short enabled;
    private Set<Role> roles = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public short getEnabled() {
        return enabled;
    }

    public void setEnabled(short enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
