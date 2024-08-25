import React, { useState, useEffect } from 'react';
import PostList from '../components/PostList';
import SubredditList from '../components/SubredditList';
import { fetchPosts, fetchSubreddits } from '../api';

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [subreddits, setSubreddits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [postsData, subredditsData] = await Promise.all([fetchPosts(), fetchSubreddits()]);
        setPosts(postsData);
        setSubreddits(subredditsData);
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
      <SubredditList subreddits={subreddits} />
    </div>
  );
};

export default Home;
