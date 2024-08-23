import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subreddit.reducer';

export const SubredditDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subredditEntity = useAppSelector(state => state.subreddit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subredditDetailsHeading">Subreddit</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{subredditEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{subredditEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{subredditEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/subreddit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subreddit/${subredditEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubredditDetail;
