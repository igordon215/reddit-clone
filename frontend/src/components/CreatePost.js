import React, { useState } from 'react';
import { createPost } from '../api';

const CreatePost = ({ tidderName, onPostCreated }) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const newPost = await createPost({ title, content, tidderName });
      onPostCreated(newPost);
      setTitle('');
      setContent('');
    } catch (error) {
      console.error('Failed to create post:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="create-post">
      <h3>Create a new post</h3>
      <input type="text" value={title} onChange={e => setTitle(e.target.value)} placeholder="Title" required />
      <textarea value={content} onChange={e => setContent(e.target.value)} placeholder="Content" required />
      <button type="submit">Create Post</button>
    </form>
  );
};

export default CreatePost;
