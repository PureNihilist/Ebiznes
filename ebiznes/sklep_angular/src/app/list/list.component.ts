import { Component, OnInit } from '@angular/core';
import {ProductService} from "../product/product.service";
import {CategoryService} from "../category/category.service";
import {Product} from "../product/product";
import {Category} from "../category/category";
import {ShoppingCartService} from "../shoppingcart/shoppingcart.service";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css'],
  providers: [ProductService, CategoryService]
})
export class ListComponent implements OnInit {

  products: Product[]= [];
  categories: Category[] = [];

  constructor(private productService: ProductService, private categoryService: CategoryService, public shoppingcartService: ShoppingCartService)
  {
    this.productService.getProducts().subscribe(data => this.products= data.json());
    this.categoryService.getCategories().subscribe(data => this.categories= data.json());

  }

  addToBasket(prodId: number)
  {
    this.shoppingcartService.addProduct(prodId);
  }

  ngOnInit() {
  }


}
