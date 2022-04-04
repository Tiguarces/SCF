import { Component, OnInit } from '@angular/core';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  categories: any;

  constructor(private categoryService: CategoryService) {
    categoryService.getAllNames().subscribe(data => this.categories = data);
  }

  ngOnInit(): void {

  }

}
