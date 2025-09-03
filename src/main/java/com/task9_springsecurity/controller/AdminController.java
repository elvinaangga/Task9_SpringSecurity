package com.task9_springsecurity.controller;

import com.task9_springsecurity.model.Role;
import com.task9_springsecurity.model.User;
import com.task9_springsecurity.repository.RoleRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "users/form";
    }

    @GetMapping
    public String listUsers(Model model, Authentication authentication) {
        User loggedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = loggedUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (isAdmin) {
            List<User> users = userService.findAll();
            List<Role> allRoles = roleRepository.findAll();

            model.addAttribute("users", users);
            model.addAttribute("pageTitle", "Admin Panel");
            model.addAttribute("tableTitle", "All Users");
            model.addAttribute("user", new User());  // untuk modal create
            model.addAttribute("allRoles", allRoles); // semua user
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
        model.addAttribute("allRoles", roleRepository.findAll());
        return "users/edit_form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam("roleIds")  List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            System.out.println("Validation errors: " + result.getAllErrors());
            return "redirect:/admin";
        }
        List<Role> selectedRoles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(selectedRoles));
        userService.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "User saved successfully!");
        return "redirect:/admin";
    }


    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "users/edit_form";
        }

        if (roleIds != null) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }

        userService.update(user);
        redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/admin";
    }
}