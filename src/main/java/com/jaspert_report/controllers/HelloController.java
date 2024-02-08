package com.jaspert_report.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.jaspert_report.models.Starship;
import com.jaspert_report.services.StarshipService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping(path="/jreport")
public class MainController {
    
    @Autowired 
    private StarshipService starshipService;

    public List<Starship> getStarships() {
        List<Starship> starships = starshipService.getStarshipsFromAPI();
        return starships;
    }

    @GetMapping("/")
    public @ResponseBody String getHello(){
        List<Starship> starships = getStarships();

        for (Starship element : starships) {
            System.out.println(element.getName());
        }
        
        return "index";
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportReport() {
        try {
            JasperPrint jasperPrint = starshipService.handleJReport();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            byte[] reportBytes = byteArrayOutputStream.toByteArray();

            return ResponseEntity.ok()
                    .contentLength(reportBytes.length)
                    .body(reportBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
