import React, { useEffect, useState } from 'react';
import { Scatter } from 'react-chartjs-2';
import { Chart as ChartJS, LinearScale, PointElement, Legend, Title, Tooltip } from 'chart.js';
import { getPointData } from '../services/serviceClustering';

ChartJS.register(LinearScale, PointElement, Legend, Title, Tooltip);

const CentroidsChart = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getPointData();
                setData(response.data); 
                setLoading(false);
            } catch (err) {
                setError('Error fetching point data. Please try again.');
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

    const chartData = {
        datasets: Object.values(datasets),
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
                title: {
                    display: true,
                    text: 'X Coordinate',
                },
            },
            y: {
                title: {
                    display: true,
                    text: 'Y Coordinate',
                },
            },
        },
    };

    return (
        <div>
            <h1>Centroids Scatter Chart</h1>
            <Scatter data={chartData} options={chartOptions} />
        </div>
    );
};

export default CentroidsChart;
