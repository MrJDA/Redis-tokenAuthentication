package com.oocl.web.controller;
import com.oocl.web.annotatons.AuthToken;
import com.oocl.web.entities.User;
import com.oocl.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user){
        return ResponseEntity.ok(userService.login(user));
    }

    @GetMapping("/getAllUsers")
    @AuthToken
    public List<User> Test(HttpServletRequest request, HttpServletResponse response){
        return userService.getAllUsers();
    }
}
