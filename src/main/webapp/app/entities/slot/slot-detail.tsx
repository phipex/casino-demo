import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './slot.reducer';

export const SlotDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const slotEntity = useAppSelector(state => state.slot.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="slotDetailsHeading">Slot</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{slotEntity.id}</dd>
          <dt>
            <span id="idCasino">Id Casino</span>
          </dt>
          <dd>{slotEntity.idCasino}</dd>
          <dt>
            <span id="serial">Serial</span>
          </dt>
          <dd>{slotEntity.serial}</dd>
          <dt>
            <span id="nuc">Nuc</span>
          </dt>
          <dd>{slotEntity.nuc}</dd>
          <dt>
            <span id="initialized">Initialized</span>
          </dt>
          <dd>{slotEntity.initialized ? <TextFormat value={slotEntity.initialized} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="balance">Balance</span>
          </dt>
          <dd>{slotEntity.balance}</dd>
          <dt>Casino</dt>
          <dd>{slotEntity.casino ? slotEntity.casino.name : ''}</dd>
          <dt>Model</dt>
          <dd>{slotEntity.model ? slotEntity.model.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/slot" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Volver</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/slot/${slotEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SlotDetail;
