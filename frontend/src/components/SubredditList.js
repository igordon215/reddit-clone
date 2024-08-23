import React from 'react';

const SubredditList = ({ subreddits }) => {
  return (
    <div className="subreddit-list">
      <h2>Subreddits</h2>
      <ul>
        {subreddits.map(subreddit => (
          <li key={subreddit.id}>
            <a href={`/r/${subreddit.name}`}>{subreddit.name}</a>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SubredditList;
