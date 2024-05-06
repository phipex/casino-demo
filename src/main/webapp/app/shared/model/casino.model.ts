import { IOperator } from 'app/shared/model/operator.model';

export interface ICasino {
  id?: number;
  nit?: string;
  name?: string;
  direction?: string;
  operator?: IOperator | null;
}

export const defaultValue: Readonly<ICasino> = {};
