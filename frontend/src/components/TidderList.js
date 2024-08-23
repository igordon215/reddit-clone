import React from 'react';
import { Link } from 'react-router-dom';

const TidderList = ({ tidders }) => {
  return (
    <div className="tidder-list">
      <h2>Tidders</h2>
      <ul>
        {tidders.map(tidder => (
          <li key={tidder.id}>
            <Link to={`/t/${tidder.name}`}>{tidder.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TidderList;
