package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.Student;
import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.service.StudentService;
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

@Tag(name = "Student API", description = "API for managing students")
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Get all students", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieve a list of all students. Security: Available only to teachers and admins.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful retrieval", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())})
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return new ResponseEntity<>((List<StudentDto>) studentService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get student by username", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieve a student by their unique username. Security: Available fully to teachers and admins. Students can only retrieve themselves.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or authentication.name == #username")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable String username) {
        try {
            return new ResponseEntity<>(studentService.findById(username), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Search for students", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Search for students by name and/or surname, returns up to {limit}. Security: Available only to teachers and admins.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    @GetMapping("/find/{query}/{limit}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StudentDto>> getStudentById(@PathVariable String query, @PathVariable int limit) {
        try {
            return new ResponseEntity<>(studentService.findStudentsContaining(query, limit), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get subjects by student username", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieve all subjects a student is enrolled in. Security: Available fully to teachers and admins. Students can only retrieve themselves.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Subjects found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    @GetMapping("/{username}/subjects")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or authentication.name == #username")
    public ResponseEntity<List<SubjectDto>> getSubjectsByStudentId(@PathVariable String username) {
        try {
            return new ResponseEntity<>(studentService.getSubjects(username), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create a new student", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Create a new student record in the database. Security: Available only to admins.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Student created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content())})
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) {
        try {
            return new ResponseEntity<>(studentService.save(studentDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing student", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Update student details by their username. Security: Available only to admins.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDto.class))),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable String username, @RequestBody StudentDto studentDto) {
        try {
            return new ResponseEntity<>(studentService.update(username, studentDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete a student", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Remove a student from the system by username. Security: Available only to teachers.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Student deleted", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Student not found", content = @Content())})
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteStudent(@PathVariable String username) {
        try {
            studentService.delete(username);
            return new ResponseEntity<>("Successfully deleted.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
