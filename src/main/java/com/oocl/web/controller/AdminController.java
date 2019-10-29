package com.oocl.web.controller;

import com.oocl.web.annotatons.AuthToken;
import com.oocl.web.annotatons.RoleNum;
import com.oocl.web.enums.RoleEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@AuthToken
@RoleNum(role = RoleEnum.MANAGER)
public class AdminController {

    @GetMapping("/getAdminInfo")
    public String getAdminInfo(){
        return "my Admin info";
    }
}
