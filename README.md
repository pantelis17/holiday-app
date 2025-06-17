# HolidayApp

A Java CLI application that fetches and analyzes public holidays using the [Nager.Date Public Holiday API](https://date.nager.at/). Built with Maven.

---

## Requirements

- Java 21
- Maven
- Internet connection (API requests made to https://date.nager.at)

---

## Build Instructions

To build the project using Maven, run:

```bash
mvn clean package
```

This will generate a JAR file in the `target/` directory, for example:

```bash
target/holidayapp-1.0-SNAPSHOT.jar
```

---

## How to Run

Run the application using the following command:

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp <mode> <arguments>
```

Replace `<mode>` and `<arguments>` with the appropriate values described below.

---

## Supported Modes

| Mode              | Command                        | Arguments                                                  | Description                                                                 |
|-------------------|--------------------------------|-------------------------------------------------------------|-----------------------------------------------------------------------------|
| last3             | last3 <countryCode>            | 1: Country code (e.g. US, DE)                               | Displays the last 3 public holidays before today in the given country.     |
| count_Holidays    | count_Holidays <year> <codes>  | 1: Year <br> 2: Comma-separated country codes   | Counts holidays in the given year that do not fall on weekends.            |
| common_Holidays   | common_Holidays <year> <c1> <c2>| 1: Year <br> 2: Country 1 <br> 3: Country 2                 | Lists common holiday dates between the two countries for the given year.   |
| available_codes   | available_codes [searchTerm]   | Optional: Partial country name (e.g. "uni" for "United")    | Lists supported country codes, filtered by search term if provided.        |
| help              | help                           | None                                                        | Displays usage information.                                                |

---

## Examples

### Example 1: Show last 3 holidays in the US

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp last3 US
```

### Example 2: Count weekday holidays in 2024 for US, FR, and DE

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp count_Holidays 2024 US,FR,DE
```

### Example 3: Show common holidays for Germany and France in 2023

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp common_Holidays 2023 DE FR
```

### Example 4: List all available countries

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp available_codes
```

### Example 5: Search for countries containing "king"

```bash
java -cp target/holidayapp-1.0-SNAPSHOT.jar com.assignment.HolidayApp available_codes king
```
