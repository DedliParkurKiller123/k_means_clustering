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
        <div>
            <h1>Upload CSV File</h1>
            <form onSubmit={handleUpload}>
                <input 
                    type="file" 
                    accept=".csv" 
                    onChange={handleFileChange} 
                />
                <button type="submit">Upload</button>
            </form>
            {response && <p>Response from server: {response}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default ReadCSVComponent;
