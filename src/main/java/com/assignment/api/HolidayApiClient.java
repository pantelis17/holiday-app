package com.assignment.api;

import com.assignment.model.Country;
import com.assignment.model.Holiday;
import com.assignment.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HolidayApiClient {
    // Shared HttpClient instance for all requests - efficient and thread-safe
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    // Jackson ObjectMapper for JSON serialization/deserialization
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Private constructor to prevent instantiation since all methods are static utility methods
    private HolidayApiClient() {
    }

    /**
     * Fetches the list of public holidays for a given year and country code.
     * 
     * @param year        the year to query holidays for
     * @param countryCode the country code (e.g., "US")
     * @return list of Holiday objects, empty list if fetch fails
     * @throws IOException          if an I/O error occurs when sending or receiving
     * @throws InterruptedException if the operation is interrupted
     */
    public static List<Holiday> fetchHolidays(int year, String countryCode) throws IOException, InterruptedException {
        final var url = String.format("https://date.nager.at/api/v3/PublicHolidays/%d/%s", year, countryCode);
        Utils.LOGGER.info("Sending request to date.nager for code '{}' and year '{}'", countryCode, year);
        final var request = HttpRequest.newBuilder(URI.create(url)).GET().timeout(Duration.ofSeconds(10)).build();
        // Send the HTTP request synchronously
        final var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        Utils.LOGGER.info("Received response from date.nager for code '{}' and year '{}'", countryCode, year);

        // Handle non-200 HTTP responses gracefully
        if (response.statusCode() != 200) {
            Utils.LOGGER.error("Failed to fetch holidays for {} - {}. HTTP {}", countryCode, year,
                    response.statusCode());
            return Collections.emptyList();
        }

        // Deserialize JSON array to List<Holiday>
        return Arrays.asList(MAPPER.readValue(response.body(), Holiday[].class));
    }

    /**
     * Fetches the list of available countries for which holidays can be queried.
     * 
     * @return list of Country objects, or empty list if fetch fails
     * @throws IOException          if an I/O error occurs when sending or receiving
     * @throws InterruptedException if the operation is interrupted
     */
    public static List<Country> fetchAvailableCountries() throws IOException, InterruptedException {
        final var url = "https://date.nager.at/api/v3/AvailableCountries";
        Utils.LOGGER.info("Sending request to date.nager to receive the available country codes");
        final var request = HttpRequest.newBuilder(URI.create(url)).GET().timeout(Duration.ofSeconds(10)).build();
        // Send the HTTP request synchronously
        final var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        Utils.LOGGER.info("Received response from date.nager");

        // Handle non-200 HTTP responses gracefully
        if (response.statusCode() != 200) {
            Utils.LOGGER.error("Failed to fetch available country codes. HTTP {}", response.statusCode());
            return Collections.emptyList();
        }

        // Deserialize JSON array to List<Country>
        return Arrays.asList(MAPPER.readValue(response.body(), Country[].class));
    }
}
