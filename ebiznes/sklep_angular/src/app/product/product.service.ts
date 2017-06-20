import {EventEmitter, Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {Product} from "./product";

@Injectable()
export class ProductService
{

  newProductEvent: EventEmitter<Product> = new EventEmitter<Product>();

  constructor(private http: Http)
  {
  }

  getProducts()
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    const options = new RequestOptions({headers: headers});

    return this.http.get('http://localhost:9000/product');
  // <Product[]> = result

  }

  newProduct(formData) {
    const serializedForm = JSON.stringify(formData);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.post('http://localhost:9000/product', serializedForm, options)
      .subscribe(
        data =>
        {
          this.newProductEvent.emit(data.json())
        },
        error => console.error('nie bangla', error)
      );
  }

  updateProduct(product: Product)
  {
    const serializedForm = JSON.stringify(product);
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.put('http://localhost:9000/product', serializedForm, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );
  }

  deleteProduct(id: number)
  {
    const headers: Headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');
    const options = new RequestOptions({headers: headers});
    this.http.delete('http://localhost:9000/product/'+id, options)
      .subscribe(
        data => {
          //console.log('wyslane!', data)
        },
        error => console.error('nie bangla', error)
      );
  }

}
