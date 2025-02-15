package dev.tuvumba.universityClient.controller;

import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.dto.TeacherDto;
import dev.tuvumba.universityClient.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/{username}")
    public String getTeacherDetails(@PathVariable String username, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        List<String> roles = (List<String>) session.getAttribute("roles");

        TeacherDto teacher = teacherService.getTeacher(token, username);
        List<SubjectDto> subjects = teacherService.getSubjectsTaughtByTeacher(token, username);

        if (teacher == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Teacher not found");
            redirectAttributes.addFlashAttribute("currentUrl", "/teachers/" + username);
            return "redirect:/error";
        }

        boolean isAdmin = roles.contains("ADMIN");

        model.addAttribute("teacher", teacher);
        model.addAttribute("subjects", subjects);
        model.addAttribute("isAdmin", isAdmin);

        return "teacherDetails";
    }

    @PostMapping("/create")
    public String createTeacher(@RequestParam String username, @RequestParam String name, @RequestParam String surname,
                                @RequestParam String titles, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            teacherService.createTeacher(token, new TeacherDto(name, surname, username, titles));
            redirectAttributes.addFlashAttribute("success", "Teacher created successfully!");
            return "redirect:/teachers/" + username;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating teacher: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/{username}/update")
    public String updateTeacher(@PathVariable String username,
                              @RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String titles, HttpSession session) {
        String token = (String) session.getAttribute("jwt");
        teacherService.updateTeacher(token, username, new TeacherDto(name, surname, username, titles));
        return "redirect:/teachers/" + username;
    }

    @PostMapping("/{username}/delete")
    public String deleteTeacher(@PathVariable String username, HttpSession session, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            teacherService.deleteTeacher(token, username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting teacher: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}

