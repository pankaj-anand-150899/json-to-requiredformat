package com.anand.jsonhandler.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.itextpdf.layout.Document;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Map;

@Service
public class InputDataParserImpl implements InputDataParser{

    @Override
    public ResponseEntity<byte[]> generatePdf(String jsonData) {
        try {
            // Convert JSON string to Jackson JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonData);

            // Create PDF in memory
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add content to PDF
            document.add(new Paragraph("JSON Data"));
            document.add(new Paragraph("-------------------------"));

            if (jsonNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    document.add(new Paragraph(entry.getKey() + " : " + entry.getValue().toString()));
                }
            } else {
                document.add(new Paragraph(jsonNode.toPrettyString()));
            }

            document.close();

            // Return PDF as a downloadable file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=json-data.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }
}
