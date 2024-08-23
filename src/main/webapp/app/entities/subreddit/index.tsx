import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subreddit from './subreddit';
import SubredditDetail from './subreddit-detail';
import SubredditUpdate from './subreddit-update';
import SubredditDeleteDialog from './subreddit-delete-dialog';

const SubredditRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Subreddit />} />
    <Route path="new" element={<SubredditUpdate />} />
    <Route path=":id">
      <Route index element={<SubredditDetail />} />
      <Route path="edit" element={<SubredditUpdate />} />
      <Route path="delete" element={<SubredditDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubredditRoutes;
