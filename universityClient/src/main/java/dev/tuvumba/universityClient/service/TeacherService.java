package dev.tuvumba.universityClient.service;

import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.dto.TeacherDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TeacherService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8081/teachers";

    private HttpEntity<?> prepareEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public TeacherDto getTeacher(String token, String username) {
        try {
            ResponseEntity<TeacherDto> response = restTemplate.exchange(
                    BASE_URL + "/" + username,
                    HttpMethod.GET,
                    prepareEntity(token),
                    TeacherDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SubjectDto> getSubjectsTaughtByTeacher(String token, String username) {
        TeacherDto teacher =getTeacher(token, username);
        if (teacher == null) {
            return null;
        }
        ResponseEntity<List<SubjectDto>> teachersResponse = restTemplate.exchange(
                BASE_URL + "/" + teacher.getUsername() + "/subjects",
                HttpMethod.GET,
                prepareEntity(token),
                new ParameterizedTypeReference<>() {
                }
        );

        return teachersResponse.getBody();
    }

    public void createTeacher(String token,  TeacherDto teacherDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<TeacherDto> entity = new HttpEntity<>(teacherDto, headers);

        ResponseEntity<TeacherDto> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                entity,
                TeacherDto.class
        );
        System.out.println(response.getBody());
    }

    public void updateTeacher(String token, String teacherUsername,  TeacherDto teacherDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<TeacherDto> entity = new HttpEntity<>(teacherDto, headers);

        ResponseEntity<TeacherDto> response = restTemplate.exchange(
                BASE_URL + "/" + teacherUsername,
                HttpMethod.PUT,
                entity,
                TeacherDto.class
        );

        System.out.println(response.getBody());
    }

    public void deleteTeacher(String token, String username) {
        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL + "/" + username,
                HttpMethod.DELETE,
                prepareEntity(token),
                String.class
        );
        System.out.println(response.getBody());
    }

    public List<TeacherDto> searchTeachers(String token, String query, int limit) {
        try {
            ResponseEntity<List<TeacherDto>> response = restTemplate.exchange(
                    BASE_URL + "/find/" + query + "/" + limit,
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TeacherDto> getAllTeachers(String token) {
        try {
            ResponseEntity<List<TeacherDto>> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }
}
