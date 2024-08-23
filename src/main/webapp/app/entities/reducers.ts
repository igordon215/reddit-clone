import subreddit from 'app/entities/subreddit/subreddit.reducer';
import post from 'app/entities/post/post.reducer';
import comment from 'app/entities/comment/comment.reducer';
import vote from 'app/entities/vote/vote.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  subreddit,
  post,
  comment,
  vote,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
