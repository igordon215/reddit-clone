import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { fetchPost, fetchComments, createComment } from '../api';

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

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (!post) return <div>Post not found</div>;

  return (
    <div className="post-page">
      <div className="post">
        <h2>{post.title}</h2>
        <p>{post.content}</p>
        <p>
          Posted by {post.user.login} in {post.subreddit.name}
        </p>
        <p>Votes: {post.voteCount}</p>
      </div>
      <div className="comments">
        <h3>Comments</h3>
        {comments.map(comment => (
          <div key={comment.id} className="comment">
            <p>{comment.content}</p>
            <p>Comment by {comment.user.login}</p>
          </div>
        ))}
      </div>
      <form onSubmit={handleSubmitComment}>
        <textarea value={newComment} onChange={e => setNewComment(e.target.value)} placeholder="Write a comment..." required />
        <button type="submit">Add Comment</button>
      </form>
    </div>
  );
};

export default PostPage;
