package com.clustering.k_means.controller;

import com.clustering.k_means.services.ClusteringService;
import com.clustering.k_means.services.measures.Measure;
import com.clustering.k_means.services.normalizations.Normalization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import weka.filters.UnsupervisedFilter;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/kmeans/clustering")
public class ClusteringController {

    private final ClusteringService clusteringService;

    @GetMapping("/do-clustering")
    public ResponseEntity<String> doClustering(
            @RequestParam("clusters") Integer cluster,
            @RequestParam("measure") Measure measure,
            @RequestParam("norm") Normalization normalization) {
        return ResponseEntity.ok(clusteringService
                .clusteredData(cluster, measure, normalization));
    }

    @GetMapping("/get-all-clustering-data")
    public ResponseEntity<List<Map<String,Object>>> getAllClusteringData() {
        return ResponseEntity.ok(clusteringService.getAllClusteredData());
    }

    @GetMapping("/get-point-clustering-data")
    public ResponseEntity<List<Map<String,Object>>> getPointClusteringData() {
        return ResponseEntity.ok(clusteringService.getPointClusteredData());
    }

    @GetMapping("/get-point-centroids-clustering-data")
    public ResponseEntity<List<Map<String,Double>>> getCentroidsClusteringData() {
        return ResponseEntity.ok(clusteringService.getCentroidsClusteredData());
    }

}