import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './casino.reducer';

export const CasinoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const casinoEntity = useAppSelector(state => state.casino.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="casinoDetailsHeading">Casino</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{casinoEntity.id}</dd>
          <dt>
            <span id="nit">Nit</span>
          </dt>
          <dd>{casinoEntity.nit}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{casinoEntity.name}</dd>
          <dt>
            <span id="direction">Direction</span>
          </dt>
          <dd>{casinoEntity.direction}</dd>
          <dt>Operator</dt>
          <dd>{casinoEntity.operator ? casinoEntity.operator.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/casino" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/casino/${casinoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CasinoDetail;
