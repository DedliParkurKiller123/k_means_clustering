import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import ReadCSVComponent from './components/ReadCSVComponent'
import DoClusteringComponent from './components/DoClusteringComponent';
import ClusteringDataTable from './components/ClusteringDataTable';
import CentroidsChart from './components/CentroidsChart';

const App = () => {
    return (
        <Router>
            <div>
                <Routes>
                    <Route path="/readCSV" element={<ReadCSVComponent/>} />
                    <Route path="/doClustering" element={<DoClusteringComponent/>} />
                    <Route path="/clusteringTable" element={<ClusteringDataTable/>} />
                    <Route path="/clusteringCentroid" element={<CentroidsChart/>} />
                </Routes>
            </div>
        </Router>
    );
};

export default App;