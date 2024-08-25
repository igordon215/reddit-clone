import React, { useState } from 'react';
import { createSubreddit } from '../api';

const CreateSubreddit = ({ onSubredditCreated }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const newSubreddit = await createSubreddit({ name, description });
      onSubredditCreated(newSubreddit);
      setName('');
      setDescription('');
      setError('');
    } catch (err) {
      setError('Failed to create subreddit. Please try again.');
    }
  };

  return (
    <div className="create-subreddit">
      <h3>Create a New Subreddit</h3>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="Subreddit Name" required />
        <textarea value={description} onChange={e => setDescription(e.target.value)} placeholder="Description" required />
        <button type="submit">Create Subreddit</button>
      </form>
    </div>
  );
};

export default CreateSubreddit;
