package dev.tuvumba.universityClient.service;

import dev.tuvumba.universityClient.dto.SubjectDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    private final RestTemplate restTemplate = new RestTemplate();

    private HttpEntity<?> prepareEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public List<SubjectDto> getTaughtSubjects(String token, String username) {
        System.out.println("Sending GET request to /teachers/" + username + "/subjects");

        ResponseEntity<List<SubjectDto>> response = null;
        try {
            response = restTemplate.exchange(
                    "http://localhost:8081/teachers/" + username + "/subjects",
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ArrayList<>();
            } else {
                throw e;
            }
        }

        return response.getBody();
    }


    public List<SubjectDto> getStudiedSubjects(String token, String username) {
        System.out.println("Sending request to /students/" + username + "/subjects");

        ResponseEntity<List<SubjectDto>> response = null;
        try {
            response = restTemplate.exchange(
                    "http://localhost:8081/students/" + username + "/subjects",
                    HttpMethod.GET,
                    prepareEntity(token),
                    new ParameterizedTypeReference<>() {}
            );
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new ArrayList<>();
            } else {
                throw e;
            }
        }
        return response.getBody();
    }

    public List<SubjectDto> getAllSubjects(String token) {
        System.out.println("Sending GET request to /subjects");

        ResponseEntity<List<SubjectDto>> response = restTemplate.exchange(
                "http://localhost:8081/subjects",
                HttpMethod.GET,
                prepareEntity(token),
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public String getBestTeacher(String token)
    {
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8081/teachers/leaderboard/best",
                HttpMethod.GET,
                prepareEntity(token),
                String.class
        );
        return response.getBody();
    }


}


