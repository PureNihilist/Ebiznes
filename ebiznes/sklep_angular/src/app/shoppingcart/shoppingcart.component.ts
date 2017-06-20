import { Component, OnInit } from '@angular/core';
import { ShoppingCartService} from "./shoppingcart.service";
import {Product} from "../product/product";
import {ProductService} from "../product/product.service";

@Component({
  selector: 'app-basket',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.css'],
  providers: [ProductService]
})
export class ShoppingCartComponent implements OnInit {

  products: Product[] = [];

  constructor(private shoppingcartService: ShoppingCartService, private productService: ProductService)
  {
    this.productService.getProducts().subscribe(data => this.products= data.json() );
  }

  numberInBasket(prodId: number)
  {
    var val = this.shoppingcartService.productsInBasket[prodId];
    if(val > 0)
      return val;
    return 0;
  }

  priceForProduct(prodId: number)
  {
    var price= 0;
    for(var prod of this.products)
    {
      if(prod.prodId == prodId)
      {
        price= prod.cena;
        break;
      }
    }
    return (price * this.numberInBasket(prodId)).toFixed(2);
  }

  sumOfPrice()
  {
    var price= 0;
    for(var prod of this.products)
    {

        price += prod.cena*this.numberInBasket(prod.prodId);

    }
    return price.toFixed(2);
  }

  ngOnInit() {
  }

}
