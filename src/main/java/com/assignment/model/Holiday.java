package com.assignment.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Holiday {
    public String date; // yyyy-MM-dd
    public String localName;
    public String name;
    public String countryCode;
    public boolean fixed;
    public boolean global;
    public List<String> counties;
    public int launchYear;
    public String type;

    public LocalDate getLocalDate() {
        return LocalDate.parse(date);
    }
}
