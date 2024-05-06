import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Slot from './slot';
import SlotDetail from './slot-detail';
import SlotUpdate from './slot-update';
import SlotDeleteDialog from './slot-delete-dialog';

const SlotRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Slot />} />
    <Route path="new" element={<SlotUpdate />} />
    <Route path=":id">
      <Route index element={<SlotDetail />} />
      <Route path="edit" element={<SlotUpdate />} />
      <Route path="delete" element={<SlotDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SlotRoutes;
