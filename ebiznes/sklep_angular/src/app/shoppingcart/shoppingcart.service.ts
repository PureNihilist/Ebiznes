import { Injectable } from '@angular/core';
import {Headers, Http, RequestOptions} from "@angular/http";
import {CookieService} from "angular2-cookie/core";

@Injectable()
export class ShoppingCartService {

  numberOfProducts : number = 0;
  productsInBasket = [];

  constructor(private http: Http, private cookieService: CookieService)
  {
    this.receiveBasket();
  }

  addProduct(prodId: number)
  {
    var num= 0;
    if( prodId in this.productsInBasket)
    {
      num = this.productsInBasket[prodId];
    }
    num++;
    this.productsInBasket[prodId]= num;

    this.sendBasket();
    this.countBasket();
  }

  removeProduct(prodId: number)
  {
    var num= 0;
    if( prodId in this.productsInBasket)
    {
      num = this.productsInBasket[prodId];
    }
    if(num > 0)
      num--;
    this.productsInBasket[prodId]= num;

    this.sendBasket();
    this.countBasket();
  }

  removeProductAll(prodId: number)
  {
    this.productsInBasket[prodId]= 0;

    this.sendBasket();
    this.countBasket();
  }

  countBasket()
  {
    var counter= 0;
    for(var val of this.productsInBasket)
    {
      if(val > 0)
        counter += val;
    }
    this.numberOfProducts = counter;
  }

  basketToJson()
  {
    var arr= [];
    for(var index in this.productsInBasket)
    {
      var val = this.productsInBasket[index];
      if(val > 0)
      {
        var obj = {
          prodId: index,
          number: val
        }
        arr.push(obj);
      }
    }
    return JSON.stringify(arr);
  }

  sendBasket()
  {
    var jsonBasket= this.basketToJson();
    var cookieName = this.cookieService.get("userAuth");
    console.log(cookieName);
    var sendObj = {
      cartId: 0,
      userId: cookieName,
      products: jsonBasket
    }

    const serializedForm = JSON.stringify(sendObj);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.post('http://localhost:9000/shoppingcart', serializedForm, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );
  }

  receiveBasket()
  {
    var cookieName = this.cookieService.get("userAuth");

    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    this.http.get('http://localhost:9000/shoppingcart/'+cookieName).subscribe(data =>
    {
      var arr = data.json();
      if(arr.length == 0)
        return;

      var jsonStr = arr[0].products;
      var basketArr = JSON.parse(jsonStr);
      for(var basketObj of basketArr)
      {
        this.productsInBasket[basketObj.prodId]= basketObj.number;
      }
      this.countBasket();
    })
  }

  removeBasket()
  {
    var cookieName = this.cookieService.get("userAuth");

    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    this.http.delete('http://localhost:9000/shoppingcart/'+cookieName).subscribe(
      data => {
        //console.log('wyslane!', data)
      },
      error => console.error('nie bangla', error)
    );
  }

  clearBasket()
  {
    this.removeBasket();
    this.numberOfProducts= 0;
  }

}
