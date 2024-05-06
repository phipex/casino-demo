import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Operator from './operator';
import OperatorDetail from './operator-detail';
import OperatorUpdate from './operator-update';
import OperatorDeleteDialog from './operator-delete-dialog';

const OperatorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Operator />} />
    <Route path="new" element={<OperatorUpdate />} />
    <Route path=":id">
      <Route index element={<OperatorDetail />} />
      <Route path="edit" element={<OperatorUpdate />} />
      <Route path="delete" element={<OperatorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OperatorRoutes;
