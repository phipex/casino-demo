import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Manufacturer from './manufacturer';
import ManufacturerDetail from './manufacturer-detail';
import ManufacturerUpdate from './manufacturer-update';
import ManufacturerDeleteDialog from './manufacturer-delete-dialog';

const ManufacturerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Manufacturer />} />
    <Route path="new" element={<ManufacturerUpdate />} />
    <Route path=":id">
      <Route index element={<ManufacturerDetail />} />
      <Route path="edit" element={<ManufacturerUpdate />} />
      <Route path="delete" element={<ManufacturerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ManufacturerRoutes;
