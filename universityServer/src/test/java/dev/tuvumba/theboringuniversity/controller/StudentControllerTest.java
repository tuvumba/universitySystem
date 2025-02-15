package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.dto.StudentDto;
import dev.tuvumba.theboringuniversity.security.JwtUtil;
import dev.tuvumba.theboringuniversity.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("production")
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        studentDto = new StudentDto("john@test", "JOHN", "TEST");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateStudent() throws Exception {
        when(studentService.save(any(StudentDto.class))).thenReturn(studentDto);

        MvcResult result = mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\":\"john@test\",\"name\":\"JOHN\",\"surname\":\"TEST\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        String expectedJson = "{\"username\":\"john@test\",\"name\":\"JOHN\",\"surname\":\"TEST\"}";
        JSONAssert.assertEquals(expectedJson, actualResponse, false);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateStudent_InvalidInput() throws Exception {
        when(studentService.save(any(StudentDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\":\"\",\"name\":\"\",\"surname\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testGetAllStudents_Success() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.singletonList(studentDto));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("john@test"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testGetAllStudents_Forbidden() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "john@test", roles = "STUDENT")
    void testGetStudentById_Success() throws Exception {
        when(studentService.findById("john@test")).thenReturn(studentDto);

        mockMvc.perform(get("/students/john@test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john@test"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testGetStudentById_NotFound() throws Exception {
        doThrow(new RuntimeException()).when(studentService).findById("unknown");

        mockMvc.perform(get("/students/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteStudent_Success() throws Exception {
        doNothing().when(studentService).delete("john@test");

        mockMvc.perform(delete("/students/john@test"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteStudent_NotFound() throws Exception {
        doThrow(new RuntimeException()).when(studentService).delete("unknown");

        mockMvc.perform(delete("/students/unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateStudent_Success() throws Exception {
        when(studentService.update(eq("john@test"), any(StudentDto.class))).thenReturn(studentDto);

        mockMvc.perform(put("/students/john@test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\":\"john@test\",\"name\":\"JOHN\",\"surname\":\"TEST\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john@test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateStudent_NotFound() throws Exception {
        when(studentService.update(eq("unknown"), any(StudentDto.class)))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/students/unknown")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\":\"unknown\",\"name\":\"TEST\",\"surname\":\"USER\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void testSearchStudents_Success() throws Exception {
        when(studentService.findStudentsContaining("John", 10)).thenReturn(List.of(studentDto));

        mockMvc.perform(get("/students/find/John/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("john@test"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void testSearchStudents_Forbidden() throws Exception {
        mockMvc.perform(get("/students/find/John/10"))
                .andExpect(status().isForbidden());
    }
}
