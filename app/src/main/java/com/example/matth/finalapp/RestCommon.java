package com.example.matth.finalapp;

import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by michael on 24/10/2016.
 */

public class RestCommon {



    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private String baseUrl;


    public RestCommon(String token){
        initializeRest(token);
    }

    public void initializeRest(String token){
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        headers.add("Authorization", token);
        baseUrl = "http://10.0.2.2:8080";
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
