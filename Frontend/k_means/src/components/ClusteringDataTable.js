import React, { useEffect, useState } from 'react';
import { getAllData } from '../services/serviceClustering'; 

const ClusteringDataTable = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedClusters, setSelectedClusters] = useState('all'); // Вибір між усіма кластерами чи одним
    const [maxClusters, setMaxClusters] = useState(0); // Максимальна кількість кластерів

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllData();
                setData(response.data);
                setLoading(false);

                // Отримуємо унікальні кластери
                const clusters = [...new Set(response.data.map((point) => point.clusterId))];
                setMaxClusters(clusters.length); // Встановлюємо максимальну кількість кластерів
            } catch (err) {
                setError('Error fetching clustering data. Please try again.');
                console.error(err);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    // Крок 1: Збираємо унікальні кластери
    const clusters = [...new Set(data.map((point) => point.clusterId))]; // Унікальні ID кластерів

    // Крок 2: Групуємо країни по кластерах
    const countriesByCluster = clusters.map((clusterId) => {
        return {
            clusterId,
            countries: data
                .filter((point) => point.clusterId === clusterId)
                .map((point) => point.nameOfCountry), // Вибираємо країну для кожного кластеру
        };
    });

    // Фільтруємо кластери в залежності від вибору
    let filteredClusters = [];
    if (selectedClusters === 'all') {
        filteredClusters = countriesByCluster;
    } else {
        // Якщо вибрано конкретний кластер, то показуємо лише його
        filteredClusters = countriesByCluster.filter(
            (cluster) => cluster.clusterId === parseInt(selectedClusters)
        );
    }

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
                {/* Генерація перемикачів для 1 до maxClusters */}
                {Array.from({ length: maxClusters }, (_, index) => (
                    <label key={index}>
                        <input 
                            type="radio" 
                            name="clusterView" 
                            value={index + 1} 
                            checked={selectedClusters === (index + 1).toString()} 
                            onChange={() => setSelectedClusters((index + 1).toString())} 
                        />
                        {index + 1} Cluster
                    </label>
                ))}
            </div>

            {/* Таблиця з даними кластерів */}
            <table className="clustering-table">
                <thead>
                    <tr>
                        {/* Заголовки для обраних кластерів */}
                        {filteredClusters.map((cluster) => (
                            <th key={cluster.clusterId}>Cluster {cluster.clusterId}</th> 
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {/* Виведення даних для кожного кластеру */}
                    <tr>
                        {filteredClusters.map((cluster, index) => (
                            <td key={index}>
                                <ul>
                                    {/* Виведення країни для кожного кластеру */}
                                    {cluster.countries.map((country, idx) => (
                                        <li key={idx}>{country}</li>
                                    ))}
                                </ul>
                            </td>
                        ))}
                    </tr>
                </tbody>
            </table>
        </div>
    );
};

export default ClusteringDataTable;
