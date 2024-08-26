import React from 'react';
import { Link } from 'react-router-dom';

const Header = ({ isAuthenticated, onLogout }) => {
  return (
    <header>
      <h1>Tidder</h1>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/subreddits">SubTidders</Link>
          </li>
          {isAuthenticated ? (
            <li>
              <button onClick={onLogout}>Logout</button>
            </li>
          ) : (
            <li>
              <Link to="/login">Login</Link>
            </li>
          )}
        </ul>
      </nav>
    </header>
  );
};

export default Header;
