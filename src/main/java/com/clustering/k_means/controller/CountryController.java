package com.clustering.k_means.controller;

import com.clustering.k_means.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/kmeans/readCSV")
public class CountryController {

    private final CountryService countryService;
}
