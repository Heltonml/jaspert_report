package com.jaspert_report.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


import com.jaspert_report.models.Planet;
import com.jaspert_report.serialize.PlanetResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class PlanetService {
    
    private final RestTemplate restTemplate;

    @Autowired
    public PlanetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Planet> getPlanetsFromAPI() {
        String apiUrl = "https://swapi.dev/api/planets";
        List<Planet> planets = new ArrayList<>();
        try {
            PlanetResponse response = restTemplate.getForObject(apiUrl, PlanetResponse.class);
            planets = response.getResults();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return planets;
    }

    public JasperPrint handleJReport() throws Exception {
        List<Planet> planets = getPlanetsFromAPI();

        File file = ResourceUtils.getFile("classpath:sw_planets.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(planets);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Planets Report");
        parameters.put("createdBy", "Helton");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return jasperPrint;
    }
}
