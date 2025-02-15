package dev.tuvumba.theboringuniversity.controller;

import dev.tuvumba.theboringuniversity.domain.Subject;
import dev.tuvumba.theboringuniversity.domain.dto.SubjectDto;
import dev.tuvumba.theboringuniversity.service.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    SubjectDto subject = new SubjectDto(1L, "Mathematics", "lalala");

    @Test
    void testGetAllSubjects() throws Exception {

        when(subjectService.findAll()).thenReturn(List.of(subject));

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Mathematics"))
                .andExpect(jsonPath("$[0].description").value("lalala"));
    }

    @Test
    void testGetSubjectById() throws Exception {
        when(subjectService.findById(1L)).thenReturn(subject);

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mathematics"))
                .andExpect(jsonPath("$.description").value("lalala"));
    }

    @Test
    void testGetSubjectById_NotFound() throws Exception {
        when(subjectService.findById(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/subjects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSubject() throws Exception {
        when(subjectService.save(any())).thenReturn(subject);

        mockMvc.perform(post("/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Physics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mathematics"))
                .andExpect(jsonPath("$.description").value("lalala"));
    }

    @Test
    void testUpdateSubject() throws Exception {
        SubjectDto updatedSubject = subject;
        updatedSubject.setName("Updated Physics");
        when(subjectService.update(eq(1L), any())).thenReturn(updatedSubject);

        mockMvc.perform(put("/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Physics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Physics"))
                .andExpect(jsonPath("$.description").value("lalala"));
    }

    @Test
    void testUpdateSubject_NotFound() throws Exception {
        when(subjectService.update(eq(1L), any())).thenThrow(new RuntimeException());

        mockMvc.perform(put("/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Physics\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteSubject() throws Exception {
        doNothing().when(subjectService).delete(1L);

        mockMvc.perform(delete("/subjects/1"))
                .andExpect(status().isNoContent());

        verify(subjectService, times(1)).delete(1L);
    }
}

