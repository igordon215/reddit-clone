import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { fetchPosts, fetchSubreddits } from '../api';
import CreateSubreddit from '../components/CreateSubreddit';

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

  const handleSubredditCreated = newSubreddit => {
    setSubreddits([...subreddits, newSubreddit]);
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="home">
      <div className="posts">
        <h2>Latest Posts</h2>
        {posts.map(post => (
          <div key={post.id} className="post">
            <h3>
              <Link to={`/post/${post.id}`}>{post.title}</Link>
            </h3>
            <p>{post.content.substring(0, 100)}...</p>
            <p>
              Posted by {post.user.login} in <Link to={`/r/${post.subreddit.name}`}>{post.subreddit.name}</Link>
            </p>
            <p>Votes: {post.voteCount}</p>
          </div>
        ))}
      </div>
      <div className="sidebar">
        <CreateSubreddit onSubredditCreated={handleSubredditCreated} />
        <div className="subreddits">
          <h2>Subreddits</h2>
          <ul>
            {subreddits.map(subreddit => (
              <li key={subreddit.id}>
                <Link to={`/r/${subreddit.name}`}>{subreddit.name}</Link>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Home;
