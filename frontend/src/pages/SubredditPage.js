import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PostList from '../components/PostList';

const SubredditPage = () => {
  const { subredditName } = useParams();
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    // TODO: Fetch posts for this subreddit from the backend API
    // For now, we'll use dummy data
    setPosts([
      { id: 1, title: `${subredditName} post 1`, content: 'This is the first post', author: 'user1', voteCount: 10 },
      { id: 2, title: `${subredditName} post 2`, content: 'This is the second post', author: 'user2', voteCount: 5 },
    ]);
  }, [subredditName]);

  return (
    <div className="subreddit-page">
      <h2>r/{subredditName}</h2>
      <PostList posts={posts} />
    </div>
  );
};

export default SubredditPage;
