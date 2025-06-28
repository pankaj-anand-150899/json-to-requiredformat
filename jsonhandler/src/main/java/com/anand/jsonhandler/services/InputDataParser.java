package com.anand.jsonhandler.services;

import org.springframework.http.ResponseEntity;

public interface InputDataParser {

    public ResponseEntity<byte[]> generatePdf(String jsonData);
}
