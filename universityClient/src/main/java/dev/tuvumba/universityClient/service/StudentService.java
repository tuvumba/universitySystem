package dev.tuvumba.universityClient.service;

import dev.tuvumba.universityClient.dto.StudentDto;
import dev.tuvumba.universityClient.dto.SubjectDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StudentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8081/students";

    private HttpEntity<?> prepareEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public StudentDto getStudent(String token, String username) {
        try {
            ResponseEntity<StudentDto> response = restTemplate.exchange(
                    BASE_URL + "/" + username,
                    HttpMethod.GET,
                    prepareEntity(token),
                    StudentDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SubjectDto> getSubjectsForStudent(String token, String username) {
        StudentDto student = getStudent(token, username);
        if (student == null) {
            return null;
        }
        ResponseEntity<List<SubjectDto>> response = restTemplate.exchange(
                BASE_URL + "/" + student.getUsername() + "/subjects",
                HttpMethod.GET,
                prepareEntity(token),
                new ParameterizedTypeReference<>() {
                }
        );

        return response.getBody();
    }

    public void createStudent(String token, StudentDto studentDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<StudentDto> entity = new HttpEntity<>(studentDto, headers);

        restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                entity,
                StudentDto.class
        );
    }


    public void updateStudent(String token, String studentUsername, StudentDto studentDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<StudentDto> entity = new HttpEntity<>(studentDto, headers);

        restTemplate.exchange(
                BASE_URL + "/" + studentUsername,
                HttpMethod.PUT,
                entity,
                StudentDto.class
        );
    }

    public void deleteStudent(String token, String username) {
        restTemplate.exchange(
                BASE_URL + "/" + username,
                HttpMethod.DELETE,
                prepareEntity(token),
                String.class
        );
    }

    public List<StudentDto> searchStudents(String token, String query, int limit) {
        try {
            ResponseEntity<List<StudentDto>> response = restTemplate.exchange(
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

    public List<StudentDto> getAllStudents(String token) {
        try {
            ResponseEntity<List<StudentDto>> response = restTemplate.exchange(
                    BASE_URL,
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            return null;
        }
    }
}
