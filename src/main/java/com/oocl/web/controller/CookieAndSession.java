package com.oocl.web.controller;

import com.oocl.web.annotatons.AuthToken;
import com.oocl.web.entities.User;
import com.oocl.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@AuthToken
public class CookieAndSession {

    @Autowired
    private UserService userService;



}
