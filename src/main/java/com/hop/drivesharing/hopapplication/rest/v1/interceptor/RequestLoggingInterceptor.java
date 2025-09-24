package com.hop.drivesharing.hopapplication.rest.v1.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.logging.Logger;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(RequestLoggingInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        logger.info("Incoming request: " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }
}