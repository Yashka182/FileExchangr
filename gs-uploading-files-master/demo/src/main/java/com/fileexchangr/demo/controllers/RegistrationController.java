package com.fileexchangr.demo.controllers;

import com.fileexchangr.demo.configs.StringConstants;
import com.fileexchangr.demo.entitys.Role;
import com.fileexchangr.demo.entitys.User;
import com.fileexchangr.demo.repositories.UserRepository;
import com.fileexchangr.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String username, @RequestParam String password, Map<String, Object> model){
        User user = new User(username, password);
        Boolean add = userService.addUser(user);

        if(!add){
            model.put(StringConstants.message, StringConstants.userExists);
            return "registration";
        }
        return "redirect:/login";
    }
}

