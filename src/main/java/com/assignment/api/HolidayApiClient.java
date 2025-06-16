package com.assignment.api;

import com.assignment.model.Holiday;
import com.assignment.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HolidayApiClient {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HolidayApiClient() {
    }

    public static List<Holiday> fetchHolidays(int year, String countryCode) throws IOException, InterruptedException {
        final var url = String.format("https://date.nager.at/api/v3/PublicHolidays/%d/%s", year, countryCode);
        final var request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        final var response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            Utils.LOGGER.error("Failed to fetch holidays for {} - {}. HTTP {}", countryCode, year,
                    response.statusCode());
            return Collections.emptyList();
        }

        return Arrays.asList(MAPPER.readValue(response.body(), Holiday[].class));
    }
}
