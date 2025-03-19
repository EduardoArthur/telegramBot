package com.example.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CarteiraService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestApiService restApiService;

    public String getCarteira() {

        ResponseEntity<String> response = restApiService.sendGetRequest("/api/carteira/consultar", String.class);

        return response.getBody();

    }



}
