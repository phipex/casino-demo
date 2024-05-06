import dayjs from 'dayjs';
import { ICasino } from 'app/shared/model/casino.model';
import { IModel } from 'app/shared/model/model.model';

export interface ISlot {
  id?: number;
  idCasino?: number;
  serial?: string;
  nuc?: string;
  initialized?: dayjs.Dayjs;
  balance?: number | null;
  casino?: ICasino | null;
  model?: IModel | null;
}

export const defaultValue: Readonly<ISlot> = {};
