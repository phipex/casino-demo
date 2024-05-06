import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOperator } from 'app/shared/model/operator.model';
import { getEntities as getOperators } from 'app/entities/operator/operator.reducer';
import { ICasino } from 'app/shared/model/casino.model';
import { getEntity, updateEntity, createEntity, reset } from './casino.reducer';

export const CasinoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const operators = useAppSelector(state => state.operator.entities);
  const casinoEntity = useAppSelector(state => state.casino.entity);
  const loading = useAppSelector(state => state.casino.loading);
  const updating = useAppSelector(state => state.casino.updating);
  const updateSuccess = useAppSelector(state => state.casino.updateSuccess);

  const handleClose = () => {
    navigate('/casino' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOperators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...casinoEntity,
      ...values,
      operator: operators.find(it => it.id.toString() === values.operator?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...casinoEntity,
          operator: casinoEntity?.operator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demoCasinoApp.casino.home.createOrEditLabel" data-cy="CasinoCreateUpdateHeading">
            Crear o editar Casino
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="casino-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Nit"
                id="casino-nit"
                name="nit"
                data-cy="nit"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Name"
                id="casino-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Direction"
                id="casino-direction"
                name="direction"
                data-cy="direction"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField id="casino-operator" name="operator" data-cy="operator" label="Operator" type="select">
                <option value="" key="0" />
                {operators
                  ? operators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/casino" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Volver</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Guardar
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CasinoUpdate;
