import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import TidderPage from './pages/TidderPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/t/:tidderName" element={<TidderPage />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
