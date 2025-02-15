package dev.tuvumba.universityClient.controller;

import dev.tuvumba.universityClient.dto.StudentDto;
import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{username}")
    public String getStudentDetails(@PathVariable String username, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        List<String> roles = (List<String>) session.getAttribute("roles");

        StudentDto student = studentService.getStudent(token, username);
        List<SubjectDto> subjects = studentService.getSubjectsForStudent(token, username);

        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            redirectAttributes.addFlashAttribute("currentUrl", "/students/" + username);
            return "redirect:/error";
        }

        boolean isAdmin = roles.contains("ADMIN");

        model.addAttribute("student", student);
        model.addAttribute("subjects", subjects);
        model.addAttribute("isAdmin", isAdmin);

        return "studentDetails";
    }

    @PostMapping("/create")
    public String createStudent(@RequestParam String username, @RequestParam String name, @RequestParam String surname,
                                HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            studentService.createStudent(token, new StudentDto(username, name, surname));
            redirectAttributes.addFlashAttribute("success", "Student created successfully!");
            return "redirect:/students/" + username;
        } catch (Exception e) {
            System.out.println("Error creating student: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error creating student: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/{username}/update")
    public String updateStudent(@PathVariable String username,
                                @RequestParam String name,
                                @RequestParam String surname,
                                HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");

        try {
            studentService.updateStudent(token, username, new StudentDto(username, name, surname));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
        }
        return "redirect:/students/" + username;
    }

    @PostMapping("/{username}/delete")
    public String deleteStudent(@PathVariable String username, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            studentService.deleteStudent(token, username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}

