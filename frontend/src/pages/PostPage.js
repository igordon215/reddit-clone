import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { fetchPost, fetchComments, createComment, voteOnPost } from '../api';

const PostPage = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [postData, commentsData] = await Promise.all([fetchPost(postId), fetchComments(postId)]);
        setPost(postData);
        setComments(commentsData);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch data');
        setLoading(false);
      }
    };

    fetchData();
  }, [postId]);

  const handleSubmitComment = async e => {
    e.preventDefault();
    try {
      const comment = await createComment(postId, newComment);
      setComments([...comments, comment]);
      setNewComment('');
    } catch (err) {
      setError('Failed to create comment');
    }
  };

  const handleVote = async voteType => {
    try {
      await voteOnPost(postId, voteType);
      const updatedPost = await fetchPost(postId);
      setPost(updatedPost);
    } catch (err) {
      setError('Failed to vote on post');
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (!post) return <div>Post not found</div>;

  return (
    <div className="post-page">
      <div className="post">
        <h2>{post.title}</h2>
        <p>{post.content}</p>
        <p>
          Posted by {post.user ? post.user.login : 'Unknown'} in{' '}
          {post.subreddit ? <Link to={`/r/${post.subreddit.name}`}>{post.subreddit.name}</Link> : 'Unknown Subreddit'}
        </p>
        <div className="vote-buttons">
          <button onClick={() => handleVote('UPVOTE')}>Upvote</button>
          <span>{post.voteCount}</span>
          <button onClick={() => handleVote('DOWNVOTE')}>Downvote</button>
        </div>
      </div>
      <div className="comments">
        <h3>Comments</h3>
        {comments.map(comment => (
          <div key={comment.id} className="comment">
            <p>{comment.content}</p>
            <p>Comment by {comment.user ? comment.user.login : 'Unknown'}</p>
          </div>
        ))}
      </div>
      <form onSubmit={handleSubmitComment} className="comment-form">
        <textarea value={newComment} onChange={e => setNewComment(e.target.value)} placeholder="Write a comment..." required />
        <button type="submit">Add Comment</button>
      </form>
    </div>
  );
};

export default PostPage;
