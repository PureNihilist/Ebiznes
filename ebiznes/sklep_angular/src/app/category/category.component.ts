import { Component, OnInit } from '@angular/core';
import {CategoryService} from './category.service';
import {Category} from "./category";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css'],
  providers: [CategoryService]
})

export class CategoryComponent implements OnInit {

  categoryForm: FormGroup;
  categories: Category[];
  editCategoryId = -1;

  constructor(private categoryService: CategoryService)
  {
    this.categoryForm = new FormGroup({
      nazwa: new FormControl('', Validators.required)
    });

    this.categoryService.getCategories().subscribe( data => this.categories = data.json());

    this.categoryService.newCategoryEvent.subscribe( data => this.categories.push(data) );
  }

  addCategory(event)
  {
    var catObj = this.categoryForm.value;
    if(this.categoryForm.value.nazwa.length == 0)
      return;
    catObj.catId= 0;

    this.categoryService.newCategory(catObj);
    this.categoryForm.reset();
  }

  removeCategory(id: number)
  {
    var result = confirm("Na pewno chcesz usunąć kategorie o id: "+id+" ?");
    if (result)
    {
      this.categoryService.deleteCategory(id);
      var index = this.categories.findIndex(x => x.catId==id);
      if (index > -1) {
        this.categories.splice(index, 1);
      }
    }
  }

  editCategory(id: number)
  {
    this.editCategoryId= id;
  }

  updateCategory(id: number)
  {
    var newNazwa = $("#nazwaEdit").val();
    if(newNazwa.length == 0)
      return;
    var cat = new Category()
    cat.catId= id;
    cat.nazwa= newNazwa;

    this.categoryService.updateCategory(cat);
    var index = this.categories.findIndex(x => x.catId==id);
    if (index > -1) {
      this.categories[index]= cat;
    }
    this.editCategoryId= -1;
  }

  ngOnInit()
  {
  }

}
