import { IManufacturer } from 'app/shared/model/manufacturer.model';

export interface IModel {
  id?: number;
  name?: string;
  manufacturer?: IManufacturer | null;
}

export const defaultValue: Readonly<IModel> = {};
