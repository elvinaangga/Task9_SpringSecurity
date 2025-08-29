package com.task9_springsecurity.controller;

import com.task9_springsecurity.model.User;
import com.task9_springsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/users")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @GetMapping
    public String listUsers(Model model, Authentication authentication) {
        User loggedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            model.addAttribute("users", userService.findAll()); // semua user
        } else {
            model.addAttribute("users", List.of(loggedUser)); // cuma user sendiri
        }

        return "users/list";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        model.addAttribute("user", user);
        return "users/edit_form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "users/form";
        }
        System.out.println("Saving user: " + user.getEmail());
        userService.save(user);
        System.out.println("Saved successfully!");
        redirectAttributes.addFlashAttribute("successMessage", "User saved successfully!");
        return "redirect:/users";
    }


    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "users/edit_form"; // balik ke edit form kalau ada error
        }

        userService.update(user); // service update, jangan save lagi
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/users";
    }
}