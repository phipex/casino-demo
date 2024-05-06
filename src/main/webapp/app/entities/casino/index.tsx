import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Casino from './casino';
import CasinoDetail from './casino-detail';
import CasinoUpdate from './casino-update';
import CasinoDeleteDialog from './casino-delete-dialog';

const CasinoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Casino />} />
    <Route path="new" element={<CasinoUpdate />} />
    <Route path=":id">
      <Route index element={<CasinoDetail />} />
      <Route path="edit" element={<CasinoUpdate />} />
      <Route path="delete" element={<CasinoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CasinoRoutes;
