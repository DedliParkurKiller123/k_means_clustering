import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { readCSV, getDataSet } from '../services/serviceCSV'; 

const ReadCSVComponent = () => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState(null);
    const [dataSet, setDataSet] = useState([]);
    const [isDataLoaded, setIsDataLoaded] = useState(false);
    const navigate = useNavigate(); 

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async (event) => {
        event.preventDefault();
        if (!file) {
            setError('Please select a file before uploading.');
            return;
        }

        try {
            await readCSV(file); 
            const response = await getDataSet();
            setDataSet(response.data);
            setError(null);
            setIsDataLoaded(true); 
        } catch (err) {
            setError('Error uploading file. Please try again.');
            console.error(err);
            setIsDataLoaded(false); 
        }
    };

    const handleNavigate = () => {
        navigate('/doClustering'); 
    };

    return (
        <div className="upload-container">
            <h1>Upload CSV File</h1>
            <form onSubmit={handleUpload} className="upload-form">
                <input 
                    type="file" 
                    accept=".csv" 
                    onChange={handleFileChange} 
                    className="file-input"
                />
                <button type="submit" className="submit-button">Upload</button>
            </form>
            {error && <p className="error-message">{error}</p>}
            
            {isDataLoaded && (
                <>
                <button onClick={handleNavigate} className="submit-button center-button">Go to do clustering</button>
                    <h1>Clustering Data</h1>
                    <table className="clustering-table">
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
                            </tr>
                        </thead>
                        <tbody>
                            {dataSet.map((row, index) => (
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
                                    <td>{row.gdp}</td>
                                    <td>{row.service}</td>
                                    <td>{row.deathrate}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
};

export default ReadCSVComponent;
