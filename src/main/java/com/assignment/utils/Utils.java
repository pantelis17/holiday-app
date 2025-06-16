package com.assignment.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    public static final Logger LOGGER = LoggerFactory.getLogger("default");

    private Utils() {
    }

    public static void printUsageAndExit() {
        LOGGER.info("Usage:");
        LOGGER.info("  last3 <countryCode>");
        LOGGER.info("  countHolidays <year> <countryCode1,countryCode2,...>");
        LOGGER.info("  commonHolidays <year> <countryCode1> <countryCode2>");
        System.exit(1);
    }
}
