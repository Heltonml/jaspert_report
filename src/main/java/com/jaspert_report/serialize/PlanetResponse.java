package com.jaspert_report.serialize;

import com.jaspert_report.models.Planet;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PlanetResponse {
    @JsonProperty("results")
    private List<Planet> results;

    public List<Planet> getResults() {
        return results;
    }
}
