import React, { useState } from 'react';
import { doClustering } from '../services/serviceClustering'; 

const DoClusteringComponent = () => {
    const [numberOfClusters, setNumberOfClusters] = useState('');
    const [measure, setMeasure] = useState('');
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);

    const handleClustering = async (event) => {
        event.preventDefault();

        if (!numberOfClusters || !measure) {
            setError('Please provide both the number of clusters and a measure.');
            return;
        }

        try {
            const response = await doClustering(numberOfClusters, measure);
            setResult(response.data); 
            setError(null);
        } catch (err) {
            setError('Error during clustering. Please try again.');
            console.error(err);
        }
    };

    return (
        <div className="clustering-container">
            <h1>Perform Clustering</h1>
            <form onSubmit={handleClustering} className="clustering-form">
                <div className="form-group">
                    <label>Number of Clusters:</label>
                    <input
                        type="number"
                        value={numberOfClusters}
                        onChange={(e) => setNumberOfClusters(e.target.value)}
                        required
                        className="input-field"
                    />
                </div>
                <div className="form-group">
                    <label>Measure:</label>
                    <select
                        value={measure}
                        onChange={(e) => setMeasure(e.target.value)}
                        required
                        className="select-field"
                    >
                        <option value="">Select Measure</option>
                        <option value="EUCLIDEAN">Euclidean</option>
                        <option value="MANHATTAN">Manhattan</option>
                        <option value="MINKOWSKI">Minkowski</option>
                    </select>
                </div>
                <button type="submit" className="submit-button">Start Clustering</button>
            </form>
            {result !== null && (
                <p className="result-message">Clustering successful: {result ? 'Yes' : 'No'}</p>
            )}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default DoClusteringComponent;
