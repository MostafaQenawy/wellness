package com.graduation.wellness.mapper;

import com.graduation.wellness.model.dto.UserDto;
import com.graduation.wellness.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //map from entity to dto
    @Mapping(target = "password" , ignore = true)
    UserDto Map(User user);

    //map from dto to entity
    User UnMap(UserDto userDto);
}
