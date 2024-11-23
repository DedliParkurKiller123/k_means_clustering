package com.clustering.k_means.services;

import com.clustering.k_means.csvRepresentation.CountriesCsvRepresentation;
import com.clustering.k_means.models.Country;
import com.clustering.k_means.repository.CountryRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public Integer uploadCountries(MultipartFile file) {
        Set<Country> countries = parseCsv(file);
        countryRepository.saveAll(countries);
        return countries.size();
     }

    @SneakyThrows(IOException.class)
    private Set<Country> parseCsv(MultipartFile file) {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            HeaderColumnNameMappingStrategy<CountriesCsvRepresentation> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CountriesCsvRepresentation.class);
            CsvToBean<CountriesCsvRepresentation> csvToBean
                    = new CsvToBeanBuilder<CountriesCsvRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                    .build();
            return csvToBean.parse()
                    .stream()
                    .filter(c-> c.getCountry()!=null
                            && c.getGdpPerCapita()!=null
                            && c.getBirthrate()!=null
                            && c.getDeathrate()!=null
                            && c.getNetMigration()!=null
                            && c.getInfantMortality()!=null
                            && c.getLiteracy()!=null
                            && c.getPhonesPer1000()!=null
                            && c.getPopDensity()!=null
                            && c.getIndustry()!=null
                            && c.getService()!=null
                    )
                    .map(csvLine -> Country.builder()
                            .nameOfCountry(csvLine.getCountry())
                            .GDP(csvLine.getGdpPerCapita())
                            .birthrate(csvLine.getBirthrate())
                            .deathrate(csvLine.getDeathrate())
                            .netMigration(csvLine.getNetMigration())
                            .infantMortality(csvLine.getInfantMortality())
                            .literacy(csvLine.getLiteracy())
                            .phones(csvLine.getPhonesPer1000())
                            .popDensity(csvLine.getPopDensity())
                            .industry(csvLine.getIndustry())
                            .service(csvLine.getService())
                            .build()
                    ).collect(Collectors.toSet());
        }
    }
}
//private String nameOfCountry;
//private Double GDP;
//private Double birthrate;
//private Double deathrate;
//private Double netMigration;
//private Double infantMortality;
//private Double literacy;
//private Double phones;
//private Double popDensity;
//private Double industry;
//private Double service;