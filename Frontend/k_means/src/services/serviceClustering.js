import axios from 'axios';

const API_URL = 'http://localhost:8080/api/kmeans/clustering';

export const doClustering = (numberOfClusters, measure) => {
    return axios.get(`${API_URL}/do-clustering`, {
        params: {
            clusters: numberOfClusters,
            measure: measure
        }
    });
};

export const getAllData = () => {
    return axios.get(`${API_URL}/get-all-clustering-data`);
};

export const getPointData = () => {
    return axios.get(`${API_URL}/get-point-clustering-data`);
};