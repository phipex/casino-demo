import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Operator from './operator';
import Casino from './casino';
import Slot from './slot';
import Model from './model';
import Manufacturer from './manufacturer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="operator/*" element={<Operator />} />
        <Route path="casino/*" element={<Casino />} />
        <Route path="slot/*" element={<Slot />} />
        <Route path="model/*" element={<Model />} />
        <Route path="manufacturer/*" element={<Manufacturer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
