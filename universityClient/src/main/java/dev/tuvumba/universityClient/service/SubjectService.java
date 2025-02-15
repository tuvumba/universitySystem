package dev.tuvumba.universityClient.service;

import dev.tuvumba.universityClient.dto.StudentDto;
import dev.tuvumba.universityClient.dto.SubjectDto;
import dev.tuvumba.universityClient.dto.TeacherDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class SubjectService {

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<?> prepareEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    private SubjectDto getSubject(String token, Long id) {
        try {
            ResponseEntity<SubjectDto> response = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + id,
                    HttpMethod.GET,
                    prepareEntity(token),
                    SubjectDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public SubjectDto getSubject(String token, String subjectName) {
        try {
            ResponseEntity<SubjectDto> response = restTemplate.exchange(
                    "http://localhost:8081/subjects/find/" + subjectName,
                    HttpMethod.GET,
                    prepareEntity(token),
                    SubjectDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<SubjectDto> searchSubjects(String token, String name, int limit) {
        try {
            ResponseEntity<List<SubjectDto>> response = restTemplate.exchange(
                    "http://localhost:8081/subjects/find/" + name + "/" + limit,
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TeacherDto> getTeachers(String token, String subjectName) {

        SubjectDto subject = getSubject(token, subjectName);
        if (subject == null) {
            return null;
        }
        ResponseEntity<List<TeacherDto>> teachersResponse = restTemplate.exchange(
                "http://localhost:8081/subjects/" + subject.getId() + "/teachers",
                HttpMethod.GET,
                prepareEntity(token),
                new ParameterizedTypeReference<>() {
                }
        );

        return teachersResponse.getBody();

    }

    public List<StudentDto> getStudents(String token, String subjectName) {
        try {
            ResponseEntity<SubjectDto> subjectResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/find/" + subjectName,
                    HttpMethod.GET,
                    prepareEntity(token),
                    SubjectDto.class
            );

            SubjectDto subject = subjectResponse.getBody();
            if (subject == null) {
                return null;
            }
            ResponseEntity<List<StudentDto>> studentsResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + subject.getId() + "/students",
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {
                    }
            );

            return studentsResponse.getBody();
        } catch (Exception e) {
            System.out.println("Error fetching students: " + e.getMessage());
            return null;
        }
    }

    public void startStudying(String token, String username, Long id) {
        try {
            SubjectDto subject = getSubject(token, id);
            ResponseEntity<String> studyResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + subject.getId() + "/students/" + username,
                    HttpMethod.POST,
                    prepareEntity(token),
                    String.class
            );
            System.out.println(studyResponse.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopStudying(String token, String username, Long id) {
        try {
            SubjectDto subject = getSubject(token, id);
            ResponseEntity<String> studyResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + subject.getId() + "/students/" + username,
                    HttpMethod.DELETE,
                    prepareEntity(token),
                    String.class
            );
            System.out.println(studyResponse.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void startTeaching(String token, String username, Long id ) {
        try {
            SubjectDto subject = getSubject(token, id);
            ResponseEntity<String> studyResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + subject.getId() + "/teachers/" + username,
                    HttpMethod.POST,
                    prepareEntity(token),
                    String.class
            );
            System.out.println(studyResponse.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopTeaching(String token, String username, Long id ) {
        try {
            SubjectDto subject = getSubject(token, id);
            ResponseEntity<String> studyResponse = restTemplate.exchange(
                    "http://localhost:8081/subjects/" + subject.getId() + "/teachers/" + username,
                    HttpMethod.DELETE,
                    prepareEntity(token),
                    String.class
            );
            System.out.println(studyResponse.getBody());
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSubject(String token, SubjectDto subjectDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<SubjectDto> entity = new HttpEntity<>(subjectDto, headers);

        ResponseEntity<SubjectDto> response = restTemplate.exchange(
                "http://localhost:8081/subjects",
                HttpMethod.POST,
                entity,
                SubjectDto.class
        );

        System.out.println(response.getBody());
    }

    public void updateSubject(String token, Long subjectId, SubjectDto subjectDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<SubjectDto> entity = new HttpEntity<>(subjectDto, headers);

        ResponseEntity<SubjectDto> response = restTemplate.exchange(
                "http://localhost:8081/subjects/" + subjectId,
                HttpMethod.PUT,
                entity,
                SubjectDto.class
        );

        System.out.println(response.getBody());
    }

    public void deleteSubject(String token, Long subjectId) {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081/subjects/" + subjectId,
                HttpMethod.DELETE,
                prepareEntity(token),
                String.class
        );
        System.out.println(response.getBody());
    }

    public List<SubjectDto> getAllSubjects(String token) {
        try {
            ResponseEntity<List<SubjectDto>> response = restTemplate.exchange(
                    "http://localhost:8081/subjects",
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
