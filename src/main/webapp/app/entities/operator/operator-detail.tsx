import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './operator.reducer';

export const OperatorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const operatorEntity = useAppSelector(state => state.operator.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="operatorDetailsHeading">Operator</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{operatorEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{operatorEntity.name}</dd>
          <dt>
            <span id="nit">Nit</span>
          </dt>
          <dd>{operatorEntity.nit}</dd>
          <dt>
            <span id="contract">Contract</span>
          </dt>
          <dd>{operatorEntity.contract}</dd>
        </dl>
        <Button tag={Link} to="/operator" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/operator/${operatorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OperatorDetail;
