package com.hop.drivesharing.hopapplication.service;

import com.hop.drivesharing.hopapplication.config.ExternalBackendsConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final ExternalBackendsConfig externalBackendsConfig;
    public NotificationService(ExternalBackendsConfig externalBackendsConfig) {
        this.externalBackendsConfig = externalBackendsConfig;
    }

    public void sendContactNotification(String sendersEmail, String receiversEmail) {
        RestTemplate restTemplate = new RestTemplate();
        String url = externalBackendsConfig.getNotificationBackendBaseURL() + "/rest/v1/contact-notification";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("sender", sendersEmail);
        body.put("receiver", receiversEmail);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
}
