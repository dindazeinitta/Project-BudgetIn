package com.budgetin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    @Value("${captcha.secret}")
    private String captchaSecret;

    @Value("${captcha.enabled:true}")
    private boolean captchaEnabled; // Default: true, bisa dimatikan via config

    public boolean verifyCaptcha(String token) {
        if (!captchaEnabled) {
            // Development mode, skip verification
            return true;
        }

        String url = "https://www.google.com/recaptcha/api/siteverify";
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", captchaSecret);
        params.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map body = response.getBody();
            return body != null && Boolean.TRUE.equals(body.get("success"));
        } catch (Exception e) {
            // Log error and return false to prevent login if verification fails due to network issues
            System.err.println("Error verifying CAPTCHA: " + e.getMessage());
            return false;
        }
    }
}
