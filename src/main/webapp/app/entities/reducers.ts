import operator from 'app/entities/operator/operator.reducer';
import casino from 'app/entities/casino/casino.reducer';
import slot from 'app/entities/slot/slot.reducer';
import model from 'app/entities/model/model.reducer';
import manufacturer from 'app/entities/manufacturer/manufacturer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  operator,
  casino,
  slot,
  model,
  manufacturer,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
