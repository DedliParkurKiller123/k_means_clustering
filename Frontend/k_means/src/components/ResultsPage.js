import React, { useState } from 'react';
import CentroidsChart from './CentroidsChart';
import ClusteringDataTable from './ClusteringDataTable';
import ScatterPlotChart from './ScatterPlotChart';

const ResultsPage = () => {
    const [activeTab, setActiveTab] = useState('chart'); 

    return (
        <div className="results-page">
            <div className="tabs">
                <button 
                    className={`tab-button ${activeTab === 'chart' ? 'active' : ''}`} 
                    onClick={() => setActiveTab('chart')}
                >
                    Centroids Chart
                </button>
                <button 
                    className={`tab-button ${activeTab === 'table' ? 'active' : ''}`} 
                    onClick={() => setActiveTab('table')}
                >
                    Clustering Table
                </button>
                <button 
                    className={`tab-button ${activeTab === 'scatter' ? 'active' : ''}`} 
                    onClick={() => setActiveTab('scatter')}
                >
                    Clustering Scatter
                </button>
            </div>

            <div className="tab-content">
                {activeTab === 'chart' && <CentroidsChart />}
                {activeTab === 'table' && <ClusteringDataTable />}
                {activeTab === 'scatter' && <ScatterPlotChart />}
            </div>
        </div>
    );
};

export default ResultsPage;
