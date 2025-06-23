package com.graduation.wellness.service;

import com.graduation.wellness.exception.BaseApiExceptions;
import com.graduation.wellness.mapper.RoleMapper;
import com.graduation.wellness.model.dto.RoleDto;
import com.graduation.wellness.model.entity.Role;
import com.graduation.wellness.repository.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;

    private final RoleMapper roleMapper;

    public List<Role> findAll() {
        return roleRepo.findAll();
    }

    public RoleDto findById(Long id){
        Role role = roleRepo.findById(id).orElseThrow(()-> new BaseApiExceptions(String.format("No Record with role_id [%d] found in data base " , id) , HttpStatus.NOT_FOUND));
        RoleDto roleDto= roleMapper.Map(role);

        return roleDto;
    }

    public Role findByName(String name){
        Role role = roleRepo.findByName(name);
        if(role == null)
            throw new BaseApiExceptions(String.format("No Record with role_name [%s] found in data base " , name),HttpStatus.NOT_FOUND);
        return role;
    }

    public Role save(Role entity) {
            return roleRepo.save(entity);
    }
}
