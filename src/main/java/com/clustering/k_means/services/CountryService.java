package com.clustering.k_means.services;

import com.clustering.k_means.CsvRepresentation.CountriesCsvRepresentation;
import com.clustering.k_means.models.Country;
import com.clustering.k_means.repository.CountryRepository;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public Integer uploadCountries(MultipartFile file) {
        Set<Country> countries = parseCsv(file);
     }

    @SneakyThrows(IOException.class)
    private Set<Country> parseCsv(MultipartFile file) {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            HeaderColumnNameMappingStrategy<CountriesCsvRepresentation> strategy
                    = new HeaderColumnNameMappingStrategy<>();
        }
    }
}
