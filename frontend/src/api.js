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
    const response = await fetch(`${API_BASE_URL}/authenticate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password, rememberMe: true }),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
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

export const fetchSubreddit = async subredditName => {
  try {
    const response = await fetch(`${API_BASE_URL}/subreddits?name.equals=${subredditName}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const subreddits = await response.json();
    return subreddits[0]; // Assuming the API returns an array with one subreddit
  } catch (error) {
    console.error('Error fetching subreddit:', error);
    throw error;
  }
};

export const fetchPostsForSubreddit = async subredditId => {
  try {
    const response = await fetch(`${API_BASE_URL}/posts?subredditId.equals=${subredditId}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error(`Error fetching posts for subreddit ${subredditId}:`, error);
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
    console.log(`Voting on post ${postId} with vote type ${voteType}`);
    const response = await fetch(`${API_BASE_URL}/votes`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ postId, voteType }),
    });
    if (!response.ok) {
      const errorText = await response.text();
      console.error(`Error voting on post. Status: ${response.status}, Response: ${errorText}`);
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    console.log('Vote response:', data);
    return data;
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

export const fetchComments = async postId => {
  try {
    const response = await fetch(`${API_BASE_URL}/comments?postId.equals=${postId}`, {
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
