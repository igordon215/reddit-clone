import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ISubreddit } from 'app/shared/model/subreddit.model';

export interface IPost {
  id?: number;
  title?: string;
  content?: string | null;
  createdDate?: dayjs.Dayjs | null;
  user?: IUser | null;
  subreddit?: ISubreddit | null;
}

export const defaultValue: Readonly<IPost> = {};
