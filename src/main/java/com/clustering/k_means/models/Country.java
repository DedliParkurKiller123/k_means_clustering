package com.clustering.k_means.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries_db")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {

    @SequenceGenerator(
            sequenceName = "seq_countries",
            name = "seq_countries",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "seq_countries",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    private Long idCountry;
    private String nameOfCountry;
    private Double GDP;
    private Double birthrate;
    private Double deathrate;
    private Double netMigration;
    private Double infantMortality;
    private Double literacy;
    private Double phones;
    private Double popDensity;
    private Double industry;
    private Double service;
}
