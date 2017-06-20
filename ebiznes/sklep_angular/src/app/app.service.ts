import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions} from "@angular/http";

@Injectable()
export class AppService {

  constructor(private http: Http) { }

  getLoggedUser(userId: String)
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/user/'+userId);
  }

  logout()
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/logout');
  }
}
