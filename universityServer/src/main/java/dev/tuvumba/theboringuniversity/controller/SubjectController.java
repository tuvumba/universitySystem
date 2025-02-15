package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.domain.dto.TeacherDto;
import dev.tuvumba.theboringuniversity.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@Tag(name = "Subject API", description = "API for managing subjects")

public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Get all subjects", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves a list of all subjects. Security: Available to all.")
    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAllSubjects() {
        return new ResponseEntity<>( (List<SubjectDto>) subjectService.findAll(), HttpStatus.OK) ;
    }

    @Operation(summary = "Get all students of a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves a list of all students studying this subject. Security: Available to teachers and admins.")
    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StudentDto>> getStudents(@PathVariable Long id) {
        return new ResponseEntity<>( (List<StudentDto>) subjectService.getStudents(id), HttpStatus.OK) ;
    }

    @Operation(summary = "Get all teachers of a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves a list of all teachers studying this subject. Security: Available to all.")
    @GetMapping("/{id}/teachers")
    public ResponseEntity<List<TeacherDto>> getTeachers(@PathVariable Long id) {
        return new ResponseEntity<>( (List<TeacherDto>) subjectService.getTeachers(id), HttpStatus.OK) ;
    }

    @Operation(summary = "Get a subject by ID", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves details of a specific subject. Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getSubjectById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(subjectService.findById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a subject by name", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves details of a specific subject. Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content())
    })
    @GetMapping("/find/{name}")
    public ResponseEntity<SubjectDto> getSubjectByName(@PathVariable String name) {
        try {
            return new ResponseEntity<>(subjectService.findByName(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a subject by a part of the name.", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Retrieves details of a specific subject Security: Available to all.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content())
    })
    @GetMapping("/find/{name}/{limit}")
    public ResponseEntity<List<SubjectDto>> getSubjectByNameContains(@PathVariable String name, @PathVariable int limit) {
        try {
            return new ResponseEntity<>(subjectService.findByNameStart(name, limit), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    

    @Operation(summary = "Create a new subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Adds a new subject to the system. Security: available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subject created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto) {
        try {
            System.out.println("Subject name: " + subjectDto.getName() + "subject description: " + subjectDto.getDescription());
            return new ResponseEntity<>(subjectService.save(subjectDto), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Update a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Updates the details of an existing subject. Security: available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content())
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<SubjectDto> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto) {
        try {
            return new ResponseEntity<>(subjectService.update(id, subjectDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Delete a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Removes a subject from the system. Security: available only to admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subject deleted", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Subject not found", content = @Content())
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteSubject(@PathVariable Long id) {
        try {
            subjectService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Deleting the subject failed", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Assign a teacher to a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Links a teacher to a subject. Security: Teachers only can assign themselves. Admins can do whatever they want :)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher assigned successfully", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Subject or teacher not found", content = @Content())
    })
    @PostMapping("/{subjectId}/teachers/{teacherUsername}")
    @PreAuthorize("(hasRole('ROLE_TEACHER') and authentication.name == #teacherUsername) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> assignTeacher(@PathVariable Long subjectId, @PathVariable String teacherUsername) {
        try {
            subjectService.assignTeacher(subjectId, teacherUsername);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Assigning the teacher failed", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Unassign a teacher from a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Removes a teacher from a subject. Security: Teachers only can assign themselves. Admins can do this also.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher unassigned successfully", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Subject or teacher not found", content = @Content())
    })
    @DeleteMapping("/{subjectId}/teachers/{teacherUsername}")
    @PreAuthorize("(hasRole('ROLE_TEACHER') and authentication.name == #teacherUsername) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> unassignTeacher(@PathVariable Long subjectId, @PathVariable String teacherUsername) {
        try {
            subjectService.unassignTeacher(subjectId, teacherUsername);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Unassigning the teacher failed", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Assign a student to a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Enrolls a student in a subject. Security: Teachers and admins have access. Students can only assign themselves.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student assigned successfully", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Subject or student not found", content = @Content())
    })
    @PostMapping("/{subjectId}/students/{studentUsername}")
    @PreAuthorize("hasRole('ROLE_TEACHER') or authentication.name == #studentUsername or hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> assignStudent(@PathVariable Long subjectId, @PathVariable String studentUsername) {
        try {
            subjectService.assignStudent(subjectId, studentUsername);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Assigning the student failed", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Unassign a student from a subject", security = @SecurityRequirement(name = "BearerAuth"),
            description = "Removes a student from a subject. Security: Teachers and admins have access. Students can only unassign themselves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student unassigned successfully", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Subject or student not found", content = @Content())
    })
    @PreAuthorize("hasRole('ROLE_TEACHER') or authentication.name == #studentUsername or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{subjectId}/students/{studentUsername}")
    public ResponseEntity<String> unassignStudent(@PathVariable Long subjectId, @PathVariable String studentUsername) {
        try {
            subjectService.unassignStudent(subjectId, studentUsername);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Unassigning the student failed", HttpStatus.NOT_FOUND);
        }
    }
}
