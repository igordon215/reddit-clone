import React, { useState, useEffect } from 'react';
import PostList from '../components/PostList';
import TidderList from '../components/TidderList';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [tidders, setTidders] = useState([]);

  useEffect(() => {
    // TODO: Fetch posts and tidders from the backend API
    // For now, we'll use dummy data
    setPosts([
      { id: 1, title: 'First post', content: 'This is the first post', author: 'user1', voteCount: 10 },
      { id: 2, title: 'Second post', content: 'This is the second post', author: 'user2', voteCount: 5 },
    ]);
    setTidders([
      { id: 1, name: 'AskTidder' },
      { id: 2, name: 'funny' },
      { id: 3, name: 'news' },
    ]);
  }, []);

  return (
    <div className="home">
      <PostList posts={posts} />
      <TidderList tidders={tidders} />
    </div>
  );
};

export default Home;
