package com.assignment.args;

import com.assignment.enums.ModeE;
import com.assignment.utils.Utils;

import java.util.Arrays;

public class ArgsParser {

    private ArgsParser() {
    }

    public static ArgsStructure parse(String[] args, ModeE mode) {
        try {
            final var argsLength = args.length;
            Utils.LOGGER.info("Parsing arguments for mode: {}", mode);
            Utils.LOGGER.debug("Raw arguments: {}", args);

            return switch (mode) {
                case LAST3 -> {
                    if (argsLength != 2) {
                        Utils.LOGGER.warn("Expected 2 arguments for LAST3, but got {}", argsLength);
                        Utils.printUsageAndExit();
                    }
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
                    final var codes = args[2].split(",");
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
            };
        } catch (Exception e) {
            Utils.LOGGER.error("Invalid arguments. Reason: {}", e.getMessage(), e);
            Utils.printUsageAndExit();
            return null; // unreachable, but required by compiler
        }
    }
}
