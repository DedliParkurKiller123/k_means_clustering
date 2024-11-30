import React, { useEffect, useState } from 'react';
import { getAllData, getInterClusteringData } from '../services/serviceClustering'; 

const ClusteringDataTable = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedClusters, setSelectedClusters] = useState('all'); // Вибір між усіма кластерами чи одним
    const [maxClusters, setMaxClusters] = useState(0); // Максимальна кількість кластерів
    const [interClusterData, setInterClusterData] = useState([]); // Дані середніх значень ознак

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllData();
                setData(response.data);
                setLoading(false);

                // Отримуємо унікальні кластери
                const clusters = [...new Set(response.data.map((point) => point.cluster))];
                setMaxClusters(clusters.length); // Встановлюємо максимальну кількість кластерів
            } catch (err) {
                setError('Error fetching clustering data. Please try again.');
                console.error(err);
                setLoading(false);
            }
        };

        const fetchInterClusteringData = async () => {
            try {
                const response = await getInterClusteringData();
                setInterClusterData(response.data);
            } catch (err) {
                setError('Error fetching inter-cluster data. Please try again.');
                console.error(err);
            }
        };

        fetchData();
        fetchInterClusteringData();
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    // Крок 1: Збираємо унікальні кластери
    const clusters = [...new Set(data.map((point) => point.cluster))]; // Унікальні ID кластерів

    // Крок 2: Групуємо країни по кластерах
    const countriesByCluster = clusters.map((cluster) => {
        return {
            cluster,
            countries: data
                .filter((point) => point.cluster === cluster)
                .map((point) => point.nameOfCountry), // Вибираємо країну для кожного кластеру
        };
    });

    // Крок 3: Групуємо середні значення ознак для кожного кластеру
    const interClusterInfo = interClusterData.map((item) => {
        const clusterName = Object.keys(item)[0]; // "Cluster 1", "Cluster 2", ...
        const clusterAverages = Object.values(item)[0]; // Дані для кластера
        return { clusterName, clusterAverages };
    });

    // Фільтруємо кластери в залежності від вибору
    const filteredClusters = selectedClusters === 'all'
        ? countriesByCluster
        : countriesByCluster.filter(
              (cluster) => cluster.cluster === parseInt(selectedClusters)
          );

    const filteredInterClusters = selectedClusters === 'all'
        ? interClusterInfo
        : interClusterInfo.filter(
              (cluster) => cluster.clusterName === `Cluster ${selectedClusters}`
          );

    return (
        <div className="table-container">
            <h1>Clustering Data</h1>

            {/* Перемикач для вибору між усіма кластерами або одним кластером */}
            <div className="cluster-selector">
                <label>
                    <input 
                        type="radio" 
                        name="clusterView" 
                        value="all" 
                        checked={selectedClusters === 'all'} 
                        onChange={() => setSelectedClusters('all')} 
                    />
                    All Clusters
                </label>
                {Array.from({ length: maxClusters }, (_, index) => (
                    <label key={index}>
                        <input 
                            type="radio" 
                            name="clusterView" 
                            value={index + 1} 
                            checked={selectedClusters === (index + 1).toString()} 
                            onChange={() => setSelectedClusters((index + 1).toString())} 
                        />
                        Cluster {index + 1}
                    </label>
                ))}
            </div>

            {/* Таблиця з даними кластерів */}
            <table className="clustering-table">
                <thead>
                    <tr>
                        {filteredClusters.map((cluster) => (
                            <th key={cluster.cluster}>Cluster {cluster.cluster}</th>
                        ))}
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        {filteredClusters.map((cluster, index) => (
                            <td key={index}>
                                <ul>
                                    {cluster.countries.map((country, idx) => (
                                        <li key={idx}>{country}</li>
                                    ))}
                                </ul>
                            </td>
                        ))}
                    </tr>
                </tbody>
            </table>

            {/* Таблиця з середніми значеннями */}
            <h2>Cluster Averages</h2>
            <table className="clustering-table">
                <thead>
                    <tr>
                        <th>Cluster</th>
                        {filteredInterClusters.length > 0 &&
                            Object.keys(filteredInterClusters[0].clusterAverages).map(
                                (feature, index) => <th key={index}>{feature}</th>
                            )}
                    </tr>
                </thead>
                <tbody>
                    {filteredInterClusters.map((cluster, index) => (
                        <tr key={index}>
                            <td>{cluster.clusterName}</td>
                            {Object.values(cluster.clusterAverages).map((value, idx) => (
                                <td key={idx}>{value.toFixed(2)}</td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ClusteringDataTable;
