import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vote.reducer';

export const VoteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const voteEntity = useAppSelector(state => state.vote.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="voteDetailsHeading">Vote</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{voteEntity.id}</dd>
          <dt>
            <span id="voteType">Vote Type</span>
          </dt>
          <dd>{voteEntity.voteType}</dd>
          <dt>User</dt>
          <dd>{voteEntity.user ? voteEntity.user.login : ''}</dd>
          <dt>Post</dt>
          <dd>{voteEntity.post ? voteEntity.post.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/vote" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vote/${voteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VoteDetail;
