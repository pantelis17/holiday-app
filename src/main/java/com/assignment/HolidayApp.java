package com.assignment;

import com.assignment.args.*;
import com.assignment.enums.ModeE;
import com.assignment.service.HolidayService;
import com.assignment.utils.Utils;

public class HolidayApp {

    public static void main(String[] args) throws Exception {
        Utils.LOGGER.info("Application started with arguments: {}", (Object) args);

        // Check if any arguments were provided
        if (args.length < 1) {
            Utils.LOGGER.warn("No arguments provided.");
            Utils.printUsageAndExit(); // Exits after printing usage
        }

        final var holidayService = new HolidayService();
        final ModeE mode;

        try {
            // Parse the first argument as the mode
            mode = ModeE.fromString(args[0]);
            Utils.LOGGER.info("Mode detected: {}", mode);
        } catch (IllegalArgumentException e) {
            // Invalid mode name supplied
            Utils.LOGGER.error("Invalid mode provided: {}. Error: {}", args[0], e.getMessage());
            Utils.printUsageAndExit();
            return; // Return technically unnecessary, but makes intent clear
        }

        // Handle the HELP mode by printing usage and exiting
        if (mode.equals(ModeE.HELP)) {
            Utils.printUsageAndExit();
            return;
        }

        // Delegate argument parsing to ArgsParser based on detected mode
        final var parsedArgs = ArgsParser.parse(args, mode);
        Utils.LOGGER.debug("Parsed args structure: {}", parsedArgs);

        // Dispatch execution based on parsed mode
        switch (mode) {
            case LAST3 -> {
                // Safe cast: we know parse() returned Last3Args in this case
                final var last3Args = (Last3Args) parsedArgs;
                Utils.LOGGER.info("Executing LAST3 for country: {}", last3Args.countryCode());
                holidayService.last3Holidays(last3Args.countryCode());
            }
            case COUNT_HOLIDAYS -> {
                final var countArgs = (CountArgs) parsedArgs;
                Utils.LOGGER.info("Executing COUNT_HOLIDAYS for year {} and countries: {}",
                        countArgs.year(), countArgs.countryCodes());
                holidayService.countHolidaysNotOnWeekends(countArgs.year(), countArgs.countryCodes());
            }
            case COMMON_HOLIDAYS -> {
                final var commonArgs = (CommonArgs) parsedArgs;
                Utils.LOGGER.info("Executing COMMON_HOLIDAYS for year {} and countries: {} & {}",
                        commonArgs.year(), commonArgs.countryCode1(), commonArgs.countryCode2());
                holidayService.commonHolidays(commonArgs.year(), commonArgs.countryCode1(), commonArgs.countryCode2());
            }
            case AVAILABLE_CODES -> {
                final var availableCodesArgs = (AvailableCodesArgs) parsedArgs;
                Utils.LOGGER.info("Executing AVAILABLE_CODES{}",
                        availableCodesArgs.search() != null
                                ? String.format(" for search: %s", availableCodesArgs.search())
                                : "");
                holidayService.printAvailableCountries(availableCodesArgs.search());
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + mode);
        }

        Utils.LOGGER.info("Application execution completed.");
    }
}
