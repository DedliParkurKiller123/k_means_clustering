import axios from 'axios';

const API_URL = 'http://localhost:8080/api/kmeans/clustering';

export const doClustering = (numberOfClusters, measure, norm) => {
    return axios.get(`${API_URL}/do-clustering`, {
        params: {
            clusters: numberOfClusters,
            measure: measure,
            norm: norm
        }
    });
};

export const getAllData = () => {
    return axios.get(`${API_URL}/get-all-clustering-data`);
};

export const getPointData = () => {
    return axios.get(`${API_URL}/get-point-clustering-data`);
};

export const getCentroidsData = () => {
    return axios.get(`${API_URL}/get-point-centroids-clustering-data`);
};

export const getInterClusteringData = () => {
    return axios.get(`${API_URL}/get-inter-clustering-data`);
};