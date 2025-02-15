package dev.tuvumba.universityClient.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FallbackController {

    @GetMapping("{path:[^.]*}")
    public String handleUnknownPages(HttpSession session, @PathVariable String path) {
        if (session.getAttribute("jwt") == null) {
            return "redirect:/login";
        } else {
            return "redirect:/dashboard";
        }
    }
}

