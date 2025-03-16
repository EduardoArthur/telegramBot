package com.example.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.urlBase}")
    private String urlBase;

    public <T> ResponseEntity<T> sendPostRequest(String api, Object request, Class<T> responseType) {

        String url = urlBase + api;

        try {
            return restTemplate.postForEntity(url, request, responseType);
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao fazer requisição POST para " + url, e);
        }
    }

    public <T> ResponseEntity<T> sendGetRequest(String api, Class<T> responseType) {

        String url = urlBase + api;

        try {
            return restTemplate.getForEntity(url, responseType);
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao fazer requisição GET para " + url, e);
        }
    }

}
