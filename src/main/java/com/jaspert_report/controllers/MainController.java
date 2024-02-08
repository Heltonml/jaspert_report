package com.jaspert_report.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.jaspert_report.models.Planet;
import com.jaspert_report.services.PlanetService;

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
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@RestController
@RequestMapping(path="/report")
public class MainController {
    
    @Autowired 
    private PlanetService planetService;

    public List<Planet> getPlanets() {
        List<Planet> planets = planetService.getPlanetsFromAPI();
        return planets;
    }

    @GetMapping("/")
    public @ResponseBody String getHello(){
        List<Planet> planets = getPlanets();

        for (Planet planet : planets) {
            System.out.println(planet.getName());
        }
        
        return "index";
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportPdfReport() {
        try {
            JasperPrint jasperPrint = planetService.handleJReport();

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

    @GetMapping(value = "/xls", produces = "application/vnd.ms-excel")
    public ResponseEntity<byte[]> exportReportAsXls() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRXlsExporter exporter = new JRXlsExporter();

            JasperPrint jasperPrint = planetService.handleJReport();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
            exporter.exportReport();

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
