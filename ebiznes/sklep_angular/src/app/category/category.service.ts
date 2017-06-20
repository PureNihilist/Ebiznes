import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {EventEmitter} from "@angular/forms/src/facade/async";
import {Category} from "./category";

@Injectable()
export class CategoryService {

  newCategoryEvent : EventEmitter<Category> = new EventEmitter<Category>();

  constructor(private http: Http) { }

  getCategories()
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/category');
  }

  newCategory(formData) {
    const serializedForm = JSON.stringify(formData);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.post('http://localhost:9000/category', serializedForm, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
          this.newCategoryEvent.emit(data.json())
        },
        error => console.error('nie bangla', error)
      );
  }

  deleteCategory(id: number)
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.delete('http://localhost:9000/category/'+id, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)

        },
        error => console.error('nie bangla', error)
      );
  }

  updateCategory(cat: Category)
  {
    const serializedForm = JSON.stringify(cat);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.put('http://localhost:9000/category', serializedForm, options)
      .subscribe(
        data => {
          console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );

  }

}
