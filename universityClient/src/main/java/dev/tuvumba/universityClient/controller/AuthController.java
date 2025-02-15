package dev.tuvumba.universityClient.controller;

import dev.tuvumba.universityClient.dto.UserLoginDto;
import dev.tuvumba.universityClient.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        UserLoginDto loginDTO = new UserLoginDto(username, password);

        try {
            authService.authenticate(loginDTO);
            session.setAttribute("jwt", authService.token);
            session.setAttribute("username", username);
            session.setAttribute("roles", authService.roles);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Invalid credentials");
            return "login";
        }
    }
}