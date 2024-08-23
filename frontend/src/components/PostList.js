import React from 'react';
import { voteOnPost } from '../api';

const PostList = ({ posts }) => {
  const handleVote = async (postId, voteType) => {
    try {
      const updatedPost = await voteOnPost(postId, voteType);
      // TODO: Update the post in the list with the new vote count
      console.log('Vote successful:', updatedPost);
    } catch (error) {
      console.error('Failed to vote:', error);
    }
  };

  return (
    <div className="post-list">
      {posts.map(post => (
        <div key={post.id} className="post">
          <h2>{post.title}</h2>
          <p>{post.content}</p>
          <p>Posted by: {post.author}</p>
          <div className="vote-buttons">
            <button onClick={() => handleVote(post.id, 'upvote')}>Upvote</button>
            <span>{post.voteCount}</span>
            <button onClick={() => handleVote(post.id, 'downvote')}>Downvote</button>
          </div>
        </div>
      ))}
    </div>
  );
};

export default PostList;
