package com.assignment.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    public static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public static void printUsageAndExit() {
        LOGGER.info("Usage:");
        LOGGER.info("  last3 <countryCode>");
        LOGGER.info("  count_Holidays <year> <countryCode1,countryCode2,...>");
        LOGGER.info("  common_Holidays <year> <countryCode1> <countryCode2>");
        LOGGER.info("  available_codes <searchString>");
        LOGGER.info("  help");
        System.exit(1);
    }
}
