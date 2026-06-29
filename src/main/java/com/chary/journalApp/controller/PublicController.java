package com.chary.journalApp.controller;

import com.chary.journalApp.entity.User;
import com.chary.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UserService userService;
    @GetMapping("/health-check")
    public String healtCheck(){
        return "ok";
    }
    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
        userService.saveNewEntry(user);
    }
}
