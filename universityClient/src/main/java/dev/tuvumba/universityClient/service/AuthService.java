package dev.tuvumba.universityClient.service;

import dev.tuvumba.universityClient.dto.AuthResponseDto;
import dev.tuvumba.universityClient.dto.UserLoginDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String AUTH_URL = "http://localhost:8081/auth/login";

    public List<String> roles;
    public String token;

    public void authenticate(UserLoginDto loginDTO) {
        this.token = null;
        this.roles = null;

        HttpEntity<UserLoginDto> request = new HttpEntity<>(loginDTO);
        AuthResponseDto authDto = restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, AuthResponseDto.class).getBody();
        this.token = authDto.getToken();
        this.roles = authDto.getRoles();
    }
}

