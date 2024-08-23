import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header>
      <h1>Tidder</h1>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/tidders">Tidders</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
