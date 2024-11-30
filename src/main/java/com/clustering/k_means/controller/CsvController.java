package com.clustering.k_means.controller;

import com.clustering.k_means.models.Country;
import com.clustering.k_means.services.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/kmeans/readCSV")
public class CsvController {

    private final CsvService csvService;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Integer> uploadCountries(
            @RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(csvService.uploadCountries(file));
    }

    @GetMapping("/getDataSet")
    public ResponseEntity<List<Country>> getDataSet(){
        return ResponseEntity.ok(csvService.getDataSet());
    }
}
