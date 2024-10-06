import React from 'react';
import { Link } from 'react-router-dom';

const SubredditList = ({ subreddits }) => {
  return (
    <div className="subreddit-list">
      <h2>SubTidders</h2>
      <ul>
        {subreddits.map(subreddit => (
          <li key={subreddit.id}>
            <Link to={`/r/${subreddit.name}`}>{subreddit.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SubredditList;
