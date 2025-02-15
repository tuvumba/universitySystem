package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.domain.dto.TeacherDto;
import dev.tuvumba.theboringuniversity.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@Tag(name = "Teacher API", description = "API for managing teachers")

public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Get all teachers", description = "Retrieves a list of all teachers. Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())
    })
    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        return new ResponseEntity<>((List<TeacherDto>) teacherService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get a teacher by username", description = "Retrieves details of a specific teacher by email. Security: Available to all")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content())
    })
    @GetMapping("/{username}")
    public ResponseEntity<TeacherDto> getTeacherByUsername(@PathVariable String username) {
        try {
            return new ResponseEntity<> (teacherService.findByUsername(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search for teachers", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Search for teachers by name and/or surname, returns up to {limit}. Security: Available to all.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Teachers found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "404", description = "No teachers, error.", content = @Content())})
    @GetMapping("/find/{query}/{limit}")
    public ResponseEntity<List<TeacherDto>> findTeachers(@PathVariable String query, @PathVariable int limit) {
        try {
            return new ResponseEntity<>(teacherService.findTeachersContaining(query, limit), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get subjects taught by a teacher", description = "Retrieves a list of subjects that the teacher is teaching. Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subjects found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content())
    })
    @GetMapping("/{username}/subjects")
    public ResponseEntity<List<SubjectDto>> getSubjectsByUsername(@PathVariable String username) {
        try {
            return new ResponseEntity<>(teacherService.getSubjects(username), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new teacher", description = "Adds a new teacher to the system. Security: Available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Teacher created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDto teacherDto) {

        try {
            return new ResponseEntity<>(teacherService.save(teacherDto), HttpStatus.CREATED);
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update a teacher", description = "Updates the details of an existing teacher. Security: Available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeacherDto.class))),
            @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content())
    })
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable String username, @RequestBody TeacherDto teacherDto) {
        try {
            return new ResponseEntity<>(teacherService.update(username, teacherDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Delete a teacher", description = "Removes a teacher from the system. Security: Available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Teacher deleted", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Teacher not found", content = @Content())
    })
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and authentication.name!=#username")
    public ResponseEntity<String> deleteTeacher(@PathVariable String username) {
        try {
            teacherService.delete(username);
            return new ResponseEntity<>("Teacher deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get the most popular", description = "Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Best teacher found", content = @Content()),
            @ApiResponse(responseCode = "404", description = "No teachers are in the database", content = @Content())
    })
    @GetMapping("/leaderboard/best")
    public ResponseEntity<String> getTeacherWithMostStudents() {
        String text = teacherService.getTeacherWithMostStudents();
        if(text == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(text, HttpStatus.OK);
    }
}
