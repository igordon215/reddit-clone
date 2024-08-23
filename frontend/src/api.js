const API_BASE_URL = 'http://localhost:8080/api';

export const fetchPosts = async () => {
  const response = await fetch(`${API_BASE_URL}/posts`);
  return response.json();
};

export const fetchTidders = async () => {
  const response = await fetch(`${API_BASE_URL}/tidders`);
  return response.json();
};

export const fetchPostsForTidder = async tidderName => {
  const response = await fetch(`${API_BASE_URL}/tidders/${tidderName}/posts`);
  return response.json();
};

export const createPost = async postData => {
  const response = await fetch(`${API_BASE_URL}/posts`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(postData),
  });
  return response.json();
};

export const voteOnPost = async (postId, voteType) => {
  const response = await fetch(`${API_BASE_URL}/posts/${postId}/vote`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ voteType }),
  });
  return response.json();
};
