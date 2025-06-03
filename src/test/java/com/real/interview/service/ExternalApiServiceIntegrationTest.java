package com.real.interview.service;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ExternalApiServiceIntegrationTest {

    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private RateLimiterRegistry rateLimiterRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private RestTemplate restTemplate;

    private RateLimiter rateLimiter;
    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setUp() {
        rateLimiter = rateLimiterRegistry.rateLimiter("externalApiRateLimiter");
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("externalApiCircuitBreaker");
        reset(restTemplate);
    }

    @Test
    void testRateLimiterBlocksAfterLimit() {
        // Mock 5 allowed calls to return a value, 6th to throw exception (simulate rate limit)
        when(restTemplate.getForObject(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(String.class)))
            .thenReturn("success", "success", "success", "success", "success")
            .thenThrow(new RuntimeException("Rate limit exceeded"));
        for (int i = 0; i < 5; i++) {
            String response = externalApiService.callExternalApi();
            assertThat(response).isNotNull();
        }
        // 6th call should trigger fallback due to rate limit
        String blockedResponse = externalApiService.callExternalApi();
        assertEquals("External service is currently unavailable. Please try again later.", blockedResponse);
    }

    @Test
    void testCircuitBreakerOpensOnFailures() throws InterruptedException {
        // Simulate 10 failures out of 10 (failure rate threshold is 50%)
        when(restTemplate.getForObject(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(String.class)))
            .thenThrow(new RuntimeException("API is down"));
        // Fill the sliding window (10 calls)
        for (int i = 0; i < 10; i++) {
            externalApiService.callExternalApi();
        }
        // Trigger state transition by making one more call
        String response = externalApiService.callExternalApi();
        assertEquals("External service is currently unavailable. Please try again later.", response);
        // Circuit breaker should be open now
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    @Test
    void testCircuitBreakerHalfOpenAndClosedStates() throws InterruptedException {
        // Simulate failures to open the circuit breaker
        when(restTemplate.getForObject(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(String.class)))
                .thenThrow(new RuntimeException("API is down"));
        for (int i = 0; i < 6; i++) {
            externalApiService.callExternalApi();
        }
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
        // Wait for open state to transition to half-open (wait-duration-in-open-state=10s)
        Thread.sleep(Duration.ofSeconds(11).toMillis());
        // Trigger state transition by making a call
        try {
            externalApiService.callExternalApi();
        } catch (Exception ignored) {}
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());
        // Now simulate successful calls in half-open state
        reset(restTemplate);
        when(restTemplate.getForObject(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(String.class)))
                .thenReturn("success");
        // 3 permitted calls in half-open state
        for (int i = 0; i < 3; i++) {
            String response = externalApiService.callExternalApi();
            assertThat(response).isNotNull();
        }
        // After successful calls, circuit breaker should close
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }
}

