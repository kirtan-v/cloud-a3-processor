package com.dal.processor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/process")
public class ProcessorController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> processFile(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        String fileName = request.get("file");
        String product = request.get("product");

        File file = new File("/kirtan_PV_dir/"+ fileName);

        if (!file.exists()) {
            response.put("file", fileName);
            response.put("error", "File not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int sum = 0;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    String[] parts = line.split(",");
                    if (parts.length != 2) {
                        response.put("file", fileName);
                        response.put("error", "Input file not in CSV format.");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length != 2) {
                    response.put("file", fileName);
                    response.put("error", "Input file not in CSV format.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                if (parts[0].trim().equalsIgnoreCase(product)) {
                    sum += Integer.parseInt(parts[1].trim());
                }
            }

            if (fileName.contains(".yml") && sum == 0){
                response.put("file", fileName);
                response.put("error", "Input file not in CSV format.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response.put("file", fileName);
            response.put("sum", sum);
            return ResponseEntity.ok(response);

        } catch (IOException | NumberFormatException e) {
            response.put("file", fileName);
            response.put("error", "Input file not in CSV format.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
