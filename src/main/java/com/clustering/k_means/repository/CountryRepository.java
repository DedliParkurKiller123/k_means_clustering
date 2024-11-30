package com.clustering.k_means.repository;

import com.clustering.k_means.models.Country;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @QueryHints(value = @QueryHint(name = "org.hibernate.fetchSize", value = "50"))
    @Query("SELECT c FROM Country c")
    Stream<Country> findCountriesStream();

    boolean existsAllByIdCountryIsNotNull();

    @Modifying
    @Query(value = "ALTER SEQUENCE seq_countries RESTART WITH 1"
            , nativeQuery = true)
    void resetIdSequence();
}
