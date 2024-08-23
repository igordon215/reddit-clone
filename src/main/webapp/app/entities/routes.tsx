import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subreddit from './subreddit';
import Post from './post';
import Comment from './comment';
import Vote from './vote';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="subreddit/*" element={<Subreddit />} />
        <Route path="post/*" element={<Post />} />
        <Route path="comment/*" element={<Comment />} />
        <Route path="vote/*" element={<Vote />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
