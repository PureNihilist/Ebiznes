import { Component, OnInit } from '@angular/core';
import {ProductService} from "../product/product.service";
import {Product} from "../product/product";
import {ShoppingCartService} from "../shoppingcart/shoppingcart.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PurchaseService} from "./purchase.service";
import {Purchase} from "./purchase";
import {CookieService} from "angular2-cookie/core";

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.css'],
  providers: [ProductService, PurchaseService]
})
export class PurchaseComponent implements OnInit {

  products: Product[] = [];
  purchaseForm: FormGroup;
  sendTypeIndex= 0;

  sendTypes = [
    {name: "Post Office", cost: 8},
    {name: "Inpost", cost: 7},
    {name: "DHL courier", cost: 15}
  ]

  constructor(private shoppingcartService: ShoppingCartService, private productService: ProductService, private purchaseService: PurchaseService, private cookieService: CookieService)
  {
    this.purchaseForm = new FormGroup({
      adress: new FormControl('', Validators.required)
    });

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

  sendPrice()
  {
    return this.sendTypes[this.sendTypeIndex].cost.toFixed(2);
  }

  totalPrice()
  {
    return (parseFloat(this.sumOfPrice()+"") + parseFloat(this.sendPrice()+"")).toFixed(2);
  }

  chooseSendType(index: number)
  {
    this.sendTypeIndex = index;
  }

  addPurchase(event)
  {
    if(this.shoppingcartService.numberOfProducts == 0)
      return;

    this.disableAll();
    var cookieName = this.cookieService.get("userAuth");
    var adress= $("#adress").val();

    var today = new Date();
    var dd = parseInt(today.getDate()+"");
    var dd2 = (dd < 10) ? "0"+dd : dd;
    var mm = parseInt(today.getMonth()+"")+1; //January is 0!
    var mm2 = (mm < 10) ? "0"+mm : mm;
    var yyyy = parseInt(today.getFullYear()+"");

    var newPurchase: Purchase = new Purchase;
    newPurchase.id= 0;
    newPurchase.userId= cookieName;
    newPurchase.adress= adress;
    newPurchase.date= yyyy+"."+mm2+"."+dd2;
    newPurchase.jsonOrder= this.shoppingcartService.basketToJson();
    newPurchase.totalPrice= parseFloat(this.totalPrice()+"");
    newPurchase.sendType= this.sendTypes[this.sendTypeIndex].name;

    this.purchaseService.newPurchase(newPurchase);
    this.shoppingcartService.clearBasket();

    $("#addedInfo").css("display", "inline");
  }

  disableAll()
  {
    $("input").attr("disabled", "disabled");
    $("textarea").attr("disabled", "disabled");
    $("#purchaseBtn").attr("disabled", "disabled");
  }

  ngOnInit() {
  }

}
