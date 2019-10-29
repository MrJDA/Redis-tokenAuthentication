package com.oocl.web.mapper;

import com.oocl.web.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectUserByUserName(User user);
    void addUser(User user);
    List<User> selectAllUser();
}
