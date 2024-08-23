import React, { useState, useEffect } from 'react';
import PostList from '../components/PostList';
import TidderList from '../components/TidderList';
import { fetchPosts, fetchTidders } from '../api';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [tidders, setTidders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [postsData, tiddersData] = await Promise.all([fetchPosts(), fetchTidders()]);
        setPosts(postsData);
        setTidders(tiddersData);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch data');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="home">
      <PostList posts={posts} />
      <TidderList tidders={tidders} />
    </div>
  );
};

export default Home;
