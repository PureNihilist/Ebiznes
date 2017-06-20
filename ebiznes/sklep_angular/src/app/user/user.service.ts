import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions} from "@angular/http";

@Injectable()
export class UserService {

  constructor(public http: Http) { }

  getUsers()
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/registereduser');
  }

  deleteProduct(id: string)
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.delete('http://localhost:9000/registereduser/'+id, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );
  }

}
