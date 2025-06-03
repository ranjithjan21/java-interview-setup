package com.real.interview.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private static final String EXTERNAL_API_URL = "https://jsonplaceholder.typicode.com/todos/1"; // Example public API

    @Autowired
    private RestTemplate restTemplate;

    @RateLimiter(name = "externalApiRateLimiter")
    @CircuitBreaker(name = "externalApiCircuitBreaker", fallbackMethod = "fallbackResponse")
    public String callExternalApi() {
        return restTemplate.getForObject(EXTERNAL_API_URL, String.class);
    }

    // Fallback method must match the signature of the original method plus a Throwable at the end
    public String fallbackResponse(Throwable t) {
        return "External service is currently unavailable. Please try again later.";
    }
}

