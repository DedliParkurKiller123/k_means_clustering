import React, { useState } from 'react';
import { readCSV } from '../services/serviceCSV'; 

const ReadCSVComponent = () => {
    const [file, setFile] = useState(null);
    const [response, setResponse] = useState(null);
    const [error, setError] = useState(null);

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
            const result = await readCSV(file);
            setResponse(result.data); 
            setError(null);
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
            {response && <p className="response-message">Response from server: {response}</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default ReadCSVComponent;
