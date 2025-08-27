package com.task9_springsecurity.controller;


import com.task9_springsecurity.model.User;
import com.task9_springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserRoleController {
    private final UserService userService;

    @Autowired
    public UserRoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userHome(Model model, Authentication authentication) {
        // Ambil user yang login
        User loggedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tampilkan cuma data user itu sendiri
        model.addAttribute("users", List.of(loggedUser));

        return "users/list";
    }
}
