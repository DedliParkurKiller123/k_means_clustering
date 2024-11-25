import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import { readCSV } from '../services/serviceCSV'; 

const ReadCSVComponent = () => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState(null);
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
            setError(null);
            navigate('/doClustering');
        } catch (err) {
            setError('Error uploading file. Please try again.');
            console.error(err);
        }
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
        </div>
    );
};

export default ReadCSVComponent;
