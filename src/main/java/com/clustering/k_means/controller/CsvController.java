package com.clustering.k_means.controller;

import com.clustering.k_means.services.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/kmeans/readCSV")
public class CsvController {

    private final CsvService csvService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadCountries(
            @RequestPart("file")MultipartFile file){
        return ResponseEntity.ok(csvService.uploadCountries(file));
    }
}
