import React from 'react';

const Header = () => {
  return (
    <header>
      <h1>Reddit Clone</h1>
      <nav>
        <ul>
          <li>
            <a href="/">Home</a>
          </li>
          <li>
            <a href="/subreddits">Subreddits</a>
          </li>
        </ul>
      </nav>
    </header>
  );
};

export default Header;
