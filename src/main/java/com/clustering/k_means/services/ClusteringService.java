package com.clustering.k_means.services;

import com.clustering.k_means.models.Country;
import com.clustering.k_means.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClusteringService {

    private final CountryRepository countryRepository;

    @SneakyThrows(Exception.class)
    public List<Map<String,Object>> clusteredData() {
        List<Country> countries = countryRepository.findAll();
        Instances data = prepareDataForClustering(countries);

        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(3);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setDistanceFunction(new EuclideanDistance());
        kmeans.buildClusterer(data);

        int[] assignments = kmeans.getAssignments();

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < assignments.length; i++) {
            Map<String, Object> clusterInfo = new HashMap<>();
            clusterInfo.put("idCountry", countries.get(i).getIdCountry());
            clusterInfo.put("nameOfCountry", countries.get(i).getNameOfCountry());
            clusterInfo.put("GDP", countries.get(i).getGDP());
            clusterInfo.put("birthrate", countries.get(i).getBirthrate());
            clusterInfo.put("deathrate", countries.get(i).getDeathrate());
            clusterInfo.put("netMigration", countries.get(i).getNetMigration());
            clusterInfo.put("infantMortality", countries.get(i).getInfantMortality());
            clusterInfo.put("literacy", countries.get(i).getLiteracy());
            clusterInfo.put("phones", countries.get(i).getPhones());
            clusterInfo.put("popDensity", countries.get(i).getPopDensity());
            clusterInfo.put("industry", countries.get(i).getIndustry());
            clusterInfo.put("service", countries.get(i).getService());
            clusterInfo.put("clusterId", assignments[i]);
            result.add(clusterInfo);
        }

        return result;

    }

    private Instances prepareDataForClustering(List<Country> countries) {
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute("GDP"));
        attributes.addElement(new Attribute("birthrate"));
        attributes.addElement(new Attribute("deathrate"));
        attributes.addElement(new Attribute("netMigration"));
        attributes.addElement(new Attribute("infantMortality"));
        attributes.addElement(new Attribute("literacy"));
        attributes.addElement(new Attribute("phones"));
        attributes.addElement(new Attribute("popDensity"));
        attributes.addElement(new Attribute("industry"));
        attributes.addElement(new Attribute("service"));

        Instances data = new Instances("Clustering", attributes, countries.size());

        for (Country country : countries) {
            Instance instance = getInstance(country);
            data.add(instance);
        }

        return standardization(data);
    }

    @SneakyThrows(Exception.class)
    private Instances standardization(Instances data) {
        Standardize standardize = new Standardize();
        standardize.setInputFormat(data);
        return Filter.useFilter(data, standardize);
    }

    private Instance getInstance(Country country) {
        Instance instance = new DenseInstance(10);
        instance.setValue(0, country.getGDP());
        instance.setValue(1, country.getBirthrate());
        instance.setValue(2, country.getDeathrate());
        instance.setValue(3, country.getNetMigration());
        instance.setValue(4, country.getInfantMortality());
        instance.setValue(5, country.getLiteracy());
        instance.setValue(6, country.getPhones());
        instance.setValue(7, country.getPopDensity());
        instance.setValue(8, country.getIndustry());
        instance.setValue(9, country.getService());
        return instance;
    }
}
