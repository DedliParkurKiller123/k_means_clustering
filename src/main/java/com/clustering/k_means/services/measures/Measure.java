package com.clustering.k_means.services.measures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.text.similarity.CosineDistance;
import weka.core.*;

@AllArgsConstructor
@Getter
public enum Measure {

    EUCLIDEAN(new EuclideanDistance()),
    MANHATTAN(new ManhattanDistance()),
    MINKOWSKI(new ManhattanDistance());

    private final DistanceFunction measureStatus;
}
