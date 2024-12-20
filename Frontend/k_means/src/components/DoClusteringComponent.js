import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { doClustering } from '../services/serviceClustering'; 

const DoClusteringComponent = () => {
    const [numberOfClusters, setNumberOfClusters] = useState('');
    const [measure, setMeasure] = useState('');
    const [norm, setNorm] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate(); 

    const handleClustering = async (event) => {
        event.preventDefault();

        if (!numberOfClusters || !measure) {
            setError('Please provide both the number of clusters and a measure.');
            return;
        }

        try {
            await doClustering(numberOfClusters, measure, norm);
            setError(null);
            navigate('/results');
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
                <div className="form-group">
                    <label>Normalization:</label>
                    <select
                        value={norm}
                        onChange={(e) => setNorm(e.target.value)}
                        required
                        className="select-field"
                    >
                        <option value="">Select Normalization</option>
                        <option value="NORMALIZATION">Normalization</option>
                        <option value="STANDARDIZATION">Standardization</option>
                    </select>
                </div>
                <button type="submit" className="submit-button">Start Clustering</button>
            </form>
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default DoClusteringComponent;
