package com.clustering.k_means.csvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CountriesCsvRepresentation {
    @CsvBindByName(column = "Country")
    private String country;
    @CsvBindByName(column = "GDP ($ per capita)")
    private Double gdpPerCapita;
    @CsvBindByName(column = "Birthrate")
    private Double birthrate;
    @CsvBindByName(column = "Deathrate")
    private Double deathrate;
    @CsvBindByName(column = "Net migration")
    private Double netMigration;
    @CsvBindByName(column = "Infant mortality (per 1000 births)")
    private Double infantMortality;
    @CsvBindByName(column = "Literacy (%)")
    private Double literacy;
    @CsvBindByName(column = "Phones (per 1000)")
    private Double phonesPer1000;
    @CsvBindByName(column = "Pop. Density (per sq. mi.)")
    private Double popDensity;
    @CsvBindByName(column = "Industry")
    private Double industry;
    @CsvBindByName(column = "Service")
    private Double service;

}

// Country
// GDP ($ per capita)
// Birthrate
// Deathrate
// Net migration
// Infant mortality (per 1000 births)
// Literacy (%)
// Phones (per 1000)
// Pop. Density (per sq. mi.)
// Industry
// Service