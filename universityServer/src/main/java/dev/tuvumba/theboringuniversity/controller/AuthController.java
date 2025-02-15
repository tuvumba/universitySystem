package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.Student;
import dev.tuvumba.theboringuniversity.domain.Teacher;
import dev.tuvumba.theboringuniversity.domain.User;
import dev.tuvumba.theboringuniversity.domain.dto.AuthResponseDto;
import dev.tuvumba.theboringuniversity.repository.StudentRepository;
import dev.tuvumba.theboringuniversity.repository.TeacherRepository;
import dev.tuvumba.theboringuniversity.repository.UserRepository;
import dev.tuvumba.theboringuniversity.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "API for managing login")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    public AuthController(JwtUtil jwtUtil, StudentRepository studentRepository, TeacherRepository teacherRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Login Okay", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content())})
    @Operation(summary = "Login to receive a token.",
            description = "If the username and password are right, the user will receive their JWT Bearer token.")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequest loginRequest) {
        Optional <User> user = userRepository.findById(loginRequest.getUsername());
        Optional<Teacher> teacher = teacherRepository.findById(loginRequest.getUsername());
        Optional<Student> student = studentRepository.findById(loginRequest.getUsername());


        if (user.isPresent() && user.get().getPassword().equals(loginRequest.getPassword())) {
            List<String> roles = new ArrayList<>();

            if (teacher.isPresent()) {
                roles.add("TEACHER");
            }
            if (student.isPresent()) {
                roles.add("STUDENT");
            }

            if(user.get().getUsername().equals("admin")) {
                roles.add("ADMIN");
            }

            String token = jwtUtil.generateToken(loginRequest.getUsername(), roles);
            return ResponseEntity.ok(new AuthResponseDto(token, new ArrayList<>(roles) {
            }));
        } else {
            return ResponseEntity.status(401).body(new AuthResponseDto("", new ArrayList<>()));
        }
    }


    @Setter
    @Getter
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
