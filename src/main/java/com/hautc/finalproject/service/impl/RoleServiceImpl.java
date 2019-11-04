package com.hautc.finalproject.service.impl;

import com.hautc.finalproject.model.Role;
import com.hautc.finalproject.repository.IRoleRepository;
import com.hautc.finalproject.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleServiceImpl")
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }
}
