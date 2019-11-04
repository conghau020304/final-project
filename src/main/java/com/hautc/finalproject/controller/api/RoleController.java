package com.hautc.finalproject.controller.api;

import com.hautc.finalproject.dto.RoleDTO;
import com.hautc.finalproject.model.Role;
import com.hautc.finalproject.service.IRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/roles")
    public List<RoleDTO> getAllRole(){
        List<Role> roles =  roleService.findAllRole();
        return roles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private RoleDTO convertToDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

}
