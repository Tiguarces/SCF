import { TopicCategory } from '../TopicCategory';
import { UniversalResponse } from './UniversalResponse';

export interface FetchAllTopicCategoriesResponse extends UniversalResponse {
  categories: TopicCategory[];
}
