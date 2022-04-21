import { ExtendedTopicDetails } from "./ExtendedTopicDetails";
import { ExtendedAnswerDetails } from './ExtendedAnswerDetails'

export interface ExtendedAppUser {
  role: string,
  nickName: string,
  createdDate: string,
  email: string,
  descriptionContent: string,
  avatarImage: string,
  backgroundImage: string,
  title: string,
  visitors: number,
  reputation: number,

  topics: ExtendedTopicDetails[],
  answers: ExtendedAnswerDetails[];
}
