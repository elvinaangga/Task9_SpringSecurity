package com.task9_springsecurity.controller;

import com.task9_springsecurity.service.UserService;
import com.task9_springsecurity.model.User;
import com.task9_springsecurity.dao.UserDao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    private UserDao userDao;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    @GetMapping("/users")
    public String listUsers(Model model, Authentication authentication) {
        User loggedUser = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // kalau role USER, hanya tampilkan data sendiri
        if (loggedUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_USER"))) {
            model.addAttribute("users", List.of(loggedUser));
        } else {
            // kalau ADMIN, tampilkan semua
            model.addAttribute("users", userService.findAll());
        }

        return "users/list";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
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
            // ada error validasi → kembali ke form
            return "users/form";
        }

        // tidak ada error → simpan user
        userService.save(user);
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


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/users";
    }

    @GetMapping("/user")
    public String userHome(Model model, Authentication auth) {
        Optional<User> currentUserOpt = userDao.findByEmail(auth.getName());
        User currentUser = currentUserOpt.orElse(null); // kalau ga ketemu → null
        List<User> users = currentUser != null ? List.of(currentUser) : Collections.emptyList();
        model.addAttribute("users", users);
        return "users/list";
    }
}
