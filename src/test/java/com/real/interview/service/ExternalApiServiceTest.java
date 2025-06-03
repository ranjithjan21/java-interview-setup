package com.real.interview.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExternalApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExternalApiService externalApiService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCallExternalApi_Success() {
        String expectedResponse = "{\"userId\": 1, \"id\": 1, \"title\": \"delectus aut autem\", \"completed\": false}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(expectedResponse);

        String result = externalApiService.callExternalApi();

        assertEquals(expectedResponse, result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    public void testCallExternalApi_FailureTriggersFallback() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("API is down"));

        String result = externalApiService.fallbackResponse(new RuntimeException("API is down"));

        assertEquals("External service is currently unavailable. Please try again later.", result);
    }
}
