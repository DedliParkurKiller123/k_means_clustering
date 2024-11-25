package com.clustering.k_means.services;

import com.clustering.k_means.models.Country;
import com.clustering.k_means.repository.CountryRepository;
import com.clustering.k_means.services.measures.Measure;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import weka.clusterers.SimpleKMeans;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.filters.unsupervised.attribute.Standardize;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClusteringService {

    private final CountryRepository countryRepository;

    private final List<Map<String,Object>> allData = new ArrayList<>();
    private final List<Map<String,Object>> pointData = new ArrayList<>();

    @SneakyThrows(Exception.class)
    @Transactional
    public String clusteredData(int numberOfClusters, Measure measure) {
        allData.clear();
        pointData.clear();
        List<Country> countries = getAllCountriesRecords();

        Instances data = prepareDataForClustering(countries);

        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(numberOfClusters);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setDistanceFunction(setMeasures(measure));
        return doAllData(countries, data, kmeans) && doPointData(data, kmeans)
                ? "Clustering is successful":"Clustering is unsuccessful";
    }

    private List<Country> getAllCountriesRecords() {
        try(Stream<Country> countryStream= countryRepository.findCountriesStream()){
            return countryStream.collect(Collectors.toList());
        }
    }

    public List<Map<String, Object>> getPointClusteredData() {
        return pointData;
    }

    public List<Map<String, Object>> getAllClusteredData() {
        return allData;
    }

    @SneakyThrows(Exception.class)
    private Boolean doPointData(Instances data, SimpleKMeans kmeans) {
        PrincipalComponents pca = new PrincipalComponents();
        pca.setMaximumAttributes(2);
        pca.setInputFormat(data);

        Instances reducedData = Filter.useFilter(data, pca);
        kmeans.buildClusterer(reducedData);

        int[] assignments = kmeans.getAssignments();

        Attribute clusterAttribute = new Attribute("cluster");
        reducedData.insertAttributeAt(clusterAttribute, reducedData.numAttributes());

        for(int i =0;i< reducedData.numInstances();i++){
            reducedData.instance(i).setValue(reducedData.numAttributes()-1, assignments[i]);
        }
        pointData.addAll(convertData(reducedData));
        return isClusteringSuccessful(kmeans);
    }

    @SneakyThrows(Exception.class)
    private Boolean doAllData(List<Country> countries,Instances data, SimpleKMeans kmeans) {
        kmeans.buildClusterer(data);
        int[] assignments = kmeans.getAssignments();
        for (int i = 0; i < assignments.length; i++) {
            Map<String, Object> clusterInfo = getStringObjectMap(countries, i, assignments);
            allData.add(clusterInfo);
        }
        return isClusteringSuccessful(kmeans);
    }

    @SneakyThrows(Exception.class)
    private Instances standardization(Instances data) {
        Standardize standardize = new Standardize();
        standardize.setInputFormat(data);
        return Filter.useFilter(data, standardize);
    }

    private Map<String, Object> getStringObjectMap(List<Country> countries, int i, int[] assignments) {
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
        return clusterInfo;
    }

    private List<Map<String, Object>> convertData(Instances reducedData) {
        List<Map<String,Object>> data = new ArrayList<>();
        for (int i = 0; i < reducedData.numInstances(); i++) {
            Instance instance = reducedData.instance(i);
            Map<String,Object> point = new HashMap<>();
            point.put("x",instance.value(0));
            point.put("y",instance.value(1));
            point.put("cluster",(int)instance.value(reducedData.numAttributes()-1));
            data.add(point);
        }
        return data;
    }

    private DistanceFunction setMeasures(Measure measure) {
        return measure.getMeasureStatus();
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
        countries.forEach(
                country -> {
                    Instance instance = getInstance(country);
                    data.add(instance);
                }
        );

        return standardization(data);
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

    @SneakyThrows(Exception.class)
    public boolean isClusteringSuccessful(SimpleKMeans kmeans) {
        Instances centroids = kmeans.getClusterCentroids();
        if (centroids == null || centroids.numInstances() == 0) {
            return false;
        }
        if (kmeans.getSquaredError() <= 0) {
            return false;
        }
        return Arrays.stream(kmeans.getClusterSizes()).noneMatch(size -> size == 0);
    }
}
