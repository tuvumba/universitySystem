package dev.tuvumba.universityClient.controller;

import dev.tuvumba.universityClient.dto.StudentDto;
import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.dto.TeacherDto;
import dev.tuvumba.universityClient.service.DashboardService;
import dev.tuvumba.universityClient.service.StudentService;
import dev.tuvumba.universityClient.service.SubjectService;
import dev.tuvumba.universityClient.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;


    public DashboardController(DashboardService dashboardService, StudentService studentService, SubjectService subjectService, TeacherService teacherService) {
        this.dashboardService = dashboardService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
    }

    private List<SubjectDto> studiedSubjects;
    private List<SubjectDto> taughtSubjects;
    private String bestTeacher;


    private void refreshData(String token, String username)
    {
        studiedSubjects = dashboardService.getStudiedSubjects(token, username);
        taughtSubjects = dashboardService.getTaughtSubjects(token, username);
        bestTeacher = dashboardService.getBestTeacher(token);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String token = (String) session.getAttribute("jwt");
        if (token == null) {
            return "redirect:/login";
        }

        String username = (String) session.getAttribute("username");
        System.out.printf("Username is %s\n", username);
        if(username == null) {
            return "redirect:/login";
        }
        refreshData(token, username);

        model.addAttribute("taughtSubjects", taughtSubjects);
        model.addAttribute("studiedSubjects", studiedSubjects);
        model.addAttribute("username", username);
        List<String> roles = (List<String>) session.getAttribute("roles");
        model.addAttribute("isAdmin", roles.contains("ADMIN"));
        model.addAttribute("isTeacher", roles.contains("TEACHER"));
        model.addAttribute("isStudent", roles.contains("STUDENT"));
        model.addAttribute("bestTeacher", bestTeacher);

        return "dashboard";
    }

    @PostMapping("/search")
    public String search(HttpSession session, @RequestParam String query, @RequestParam String category, @RequestParam int limit, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");

        List<String> roles = (List<String>) session.getAttribute("roles");
        model.addAttribute("isAdmin", roles.contains("ADMIN"));
        model.addAttribute("isTeacher", roles.contains("TEACHER"));


        System.out.println("Searching for " + category + "with query " + query + " limit " + limit + " and roles " + roles);

        if ("students".equals(category) && (roles.contains("ADMIN") || roles.contains("TEACHER"))) {
            List<StudentDto> results;
            if(query == null || query.isEmpty()) {
                 results = studentService.getAllStudents(token).stream().limit(limit).collect(Collectors.toList());;
            }
            else {
                results = studentService.searchStudents(token, query, limit);
            }
            redirectAttributes.addFlashAttribute("searchResults", results);
            System.out.println("Search results: " + results);
        } else if ("subjects".equals(category)) {
            List<SubjectDto> results;
            if(query == null || query.isEmpty()) {
                results = subjectService.getAllSubjects(token).stream().limit(limit).collect(Collectors.toList());;
            }
            else
            {
                results = subjectService.searchSubjects(token, query, limit);
            }
            redirectAttributes.addFlashAttribute("searchResults", results);
            System.out.println("Search results: " + results);
        } else if ("teachers".equals(category)) {
            List<TeacherDto> results;
            if(query == null || query.isEmpty()) {
                results = teacherService.getAllTeachers(token).stream().limit(limit).collect(Collectors.toList());
            }
            else {
                results = teacherService.searchTeachers(token, query, limit);
            }
            redirectAttributes.addFlashAttribute("searchResults", results);
            System.out.println("Search results: " + results);
        }

        redirectAttributes.addFlashAttribute("category", category);
        redirectAttributes.addFlashAttribute("query", query);
        redirectAttributes.addFlashAttribute("limit", limit);


        return "redirect:/dashboard";
    }







}


