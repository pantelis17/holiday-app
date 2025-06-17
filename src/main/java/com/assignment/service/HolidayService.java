package com.assignment.service;

import com.assignment.api.HolidayApiClient;
import com.assignment.model.Country;
import com.assignment.model.Holiday;
import com.assignment.utils.Utils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class HolidayService {

    /**
     * Fetches the last 3 holidays up to today for the given country code,
     * considering holidays from the current and previous year.
     * Logs the result or a message if no holidays are found.
     *
     * @param countryCode the country code (e.g., "US")
     * @throws IOException
     * @throws InterruptedException
     */
    public void last3Holidays(String countryCode) throws IOException, InterruptedException {
        final var today = LocalDate.now();
        final var currentYear = today.getYear();

        // Fetch holidays for current year and previous year to cover recent holidays before today
        final var allHolidays = new ArrayList<Holiday>();
        allHolidays.addAll(HolidayApiClient.fetchHolidays(currentYear - 1, countryCode));
        allHolidays.addAll(HolidayApiClient.fetchHolidays(currentYear, countryCode));

        final var last3 = allHolidays.reversed().stream()
                .filter(h -> !h.getLocalDate().isAfter(today)) // Only holidays on or before today
                .limit(3)
                .map(h -> String.format("%s : %s", h.getDate(), h.getName()))
                .toList();

        if (last3.isEmpty()) {
            Utils.LOGGER.info(
                    "No holidays found before today for '{}'. Ensure that the provided country code is correct",
                    countryCode);
            return;
        }
        Utils.LOGGER.info("Last 3 holidays for {}: {}", countryCode, last3);
    }

    /**
     * Counts the number of holidays in a given year for each country code,
     * excluding holidays that fall on weekends (Saturday and Sunday).
     * Logs the counts sorted descending by number of holidays.
     *
     * @param year         the year to check
     * @param countryCodes array of country codes
     * @throws IOException
     * @throws InterruptedException
     */
    public void countHolidaysNotOnWeekends(int year, String[] countryCodes) throws IOException, InterruptedException {
        if (countryCodes == null || countryCodes.length == 0) {
            Utils.LOGGER.warn("No country codes provided. Skipping.");
            return;
        }

        final var result = new HashMap<String, Integer>();

        for (final var code : countryCodes) {
            final var holidays = HolidayApiClient.fetchHolidays(year, code);
            // Filter out holidays that fall on Saturday or Sunday
            final var count = (int) holidays.stream()
                    .map(Holiday::getLocalDate)
                    .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                    .count();
            result.put(code, count);
        }

        // Sort by holiday count descending and log the results
        result.entrySet().stream()
                .sorted(Entry.<String, Integer>comparingByValue().reversed())
                .forEach(e -> Utils.LOGGER.info("{} : {}", e.getKey(), e.getValue()));
    }

    /**
     * Finds common holidays between two countries for a given year.
     * Holidays are grouped by date, allowing multiple holidays on the same day.
     * Logs common holidays with names from both countries.
     *
     * @param year  the year to check
     * @param code1 first country code
     * @param code2 second country code
     * @throws IOException
     * @throws InterruptedException
     */
    public void commonHolidays(int year, String code1, String code2) throws IOException, InterruptedException {
        final var holidaysMapForCode1 = getHolidaysDateMap(year, code1);
        final var holidaysMapForCode2 = getHolidaysDateMap(year, code2);

        // Create a set with the holidays date of the first code and keep only the dates
        // which also exists in the second code.
        // To save space, we can perform this check during iteration of holidaysMapForCode1
        // but in this case we cannot have an early exit.
        final var commonDates = new HashSet<LocalDate>(holidaysMapForCode1.keySet());
        commonDates.retainAll(holidaysMapForCode2.keySet());

        if (commonDates.isEmpty()) {
            Utils.LOGGER.info("No common holidays found between {} and {}", code1, code2);
            return;
        }

        Utils.LOGGER.info("Common holidays for {} and {}:", code1, code2);
        // For each common date, log the holidays from both countries side-by-side
        commonDates.stream().sorted().forEach(date -> {
            final var hol1 = holidaysMapForCode1.get(date);
            final var hol2 = holidaysMapForCode2.get(date);
            Utils.LOGGER.info("{} : {} -> '{}' / {} -> '{}'", date, code1, flatHolidays(hol1), code2,
                    flatHolidays(hol2));
        });
    }

    /**
     * Prints the list of available countries, optionally filtered by a search string.
     *
     * @param search a substring to filter countries by name; if null or blank, shows all
     * @throws IOException
     * @throws InterruptedException
     */
    public void printAvailableCountries(String search) throws IOException, InterruptedException {
        final var countries = HolidayApiClient.fetchAvailableCountries();
        final var filtered = (search == null || search.isBlank())
                ? countries
                : countries.stream()
                        .filter(c -> c.name().toLowerCase().contains(search.toLowerCase()))
                        .toList();

        if (filtered.isEmpty()) {
            Utils.LOGGER.info("No countries found matching '{}'", search);
            return;
        }

        Utils.LOGGER.info("Available countries{}:",
                search != null && !search.isBlank() ? " matching '" + search + "'" : "");
        filtered.forEach(c -> Utils.LOGGER.info("{} - {}", c.countryCode(), c.name()));
    }

    /**
     * Helper method to fetch holidays grouped by date, 
     * supporting multiple holidays on the same date.
     *
     * @param year the year to fetch holidays for
     * @param code the country code
     * @return a map from LocalDate to list of holidays on that date
     * @throws IOException
     * @throws InterruptedException
     */
    private Map<LocalDate, List<Holiday>> getHolidaysDateMap(int year, String code)
            throws IOException, InterruptedException {
        return HolidayApiClient.fetchHolidays(year, code).stream()
                // There are cases where a date might have more than 1 holidays e.g. in Greece for 25 of March
                .collect(Collectors.groupingBy(Holiday::getLocalDate));
    }

    /**
     * Flattens a list of Holiday objects into a comma-separated string of their local names.
     *
     * @param holidays list of Holiday objects
     * @return comma-separated string of holiday local names
     */
    private String flatHolidays(List<Holiday> holidays) {
        return holidays.stream()
                .map(Holiday::getLocalName)
                .collect(Collectors.joining(", "));
    }
}
