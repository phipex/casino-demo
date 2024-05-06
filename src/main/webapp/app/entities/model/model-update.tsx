import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IManufacturer } from 'app/shared/model/manufacturer.model';
import { getEntities as getManufacturers } from 'app/entities/manufacturer/manufacturer.reducer';
import { IModel } from 'app/shared/model/model.model';
import { getEntity, updateEntity, createEntity, reset } from './model.reducer';

export const ModelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const manufacturers = useAppSelector(state => state.manufacturer.entities);
  const modelEntity = useAppSelector(state => state.model.entity);
  const loading = useAppSelector(state => state.model.loading);
  const updating = useAppSelector(state => state.model.updating);
  const updateSuccess = useAppSelector(state => state.model.updateSuccess);

  const handleClose = () => {
    navigate('/model' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getManufacturers({}));
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
      ...modelEntity,
      ...values,
      manufacturer: manufacturers.find(it => it.id.toString() === values.manufacturer?.toString()),
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
          ...modelEntity,
          manufacturer: modelEntity?.manufacturer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demoCasinoApp.model.home.createOrEditLabel" data-cy="ModelCreateUpdateHeading">
            Crear o editar Model
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="model-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="model-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'Este campo es obligatorio.' },
                }}
              />
              <ValidatedField id="model-manufacturer" name="manufacturer" data-cy="manufacturer" label="Manufacturer" type="select">
                <option value="" key="0" />
                {manufacturers
                  ? manufacturers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/model" replace color="info">
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

export default ModelUpdate;
