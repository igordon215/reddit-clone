import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PostList from '../components/PostList';
import { fetchPostsForSubreddit } from '../api';

const SubredditPage = () => {
  const { subredditName } = useParams();
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const postsData = await fetchPostsForSubreddit(subredditName);
        setPosts(postsData);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch posts');
        setLoading(false);
      }
    };

    fetchPosts();
  }, [subredditName]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="subreddit-page">
      <h2>r/{subredditName}</h2>
      <PostList posts={posts} />
    </div>
  );
};

export default SubredditPage;
