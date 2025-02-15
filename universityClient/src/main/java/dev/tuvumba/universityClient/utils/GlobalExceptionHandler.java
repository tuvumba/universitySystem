package dev.tuvumba.universityClient.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex, Model model, HttpServletRequest request) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later. " + ex.getMessage());
        model.addAttribute("currentUrl", currentUrl);
        System.out.println("[GlobalExceptionHandler] Server error: " + ex.getMessage() + " currentUrl: " + currentUrl);

        return "error";
    }
}


