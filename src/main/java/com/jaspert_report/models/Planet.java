package com.jaspert_report.models;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
public class Planet {
    @JsonProperty("name")
    private String name;

    @JsonProperty("rotation_period")
    private Integer rotation;

    @JsonProperty("orbital_period")
    private Integer orbital;

    @JsonProperty("diameter")
    private Integer diameter;

    @JsonProperty("climate")
    private String climate;

    @JsonProperty("population")
    private String population;
}
