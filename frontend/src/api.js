const API_BASE_URL = 'http://localhost:8080/api';

let authToken = localStorage.getItem('authToken');

const setAuthToken = token => {
  authToken = token;
  localStorage.setItem('authToken', token);
};

const clearAuthToken = () => {
  authToken = null;
  localStorage.removeItem('authToken');
};

const getHeaders = () => {
  const headers = {
    'Content-Type': 'application/json',
  };
  if (authToken) {
    headers['Authorization'] = `Bearer ${authToken}`;
  }
  return headers;
};

export const login = async (username, password) => {
  try {
    console.log('Attempting login with:', { username, password });
    const response = await fetch(`${API_BASE_URL}/authenticate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password, rememberMe: true }),
    });
    console.log('Login response status:', response.status);
    console.log('Login response headers:', response.headers);
    const responseText = await response.text();
    console.log('Login response text:', responseText);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}, response: ${responseText}`);
    }

    const data = JSON.parse(responseText);
    console.log('Login successful, received data:', data);
    setAuthToken(data.id_token);
    return data;
  } catch (error) {
    console.error('Error logging in:', error);
    throw error;
  }
};

export const logout = () => {
  clearAuthToken();
};

export const fetchPosts = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching posts:', error);
    throw error;
  }
};

export const fetchSubreddits = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/subreddits`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching subreddits:', error);
    throw error;
  }
};

export const fetchPostsForSubreddit = async subredditName => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts?subredditName=${subredditName}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error(`Error fetching posts for subreddit ${subredditName}:`, error);
    throw error;
  }
};

export const createPost = async postData => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(postData),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error creating post:', error);
    throw error;
  }
};

export const voteOnPost = async (postId, voteType) => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts/${postId}/vote`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ voteType }),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error voting on post:', error);
    throw error;
  }
};

export const fetchPost = async postId => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts/${postId}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching post:', error);
    throw error;
  }
};

export const fetchComments = async postId => {
  try {
    const response = await fetch(`${API_BASE_URL}/comments?postId=${postId}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching comments:', error);
    throw error;
  }
};

export const createComment = async (postId, content) => {
  try {
    const response = await fetch(`${API_BASE_URL}/comments`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ postId, content }),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error creating comment:', error);
    throw error;
  }
};

export const createSubreddit = async subredditData => {
  try {
    const response = await fetch(`${API_BASE_URL}/subreddits`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(subredditData),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error creating subreddit:', error);
    throw error;
  }
};
