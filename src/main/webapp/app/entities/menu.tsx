import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/operator">
        Operator
      </MenuItem>
      <MenuItem icon="asterisk" to="/casino">
        Casino
      </MenuItem>
      <MenuItem icon="asterisk" to="/slot">
        Slot
      </MenuItem>
      <MenuItem icon="asterisk" to="/model">
        Model
      </MenuItem>
      <MenuItem icon="asterisk" to="/manufacturer">
        Manufacturer
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
