package com.jaspert_report.serialize;

import com.jaspert_report.models.Starship;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StarshipResponse {
    @JsonProperty("results")
    private List<Starship> results;

    public List<Starship> serializeCosts(List<Starship> results) {
        for (Starship starship : results) {
            System.out.println(starship.getName());
            if (starship.getCost_in_credits().equals("unknown")) {
                starship.setCost_in_credits("0");
            }
        }
        return results;
    }

    public List<Starship> getResults() {
        List<Starship> starships = serializeCosts(results);
        return starships;
        // return results;
    }
}
