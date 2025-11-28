package com.foodopia.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class ExternalBackendsConfig {

    @Value("${backend.notification.base-url}")
    private String notificationBackendBaseURL;

    public ExternalBackendsConfig() {
    }

}