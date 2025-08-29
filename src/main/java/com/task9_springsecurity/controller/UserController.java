package com.task9_springsecurity.controller;

import com.task9_springsecurity.service.UserService;
import com.task9_springsecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String userHome(Model model, Authentication auth) {
        User loggedUser = userService.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("users", List.of(loggedUser));
        return "users/list";
    }
}
