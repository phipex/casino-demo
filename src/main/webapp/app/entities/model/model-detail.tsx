import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './model.reducer';

export const ModelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const modelEntity = useAppSelector(state => state.model.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="modelDetailsHeading">Model</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{modelEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{modelEntity.name}</dd>
          <dt>Manufacturer</dt>
          <dd>{modelEntity.manufacturer ? modelEntity.manufacturer.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/model" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/model/${modelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ModelDetail;
