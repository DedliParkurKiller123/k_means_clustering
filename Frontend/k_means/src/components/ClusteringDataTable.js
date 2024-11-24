import React, { useEffect, useState } from 'react';
import { getAllData } from '../services/serviceClustering'; 

const ClusteringDataTable = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllData();
                setData(response.data);
                setLoading(false);
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
        return <p style={{ color: 'red' }}>{error}</p>;
    }

    return (
        <div>
            <h1>Clustering Data</h1>
            <table border="1" style={{ borderCollapse: 'collapse', width: '100%' }}>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Country</th>
                        <th>Population Density</th>
                        <th>Phones</th>
                        <th>Birthrate</th>
                        <th>Industry</th>
                        <th>Infant Mortality</th>
                        <th>Net Migration</th>
                        <th>Literacy</th>
                        <th>GDP</th>
                        <th>Service</th>
                        <th>Deathrate</th>
                        <th>Cluster ID</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, index) => (
                        <tr key={index}>
                            <td>{row.idCountry}</td>
                            <td>{row.nameOfCountry}</td>
                            <td>{row.popDensity}</td>
                            <td>{row.phones}</td>
                            <td>{row.birthrate}</td>
                            <td>{row.industry}</td>
                            <td>{row.infantMortality}</td>
                            <td>{row.netMigration}</td>
                            <td>{row.literacy}</td>
                            <td>{row.GDP}</td>
                            <td>{row.service}</td>
                            <td>{row.deathrate}</td>
                            <td>{row.clusterId}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ClusteringDataTable;
