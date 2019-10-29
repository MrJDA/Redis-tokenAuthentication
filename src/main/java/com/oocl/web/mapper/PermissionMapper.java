package com.oocl.web.mapper;

import com.oocl.web.entities.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper {
    void addPermission(Permission permission);
}
