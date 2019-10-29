package com.oocl.web.mapper;

import com.oocl.web.entities.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    void addRole(Role role);
}
