package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.RoleDto;
import com.graduation.wellness.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto Map(Role role);

    //map from dto to entity
    Role UnMap(RoleDto roleDto);
}
