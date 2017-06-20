import { Injectable } from '@angular/core';
import {Purchase} from "./purchase";
import {Headers, Http, RequestOptions} from "@angular/http";

@Injectable()
export class PurchaseService {

  constructor(private http: Http) { }

  newPurchase(purchase: Purchase)
  {
    const serializedForm = JSON.stringify(purchase);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.post('http://localhost:9000/purchase', serializedForm, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );
  }

  getPurchases()
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/purchase');
  }

  deletePurchase(purchaseId: number)
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});
    this.http.delete('http://localhost:9000/purchase/'+purchaseId).subscribe(
      data => {
        //console.log('wyslane!', data)
      },
      error => console.error('nie bangla', error)
    );
  }
}
