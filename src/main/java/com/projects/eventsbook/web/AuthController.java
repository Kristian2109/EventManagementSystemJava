package com.projects.eventsbook.web;


import com.projects.eventsbook.DTO.userDomain.LoginUserDTO;
import com.projects.eventsbook.DTO.userDomain.RegisterUserDTO;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.mapper.UserMapper;
import com.projects.eventsbook.service.user.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerUserDTO", new RegisterUserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid RegisterUserDTO registerUserDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            return "redirect:register";
        }
        try {
            User authenticatedUser = authService.registerUser(UserMapper.toUser(registerUserDTO));
            session.setAttribute("currentUser", UserMapper.toUserProfileDTO(authenticatedUser));
            return "redirect:/profile";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:register";
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginUserDTO", new LoginUserDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute("loginUserDTO") LoginUserDTO loginUserDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            return "redirect:login";
        }
        try {
            User authenticatedUser = authService.loginUser(UserMapper.toUser(loginUserDTO));
            session.setAttribute("currentUser", UserMapper.toUserProfileDTO(authenticatedUser));
            return "redirect:/profile";
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/auth/login";
    }
}
