import React, { useEffect, useState } from 'react';
import { Scatter } from 'react-chartjs-2';
import { Chart as ChartJS, LinearScale, PointElement, Legend, Title, Tooltip } from 'chart.js';
import { getPointData, getCentroidsData } from '../services/serviceClustering';

ChartJS.register(LinearScale, PointElement, Legend, Title, Tooltip);

const CentroidsChart = () => {
    const [data, setData] = useState([]);
    const [centroids, setCentroids] = useState([]); 
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const pointsResponse = await getPointData();
                const centroidsResponse = await getCentroidsData(); 
                setData(pointsResponse.data);
                setCentroids(centroidsResponse.data);
                console.log(centroidsResponse.data); 
                setLoading(false);
            } catch (err) {
                setError('Error fetching data. Please try again.');
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

    const datasets = data.reduce((clusters, point) => {
        const clusterIndex = point.cluster;
        if (!clusters[clusterIndex]) {
            clusters[clusterIndex] = {
                label: `Cluster ${clusterIndex}`,
                data: [],
                backgroundColor: `rgba(${Math.random() * 255}, ${Math.random() * 255}, ${Math.random() * 255}, 0.5)`,
                borderColor: `rgba(${Math.random() * 255}, ${Math.random() * 255}, ${Math.random() * 255}, 1)`,
            };
        }
        clusters[clusterIndex].data.push({ x: point.x, y: point.y });
        return clusters;
    }, {});

    const centroidDataset = centroids.map((centroid, index) => ({
        x: parseFloat(centroid.XCentroid),
        y: parseFloat(centroid.YCentroid),
        radius: 10,  
        backgroundColor: 'black',
        borderColor: 'black',
        borderWidth: 2,
        label: `Centroid ${index + 1}`,
    }));
    
    const chartData = {
        datasets: [
            ...Object.values(datasets),
            {
                label: 'Centroids',
                data: centroidDataset,
                backgroundColor: 'black',
                pointStyle: 'circle',  
                pointRadius: 10
            }
        ]        
    };

    const chartOptions = {
        plugins: {
            legend: {
                position: 'top',
            },
        },
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                min: Math.min(...data.map(point => point.x), ...centroids.map(c => c.XCentroid)) - 1,
                max: Math.max(...data.map(point => point.x), ...centroids.map(c => c.XCentroid)) + 1,
            },
            y: {
                min: Math.min(...data.map(point => point.y), ...centroids.map(c => c.YCentroid)) - 1,
                max: Math.max(...data.map(point => point.y), ...centroids.map(c => c.YCentroid)) + 1,
            },
        },
    };

    return (
        <div className="chart-container">
            <h1>Centroids Scatter Chart</h1>
            <Scatter data={chartData} options={chartOptions} />
        </div>
    );
};

export default CentroidsChart;