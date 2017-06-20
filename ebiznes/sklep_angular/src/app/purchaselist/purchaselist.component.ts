import { Component, OnInit } from '@angular/core';
import {PurchaseService} from "../purchase/purchase.service";
import {ProductService} from "../product/product.service";
import {Purchase} from "../purchase/purchase";
import {Product} from "../product/product";
import {User} from "../user/user";
import {UserService} from "../user/user.service";

@Component({
  selector: 'app-purchaselist',
  templateUrl: './purchaselist.component.html',
  styleUrls: ['./purchaselist.component.css'],
  providers: [PurchaseService, ProductService, UserService]
})
export class PurchaselistComponent implements OnInit {

  purchases: Purchase[] = [];
  products: Product[] = [];
  users: User[] = [];

  constructor(private purchaseService: PurchaseService, private productService: ProductService, private userService: UserService)
  {
    this.productService.getProducts().subscribe(data => this.products = data.json() );
    this.userService.getUsers().subscribe(data => this.users = data.json() );
    this.purchaseService.getPurchases().subscribe(data => this.purchases = data.json() );
  }

  getUsername(userId: String)
  {
    for(var user of this.users)
    {
      if(user.userId == userId)
        return user.fullName;
    }
    return "---";
  }

  getProductList(jsonOrder: String)
  {
    var strVal= [];
    var order = JSON.parse(jsonOrder+"");
    for(var ord of order)
    {
      var prodId = ord.prodId;
      var number = ord.number;

      var productOrder = "";
      for(var product of this.products)
      {
        if(product.prodId == prodId)
        {
          productOrder= product.tytul;
          break;
        }
      }
      productOrder += " [ x"+number+" ]\n";
      strVal.push( productOrder);
    }
    return strVal;
  }

  deletePurchase(purchaseId: number)
  {
    var result = confirm("Do you really wish to delete order with id: "+purchaseId+" ?");
    this.purchaseService.deletePurchase(purchaseId);

    var index = this.purchases.findIndex(x => x.id==purchaseId);
    if (index > -1) {
      this.purchases.splice(index, 1);
    }
  }

  ngOnInit() {
  }

}
