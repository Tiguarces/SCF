import { TopicDetails } from "./TopicDetails";

export interface LastTopic {
  userId: number,
  subCategoryName: string,
  avatarImageURL: string,
  userNickname: string,
  topicName: string,
  topicCategoryName: string,
  createdDate: Date;
  numberOfAnswers: number
}
