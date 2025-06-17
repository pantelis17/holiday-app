package com.assignment.service;

import com.assignment.api.HolidayApiClient;
import com.assignment.model.Holiday;
import com.assignment.utils.Utils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HolidayService {

    public void last3Holidays(String countryCode) throws IOException, InterruptedException {
        final var today = LocalDate.now();
        final var currentYear = today.getYear();
        final var allHolidays = new ArrayList<Holiday>();
        allHolidays.addAll(HolidayApiClient.fetchHolidays(currentYear - 1, countryCode));
        allHolidays.addAll(HolidayApiClient.fetchHolidays(currentYear, countryCode));

        final var last3 = allHolidays.reversed().stream()
                .filter(h -> !h.getLocalDate().isAfter(today))
                .limit(3)
                .map(h -> String.format("%s : %s", h.date, h.name))
                .toList();

        if (last3.isEmpty()) {
            Utils.LOGGER.info("No holidays found before today for {}", countryCode);
            return;
        }
        Utils.LOGGER.info("Last 3 holidays for {}: {}", countryCode, last3);
    }

    public void countHolidaysNotOnWeekends(int year, String[] countryCodes) throws IOException, InterruptedException {
        if (countryCodes == null || countryCodes.length == 0) {
            Utils.LOGGER.info("Could not locate country codes. Exit without results");
            return;
        }

        final var result = new HashMap<String, Integer>();

        for (final var code : countryCodes) {
            final var holidays = HolidayApiClient.fetchHolidays(year, code);
            final var count = (int) holidays.stream()
                    .map(Holiday::getLocalDate)
                    .filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
                    .count();
            result.put(code, count);
        }

        result.entrySet().stream()
                .sorted((x, y) -> x.getValue().compareTo(y.getValue()))
                .forEach(e -> Utils.LOGGER.info("{} : {}", e.getKey(), e.getValue()));
    }

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
            Utils.LOGGER.info("No common holidays found.");
            return;
        }
        Utils.LOGGER.info("Common holidays:");
        commonDates.stream().sorted().forEach(date -> {
            final var hol1 = holidaysMapForCode1.get(date);
            final var hol2 = holidaysMapForCode2.get(date);
            Utils.LOGGER.info("{} : {} -> '{}' / {} -> '{}'", date, code1, hol1.localName, code2, hol2.localName);
        });
    }

    private Map<LocalDate, Holiday> getHolidaysDateMap(int year, String code) throws IOException, InterruptedException {
        return HolidayApiClient.fetchHolidays(year, code).stream()
                .collect(Collectors.toMap(Holiday::getLocalDate, h -> h));
    }
}
