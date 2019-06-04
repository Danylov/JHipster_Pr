export interface IEntityb {
  id?: number;
  name?: string;
}

export class Entityb implements IEntityb {
  constructor(public id?: number, public name?: string) {}
}
