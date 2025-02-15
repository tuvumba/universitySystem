package dev.tuvumba.universityClient.controller;

import dev.tuvumba.universityClient.dto.StudentDto;
import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.dto.TeacherDto;
import dev.tuvumba.universityClient.service.DashboardService;
import dev.tuvumba.universityClient.service.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SubjectController {

    private final SubjectService subjectService;
    private final DashboardService dashboardService;

    public SubjectController(SubjectService subjectService, DashboardService dashboardService) {
        this.subjectService = subjectService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/subject/{subjectName}")
    public String getSubjectDetails(HttpSession session, @PathVariable String subjectName, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        String username = (String) session.getAttribute("username");
        List<String> roles = (List<String>) session.getAttribute("roles");


        SubjectDto subject = subjectService.getSubject(token, subjectName);
        List<TeacherDto> teachers = subjectService.getTeachers(token, subjectName);
        List<StudentDto> students = subjectService.getStudents(token, subjectName);


        List<SubjectDto> studiedSubjects = dashboardService.getStudiedSubjects(token, username);
        List<SubjectDto> taughtSubjects = dashboardService.getTaughtSubjects(token, username);

        if (subject == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Subject not found");
            redirectAttributes.addFlashAttribute("currentUrl", "/subject/" + subjectName);
            System.out.printf("Subject %s not found\n", subjectName);
            return "redirect:/error";
        }


        boolean isStudying = studiedSubjects.stream().anyMatch(s -> s.getId().equals(subject.getId()));
        boolean isTeaching = taughtSubjects.stream().anyMatch(s -> s.getId().equals(subject.getId()));
        System.out.println(roles );
        boolean isTeacher = roles.contains("TEACHER");
        boolean isAdmin = roles.contains("ADMIN");


        model.addAttribute("subject", subject);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);

        model.addAttribute("isStudying", isStudying);
        model.addAttribute("isTeaching", isTeaching);
        model.addAttribute("isTeacher", isTeacher);
        model.addAttribute("isAdmin", isAdmin);

        return "subjectDetails";
    }

    @PostMapping("/subjects/{subjectId}/study")
    public String startStudyingSubject(HttpSession session, @PathVariable Long subjectId, Model model) {

        try {
            subjectService.startStudying((String) session.getAttribute("jwt"), (String) session.getAttribute("username"),subjectId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "dashboard";
    }

    @DeleteMapping("/subjects/{subjectId}/study")
    public String stopStudyingSubject(HttpSession session, @PathVariable Long subjectId, Model model) {
        try {
            subjectService.stopStudying((String) session.getAttribute("jwt"), (String) session.getAttribute("username"),subjectId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "dashboard";
    }

    @PostMapping("/subjects/{subjectId}/teach")
    public String startTeachingSubject(HttpSession session, @PathVariable Long subjectId, Model model) {
        try {
            subjectService.startTeaching((String) session.getAttribute("jwt"), (String) session.getAttribute("username"),subjectId);
        } catch (Exception e) {
           model.addAttribute("errorMessage", e.getMessage());
        }
        return "dashboard";
    }

    @DeleteMapping("/subjects/{subjectId}/teach")
    public String stopTeachingSubject(HttpSession session, @PathVariable Long subjectId, RedirectAttributes redirectAttributes) {
        try {
            subjectService.stopTeaching((String) session.getAttribute("jwt"), (String) session.getAttribute("username"),subjectId);
        } catch (Exception e) {
           redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/subjects/create")
    public String createSubject(@RequestParam String subjectName,
                                @RequestParam String description,
                                HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            SubjectDto subjectDto = new SubjectDto();
            subjectDto.setName(subjectName);
            subjectDto.setDescription(description);
            System.out.println("Subject name: " + subjectDto.getName() + " description: " + subjectDto.getDescription());
            subjectService.createSubject(token, subjectDto);
            redirectAttributes.addFlashAttribute("errorMessage", "Subject created successfully!");
            return "redirect:/subject/" + subjectName;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating subject: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }


    @PostMapping("/subject/{subjectId}/update")
    public String updateSubject(
            HttpSession session,
            @PathVariable Long subjectId,
            @RequestParam String name,
            @RequestParam String description, Model model, RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("jwt");
        try {
            subjectService.updateSubject(token, subjectId, new SubjectDto(subjectId, name, description));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/subject/" + subjectId;
        }
        return "redirect:/subject/" + name;
    }

    @PostMapping("/subject/{subjectId}/delete")
    public String deleteSubject(HttpSession session, @PathVariable Long subjectId, RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("jwt");
        try {
            subjectService.deleteSubject(token, subjectId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/dashboard";
    }


}
