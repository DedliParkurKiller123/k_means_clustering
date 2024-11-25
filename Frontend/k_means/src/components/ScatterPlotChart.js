import React, { useEffect, useState } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Tooltip, Title } from 'chart.js';
import { Chart } from 'react-chartjs-2';
import { getPointData } from '../services/serviceClustering';

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Title);

const BarChart = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const pointsResponse = await getPointData();
                setData(pointsResponse.data);
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

    const clusters = [...new Set(data.map((point) => point.cluster))];
    const clusterCounts = clusters.map((cluster) => {
        return {
            cluster,
            count: data.filter((point) => point.cluster === cluster).length,
        };
    });

    const generateRandomColor = () => {
        const r = Math.floor(Math.random() * 256);
        const g = Math.floor(Math.random() * 256);
        const b = Math.floor(Math.random() * 256);
        return `rgba(${r}, ${g}, ${b}, 0.5)`; 
    };

    const colors = clusters.map(() => generateRandomColor());
    const borderColors = colors.map((color) => color.replace('0.5', '1')); 

    const chartData = {
        labels: clusters.map((cluster) => `Cluster ${cluster}`),
        datasets: [
            {
                data: clusterCounts.map((cluster) => cluster.count),
                backgroundColor: colors,
                borderColor: borderColors,
                borderWidth: 1,
            },
        ],
    };

    const chartOptions = {
        plugins: {
            legend: {
                display: false,
            },
            tooltip: {
                callbacks: {
                    label: (tooltipItem) => {
                        return `Count: ${tooltipItem.raw}`;
                    },
                },
            },
        },
        scales: {
            x: {
                title: {
                    display: true,
                    text: 'Clusters',
                },
            },
            y: {
                title: {
                    display: true,
                    text: 'Number of Points',
                },
                beginAtZero: true,
            },
        },
    };

    return (
        <div className="chart-container">
            <h1>Bar Chart - Clusters</h1>
            <Chart type="bar" data={chartData} options={chartOptions} />
        </div>
    );
};

export default BarChart;
