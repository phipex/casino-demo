import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICasino } from 'app/shared/model/casino.model';
import { getEntities as getCasinos } from 'app/entities/casino/casino.reducer';
import { IModel } from 'app/shared/model/model.model';
import { getEntities as getModels } from 'app/entities/model/model.reducer';
import { ISlot } from 'app/shared/model/slot.model';
import { getEntity, updateEntity, createEntity, reset } from './slot.reducer';

export const SlotUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const casinos = useAppSelector(state => state.casino.entities);
  const models = useAppSelector(state => state.model.entities);
  const slotEntity = useAppSelector(state => state.slot.entity);
  const loading = useAppSelector(state => state.slot.loading);
  const updating = useAppSelector(state => state.slot.updating);
  const updateSuccess = useAppSelector(state => state.slot.updateSuccess);

  const handleClose = () => {
    navigate('/slot' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCasinos({}));
    dispatch(getModels({}));
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
    if (values.idCasino !== undefined && typeof values.idCasino !== 'number') {
      values.idCasino = Number(values.idCasino);
    }
    values.initialized = convertDateTimeToServer(values.initialized);
    if (values.balance !== undefined && typeof values.balance !== 'number') {
      values.balance = Number(values.balance);
    }

    const entity = {
      ...slotEntity,
      ...values,
      casino: casinos.find(it => it.id.toString() === values.casino?.toString()),
      model: models.find(it => it.id.toString() === values.model?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          initialized: displayDefaultDateTime(),
        }
      : {
          ...slotEntity,
          initialized: convertDateTimeFromServer(slotEntity.initialized),
          casino: slotEntity?.casino?.id,
          model: slotEntity?.model?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demoCasinoApp.slot.home.createOrEditLabel" data-cy="SlotCreateUpdateHeading">
            Crear o editar Slot
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="slot-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Id Casino"
                id="slot-idCasino"
                name="idCasino"
                data-cy="idCasino"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                  validate: v => isNumber(v) || 'Este campo debe ser un número.',
                }}
              />
              <ValidatedField
                label="Serial"
                id="slot-serial"
                name="serial"
                data-cy="serial"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Nuc"
                id="slot-nuc"
                name="nuc"
                data-cy="nuc"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField
                label="Initialized"
                id="slot-initialized"
                name="initialized"
                data-cy="initialized"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField label="Balance" id="slot-balance" name="balance" data-cy="balance" type="text" />
              <ValidatedField id="slot-casino" name="casino" data-cy="casino" label="Casino" type="select">
                <option value="" key="0" />
                {casinos
                  ? casinos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="slot-model" name="model" data-cy="model" label="Model" type="select">
                <option value="" key="0" />
                {models
                  ? models.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/slot" replace color="info">
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

export default SlotUpdate;
