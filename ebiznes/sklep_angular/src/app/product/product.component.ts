import { Component, OnInit } from '@angular/core';
import {Product} from './product';
import {ProductService} from './product.service';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {Category} from "../category/category";
import {CategoryService} from "../category/category.service";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [ProductService, CategoryService]
})
export class ProductComponent implements OnInit {

  productForm: FormGroup;

  products: Product[]= [];
  categories: Category[] =[];
  editProductId: number = -1;

  constructor(private productService: ProductService, private categoryService: CategoryService, private route: ActivatedRoute) {

    this.productForm = new FormGroup({
      tytul: new FormControl('', Validators.required),
      img: new FormControl("", Validators.required),
      catId: new FormControl("", Validators.required),
      cena: new FormControl(0, Validators.required),
      opis: new FormControl("", Validators.required)
    });

    this.categoryService.getCategories().subscribe( data => this.categories = data.json() );
    this.productService.getProducts().subscribe( data => this.products = data.json());
    this.productService.newProductEvent.subscribe( data =>
    {
      this.products.push(data);
      this.productForm.reset();
    } );

  }

  ngOnInit()
  {
  }

  addProduct(event)
  {
    var prodObj = this.productForm.value;

    prodObj.prodId= 0;
    prodObj.cena= parseFloat(this.productForm.value.cena);
    prodObj.catId= parseInt(this.productForm.value.catId);

    this.productService.newProduct(this.productForm.value);
  }

  getCategoryName(catId: number)
  {
    for(var cat of this.categories)
    {
      if(cat.catId == catId)
        return cat.nazwa;
    }

    return "---";
  }

  editProduct(id: number)
  {
    this.editProductId= id;
  }

  updateProduct(id: number)
  {
    var newNazwa = $("#nazwaEdit").val();
    if(newNazwa.length == 0)
      return;
    var newCena = $("#cenaEdit").val();
    if( !(newCena > 0) )
      return;

    var product : Product = new Product;
    product.prodId = id;
    product.tytul = newNazwa;
    product.cena= parseFloat(newCena);
    product.opis= $("#opisEdit").val();
    product.img= $("#imgEdit").val();
    product.catId= parseInt($("#catEdit").val());

    this.productService.updateProduct(product);
    var index = this.products.findIndex(x => x.prodId==id);
    if (index > -1) {
      this.products[index]= product;
    }
    this.editProductId= -1;
  }

  removeProduct(id: number)
  {
    var result = confirm("Na pewno chcesz usunąć produkt o id: "+id+" ?");
    if (result)
    {
      this.productService.deleteProduct(id);
      var index = this.products.findIndex(x => x.prodId==id);
      if (index > -1) {
        this.products.splice(index, 1);
      }
    }
  }
}
