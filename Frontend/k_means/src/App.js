import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import ReadCSVComponent from './components/ReadCSVComponent';
import DoClusteringComponent from './components/DoClusteringComponent';
import ResultsPage from './components/ResultsPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/readCSV" />} />
                <Route path="/readCSV" element={<ReadCSVComponent />} />
                <Route path="/doClustering" element={<DoClusteringComponent />} />
                <Route path="/results/*" element={<ResultsPage />} />
            </Routes>
        </Router>
    );
};

export default App;
