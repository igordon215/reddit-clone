export interface ISubreddit {
  id?: number;
  name?: string;
  description?: string | null;
}

export const defaultValue: Readonly<ISubreddit> = {};
