export interface IEntitya {
  id?: number;
  name?: string;
}

export class Entitya implements IEntitya {
  constructor(public id?: number, public name?: string) {}
}
