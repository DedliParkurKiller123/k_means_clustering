package com.clustering.k_means.models;

import jakarta.persistence.*;

@Entity
@Table(name = "countries_db")
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
}
