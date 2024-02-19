package com.barna;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrencyConverterTest {

    private static final String TEST_APP_ID = "422d850b06b6911da1dc89d4";
    private static final String MOCK_API_RESPONSE = "{\"USD\":\"United States Dollar\",\"EUR\":\"Euro\"}";

    private CurrencyConverter currencyConverter;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        mockHttpClient = mock(HttpClient.class);
        currencyConverter = new CurrencyConverter(TEST_APP_ID);
        currencyConverter.httpClient = mockHttpClient;
    }

    @Test
    void testGetAllCurrencies() throws Exception {
        // Mocking HTTP response
        when(mockHttpClient.send(mockRequest(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(HttpResponse.BodyHandlers.ofString().apply(mockHttpResponse(MOCK_API_RESPONSE)));

        Map<String, String> result = currencyConverter.getAllCurrencies();

        // Expected result based on the mocked API response
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put("USD", "United States Dollar");
        expectedResult.put("EUR", "Euro");

        assertEquals(expectedResult, result);
    }

    @Test
    void testConvertCurrency() throws Exception {
        // Mocking HTTP response
        when(mockHttpClient.send(mockRequest(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(HttpResponse.BodyHandlers.ofString().apply(mockHttpResponse("123.45")));

        double result = currencyConverter.convertCurrency("USD", "EUR", 100.0);

        // Expected result based on the mocked API response
        double expectedResult = 123.45;

        assertEquals(expectedResult, result);
    }

    private HttpRequest mockRequest() {
        return HttpRequest.newBuilder().uri(URI.create("https://openexchangerates.org/api/"))
                .build();
    }

    private HttpResponse<String> mockHttpResponse(String body) {
        return HttpResponse.BodyHandlers.ofString().apply(200, body);
    }
}
