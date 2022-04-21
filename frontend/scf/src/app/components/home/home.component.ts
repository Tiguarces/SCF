import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { LastAnswer } from 'src/app/models/LastAnswer';
import { LastTopic } from 'src/app/models/LastTopic';
import { TopicCategory } from 'src/app/models/TopicCategory';
import { AnswerService } from 'src/app/services/answer.service';
import { CategoryService } from 'src/app/services/category.service';
import { TopicService } from 'src/app/services/topic.service';
import { FetchAllTopicCategoriesResponse } from '../../models/response/FetchAllTopicCategoriesResponse'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public categories: TopicCategory[] | undefined;
  public lastTopics: LastTopic[] | undefined;
  public lastAnswers: LastAnswer[] | undefined;

  private readonly amountLastLimit: number = 2;

  constructor(private categoryService: CategoryService, private topicService: TopicService,
              private answerService: AnswerService) {

    categoryService.getTopicCategories().subscribe({
        next:
          (response: FetchAllTopicCategoriesResponse) => this.categories = response.categories,
        error:
          (error: any) => console.log(error)
      }
    );

    topicService.getLastTopics(this.amountLastLimit).subscribe({
      next:
        (topics: LastTopic[]) => this.prepareTopics(topics),
      error:
        (error: HttpErrorResponse) => console.log(error)
    });

    answerService.getLastAnswers(this.amountLastLimit).subscribe({
      next:
        (answers: LastAnswer[]) => this.prepareAnswers(answers),
      error:
        (error: HttpErrorResponse) => console.log(error)
    });
  }

  private prepareTopics(topics: LastTopic[]) {
    this.lastTopics = topics;
    topics.forEach(topic => {
      if(topic.topicName.length >= 35)
        topic.topicName = topic.topicName.substring(0, 32).concat("...");
    });
  }

  private prepareAnswers(answers: LastAnswer[]) {
    this.lastAnswers = answers;
    answers.forEach(answer => {
      if(answer.content.length >= 118)
        answer.content = answer.content.substring(0, 115).concat("...");
    })
  }

  ngOnInit(): void {
  }

}
