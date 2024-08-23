import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PostList from '../components/PostList';

const TidderPage = () => {
  const { tidderName } = useParams();
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    // TODO: Fetch posts for this tidder from the backend API
    // For now, we'll use dummy data
    setPosts([
      { id: 1, title: `${tidderName} post 1`, content: 'This is the first post', author: 'user1', voteCount: 10 },
      { id: 2, title: `${tidderName} post 2`, content: 'This is the second post', author: 'user2', voteCount: 5 },
    ]);
  }, [tidderName]);

  return (
    <div className="tidder-page">
      <h2>t/{tidderName}</h2>
      <PostList posts={posts} />
    </div>
  );
};

export default TidderPage;
