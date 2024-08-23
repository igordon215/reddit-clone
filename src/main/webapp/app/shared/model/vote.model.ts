import { IUser } from 'app/shared/model/user.model';
import { IPost } from 'app/shared/model/post.model';

export interface IVote {
  id?: number;
  voteType?: string;
  user?: IUser | null;
  post?: IPost | null;
}

export const defaultValue: Readonly<IVote> = {};
