package com.assignment.args;

import com.assignment.enums.ModeE;
import com.assignment.utils.Utils;

import java.util.Arrays;

public class ArgsParser {

    // Private constructor to prevent instantiation - this class provides static utility method only
    private ArgsParser() {
    }

    /**
     * Parses the input arguments based on the specified mode.
     * 
     * @param args command-line arguments
     * @param mode the operation mode determining how arguments are parsed
     * @return an ArgsStructure instance representing the parsed arguments
     */
    public static ArgsStructure parse(String[] args, ModeE mode) {
        try {
            final var argsLength = args.length;

            Utils.LOGGER.info("Parsing arguments for mode: {}", mode);
            Utils.LOGGER.debug("Raw arguments: {}", (Object) args);
            // Cast to Object to avoid varargs expansion and correctly print array content

            return switch (mode) {
                case LAST3 -> {
                    if (argsLength != 2) {
                        Utils.LOGGER.warn("Expected 2 arguments for LAST3, but got {}", argsLength);
                        Utils.printUsageAndExit();
                    }
                    // Convert country code to uppercase to maintain consistency
                    final var countryCode = args[1].toUpperCase();
                    Utils.LOGGER.info("Parsed LAST3 with country code: {}", countryCode);
                    yield new Last3Args(countryCode);
                }
                case COUNT_HOLIDAYS -> {
                    if (argsLength != 3) {
                        Utils.LOGGER.warn("Expected 3 arguments for COUNT_HOLIDAYS, but got {}", argsLength);
                        Utils.printUsageAndExit();
                    }
                    final var year = Integer.parseInt(args[1]);
                    final var codes = args[2].split(","); // Split comma-separated country codes
                    Utils.LOGGER.info("Parsed COUNT_HOLIDAYS with year: {}, countries: {}", year,
                            Arrays.toString(codes));
                    yield new CountArgs(year, codes);
                }
                case COMMON_HOLIDAYS -> {
                    if (argsLength != 4) {
                        Utils.LOGGER.warn("Expected 4 arguments for COMMON_HOLIDAYS, but got {}", argsLength);
                        Utils.printUsageAndExit();
                    }
                    final var year = Integer.parseInt(args[1]);
                    final var country1 = args[2].toUpperCase();
                    final var country2 = args[3].toUpperCase();
                    Utils.LOGGER.info("Parsed COMMON_HOLIDAYS with year: {}, countries: {} & {}", year, country1,
                            country2);
                    yield new CommonArgs(year, country1, country2);
                }
                case AVAILABLE_CODES -> {
                    if (argsLength > 2) {
                        Utils.LOGGER.warn("Expected 1 or 2 arguments for AVAILABLE_CODES, but got {}", argsLength);
                        Utils.printUsageAndExit();
                    }
                    // Optional search term argument; null if not provided
                    final var search = args.length > 1 ? args[1] : null;
                    Utils.LOGGER.info("Parsed AVAILABLE_CODES with search: '{}'", search);
                    yield new AvailableCodesArgs(search);
                }
                default -> throw new IllegalArgumentException("Unexpected mode: " + mode);
            };
        } catch (Exception e) {
            Utils.LOGGER.error("Invalid arguments. Reason: {}", e.getMessage(), e);
            Utils.printUsageAndExit();
            return null; // unreachable, but required by compiler
        }
    }
}
