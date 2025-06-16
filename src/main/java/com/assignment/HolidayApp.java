package com.assignment;

import com.assignment.args.*;
import com.assignment.enums.ModeE;
import com.assignment.service.HolidayService;
import com.assignment.utils.Utils;

public class HolidayApp {

    public static void main(String[] args) throws Exception {
        Utils.LOGGER.info("Application started with arguments: {}", (Object) args);

        if (args.length < 1) {
            Utils.LOGGER.warn("No arguments provided.");
            Utils.printUsageAndExit();
        }

        final var holidayService = new HolidayService();
        final ModeE mode;

        try {
            mode = ModeE.fromString(args[0]);
            Utils.LOGGER.info("Mode detected: {}", mode);
        } catch (IllegalArgumentException e) {
            Utils.LOGGER.error("Invalid mode provided: {}. Error: {}", args[0], e.getMessage());
            Utils.printUsageAndExit();
            return;
        }

        final var parsedArgs = ArgsParser.parse(args, mode);
        Utils.LOGGER.debug("Parsed args structure: {}", parsedArgs);

        switch (mode) {
            case LAST3 -> {
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
        }

        Utils.LOGGER.info("Application execution completed.");
    }
}
