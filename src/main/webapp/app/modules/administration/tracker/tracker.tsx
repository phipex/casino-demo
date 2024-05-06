import React from 'react';

import { useAppSelector } from 'app/config/store';

export const TrackerPage = () => {
  const activities = useAppSelector(state => state.administration.tracker.activities);

  return (
    <div>
      <h2 data-cy="trackerPageHeading">Actividades del usuario en tiempo real</h2>
      <table className="table table-sm table-striped table-bordered" data-cy="trackerTable">
        <thead>
          <tr>
            <th>
              <span>Usuario</span>
            </th>
            <th>
              <span>Dirección IP</span>
            </th>
            <th>
              <span>Página actual</span>
            </th>
            <th>
              <span>Tiempo</span>
            </th>
          </tr>
        </thead>
        <tbody>
          {activities.map((activity, i) => (
            <tr key={`log-row-${i}`}>
              <td>{activity.userLogin}</td>
              <td>{activity.ipAddress}</td>
              <td>{activity.page}</td>
              <td>{activity.time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TrackerPage;
