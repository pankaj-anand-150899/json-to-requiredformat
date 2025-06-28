package com.anand.jsonhandler.controllers;

import com.anand.jsonhandler.services.InputDataParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private InputDataParser inputDataParser;

    @PostMapping(value = "/pdf/generate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getInputDataInJson(@RequestBody String jsonData){

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("Invalid Data Format... " + e.getMessage()).getBytes());
        }

        return inputDataParser.generatePdf(jsonData);
    }
}
