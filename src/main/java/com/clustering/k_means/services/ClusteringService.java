package com.clustering.k_means.services;

import com.clustering.k_means.models.Country;
import com.clustering.k_means.repository.CountryRepository;
import com.clustering.k_means.services.measures.Measure;
import com.clustering.k_means.services.normalizations.Normalization;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weka.clusterers.SimpleKMeans;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
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
    private final List<Map<String,Double>> centroids = new ArrayList<>();
    private final List<Map<String,Object>> interData = new ArrayList<>();

    @SneakyThrows(Exception.class)
    @Transactional
    public String clusteredData(int numberOfClusters, Measure measure, Normalization normalization) {
        clearData();
        List<Country> countries = getAllCountriesRecords();

        Instances data = prepareDataForClustering(countries, normalization);

        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(numberOfClusters);
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setDistanceFunction(measure.getMeasureStatus());
        kmeans.buildClusterer(data);
        allData(countries, data, kmeans);
        return isClusteringSuccessful(kmeans) ? "Clustering is successful" : "Clustering is unsuccessful";
    }

    @SneakyThrows(Exception.class)
    private void allData(List<Country> countries, Instances data, SimpleKMeans kmeans) {
        Instances clusteredData = new Instances(data);
        for(int i =0; i< data.numInstances();i++){
            clusteredData.instance(i).setValue(data.numAttributes() - 1
                    , kmeans.clusterInstance(data.instance(i)) + 1);
        }

        doInterData(kmeans);
        doAllData(countries,kmeans);
        doPointData(kmeans, clusteredData);
//        return clusteredData;
    }

    public List<Map<String, Object>> getInterClusteredData() {
        return interData;
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
    private void doPointData(SimpleKMeans kmeans, Instances clusteredData) {
        PrincipalComponents pca = new PrincipalComponents();
        pca.setMaximumAttributes(2);
        pca.setInputFormat(clusteredData);

        Instances reducedData = Filter.useFilter(clusteredData, pca);
        kmeans.buildClusterer(reducedData);
        centroids.addAll(findCentroidsFromKmeans(kmeans));

        int[] assignments = kmeans.getAssignments();
        Attribute clusterAttribute = new Attribute("cluster");
        reducedData.insertAttributeAt(clusterAttribute, reducedData.numAttributes());

        for(int i =0; i< reducedData.numInstances(); i++){
            reducedData.instance(i).setValue(reducedData.numAttributes()-1, assignments[i]+1);
        }
        pointData.addAll(convertData(reducedData));
    }

    @SneakyThrows(Exception.class)
    private void doAllData(List<Country> countries, SimpleKMeans kmeans) {
        int[] assignments = kmeans.getAssignments();
        for (int i = 0; i < assignments.length; i++) {
            Map<String, Object> clusterInfo = getStringObjectMap(countries, i, assignments);
            allData.add(clusterInfo);
        }
    }

    @SneakyThrows(Exception.class)
    private Instances standardization(Instances data, Normalization normalization) {
        var norm = getNormalization(data, normalization);
        return Filter.useFilter(data, (Filter) Objects.requireNonNull(norm));
    }

    @SneakyThrows(Exception.class)
    private Object getNormalization(Instances data, Normalization normalization) {
        switch (normalization){
            case NORMALIZATION -> {
                Normalize normalize = new Normalize();
                normalize.setInputFormat(data);
                return normalize;
            }
            case STANDARDIZATION -> {
                Standardize standardize = new Standardize();
                standardize.setInputFormat(data);
                return standardize;
            }
            case null, default -> {
                return null;
            }
        }
    }

    private Map<String, Object> getStringObjectMap(List<Country> countries, int i, int[] assignments) {
        Map<String, Object> clusterInfo = new HashMap<>();
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
        clusterInfo.put("cluster", assignments[i]+1);
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

    private Instances prepareDataForClustering(List<Country> countries, Normalization normalization) {
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

        return standardization(data, normalization);
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

    public List<Map<String, Double>> getCentroidsClusteredData() {
        return centroids;
    }

    private List<Map<String, Double>> findCentroidsFromKmeans(SimpleKMeans kMeans) {
        return kMeans.getClusterCentroids().stream().map(obj->{
            Map<String, Double> centroid = new HashMap<>();
            centroid.put("XCentroid",obj.value(0));
            centroid.put("YCentroid",obj.value(1));
            return centroid;
        }).collect(Collectors.toList());
    }

    @SneakyThrows(Exception.class)
    private void doInterData(SimpleKMeans kmeans) {
        Instances centroids = kmeans.getClusterCentroids();
        for(int i = 0; i < centroids.numInstances(); i++) {
                interData.add(convertInterData(i, centroids));
        }
    }

    private Map<String, Object> convertInterData(int i, Instances centroids) {
        Map<String, Object> clusterInfo = new HashMap<>();
        clusterInfo.put("GDP", centroids.instance(i).value(0));
        clusterInfo.put("birthrate", centroids.instance(i).value(1));
        clusterInfo.put("deathrate",  centroids.instance(i).value(2));
        clusterInfo.put("netMigration",  centroids.instance(i).value(3));
        clusterInfo.put("infantMortality",  centroids.instance(i).value(4));
        clusterInfo.put("literacy",  centroids.instance(i).value(5));
        clusterInfo.put("phones",  centroids.instance(i).value(6));
        clusterInfo.put("popDensity",  centroids.instance(i).value(7));
        clusterInfo.put("industry",  centroids.instance(i).value(8));
        clusterInfo.put("service",  centroids.instance(i).value(9));
        Map<String, Object> outClusterInfo = new HashMap<>();
        outClusterInfo.put("Cluster "+(i+1),clusterInfo);
        return outClusterInfo;
    }

    private void clearData() {
        allData.clear(); pointData.clear(); centroids.clear(); interData.clear();
    }
}
