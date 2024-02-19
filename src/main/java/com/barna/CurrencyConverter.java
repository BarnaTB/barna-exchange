package com.barna;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private static final String OPEN_EXCHANGE_API_URL = "https://openexchangerates.org/api/";
    private static final String EXCHANGERATE_API_URL = "https://v6.exchangerate-api.com/v6/";

    private final String appKey;

    public CurrencyConverter(String appKey) {
        this.appKey = appKey;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Barna's Forex!");
//        Beans
//        Interfaces 
//        @ Annotation
    }

    public static Map<String, String> getAllCurrencies() {
        /** Method to return all the available currencies got exchange
         */
        String apiUrl = OPEN_EXCHANGE_API_URL + "currencies.json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode currenciesNode = objectMapper.readTree(response.body());

            // Convert JSON to Map
            Map<String, String> currenciesMap = new HashMap<>();
            currenciesNode.fields().forEachRemaining(entry -> currenciesMap.put(entry.getKey(), entry.getValue().asText()));

            return currenciesMap;
        } catch (IOException | InterruptedException e) {
            // Handle exceptions (e.g., log or throw custom exception)
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> convertCurrency(String from, String to, double amount) {
        /**
         * Method to convert an amount of money from one currency to another
         * @param from this is the base currency for the amount that will be converted
         * @param from this is the base currency for the amount that will be converted
         * @param amount this is the amount to be converted
         * @return a map containing the converted amount of money and more details about
         * the conversion including the rate used and when it was last updated
         */
        // Building the API URL for currency conversion
        String apiUrl = EXCHANGERATE_API_URL + this.appKey + "/pair/";

        // Adding the app_id as a query parameter
        apiUrl += from + "/" + to + "/" + amount;

        // Creating an HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Creating an HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            // Sending the request and receiving the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(response.body());

            // Convert JSON to Map
            Map<String, String> responseMap = new HashMap<>();
            responseNode.fields().forEachRemaining(entry -> responseMap.put(entry.getKey(), entry.getValue().asText()));

            return responseMap;
        } catch (Exception e) {
            // Handle exceptions (e.g., log or throw custom exception)
            e.printStackTrace();
            return null; // Indicates an error; adjust as needed
        }
    }
}
