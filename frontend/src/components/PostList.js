import React from 'react';

const PostList = ({ posts }) => {
  return (
    <div className="post-list">
      {posts.map(post => (
        <div key={post.id} className="post">
          <h2>{post.title}</h2>
          <p>{post.content}</p>
          <p>Posted by: {post.author}</p>
          <p>Votes: {post.voteCount}</p>
        </div>
      ))}
    </div>
  );
};

export default PostList;
