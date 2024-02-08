package com.jaspert_report.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


import com.jaspert_report.models.Starship;
import com.jaspert_report.serialize.StarshipResponse;

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
public class StarshipService {
    
    private final RestTemplate restTemplate;

    @Autowired
    public StarshipService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Starship> getStarshipsFromAPI() {
        String apiUrl = "https://swapi.dev/api/starships";
        List<Starship> starships = new ArrayList<>();
        try {
            StarshipResponse response = restTemplate.getForObject(apiUrl, StarshipResponse.class);
            starships = response.getResults();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return starships;
    }

    public JasperPrint handleJReport() throws Exception {
        List<Starship> starships = getStarshipsFromAPI();

        File file = ResourceUtils.getFile("classpath:sw_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(starships);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Report");
        parameters.put("createdBy", "Helton M Leite");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return jasperPrint;
    }
}
