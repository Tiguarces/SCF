import { AnswerDetails } from "./AnswerDetails";

export interface ExtendedAnswerDetails {
  answer: AnswerDetails,
  topicName: string,
  subCategoryName: string,
  categoryName: string;
}
