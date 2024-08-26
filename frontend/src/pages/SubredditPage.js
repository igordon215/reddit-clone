import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchSubreddit, fetchPostsForSubreddit, createPost } from '../api';

const SubredditPage = () => {
  const { subredditName } = useParams();
  const [subreddit, setSubreddit] = useState(null);
  const [posts, setPosts] = useState([]);
  const [newPost, setNewPost] = useState({ title: '', content: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const subredditData = await fetchSubreddit(subredditName);
        setSubreddit(subredditData);
        const postsData = await fetchPostsForSubreddit(subredditData.id);
        setPosts(postsData);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch data');
        setLoading(false);
      }
    };

    fetchData();
  }, [subredditName]);

  const handleSubmitPost = async e => {
    e.preventDefault();
    try {
      const createdPost = await createPost({
        ...newPost,
        subredditId: subreddit.id,
      });
      setPosts([createdPost, ...posts]);
      setNewPost({ title: '', content: '' });
    } catch (err) {
      setError('Failed to create post');
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (!subreddit) return <div>Subreddit not found</div>;

  return (
    <div className="subreddit-page">
      <h1>{subreddit.name}</h1>
      <p>{subreddit.description}</p>

      <div className="create-post">
        <h2>Create a new post</h2>
        <form onSubmit={handleSubmitPost}>
          <input
            type="text"
            value={newPost.title}
            onChange={e => setNewPost({ ...newPost, title: e.target.value })}
            placeholder="Post title"
            required
          />
          <textarea
            value={newPost.content}
            onChange={e => setNewPost({ ...newPost, content: e.target.value })}
            placeholder="Post content"
            required
          />
          <button type="submit">Create Post</button>
        </form>
      </div>

      <div className="posts">
        <h2>Posts</h2>
        {posts.map(post => (
          <div key={post.id} className="post">
            <h3>
              <Link to={`/post/${post.id}`}>{post.title}</Link>
            </h3>
            <p>{post.content.substring(0, 100)}...</p>
            <p>Posted by {post.user && post.user.login}</p>
            <p>Votes: {post.voteCount}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SubredditPage;
