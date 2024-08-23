import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PostList from '../components/PostList';
import { fetchPostsForTidder } from '../api';

const TidderPage = () => {
  const { tidderName } = useParams();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const postsData = await fetchPostsForTidder(tidderName);
        setPosts(postsData);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch posts');
        setLoading(false);
      }
    };

    fetchData();
  }, [tidderName]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="tidder-page">
      <h2>t/{tidderName}</h2>
      <PostList posts={posts} />
    </div>
  );
};

export default TidderPage;
