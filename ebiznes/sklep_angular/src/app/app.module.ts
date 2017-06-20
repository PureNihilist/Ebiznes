import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { ProductComponent } from './product/product.component';
import {RouterModule} from '@angular/router';
import { CategoryComponent } from './category/category.component';

import * as $ from 'jquery';
import { ListComponent } from './list/list.component';
import { UserComponent } from './user/user.component';
import { LoginComponent } from './login/login.component';
import { ShoppingCartComponent } from './shoppingcart/shoppingcart.component';
import { ShoppingCartService } from "./shoppingcart/shoppingcart.service";
import { CookieService } from 'angular2-cookie/services/cookies.service';
import { PurchaseComponent } from './purchase/purchase.component';
import { PurchaselistComponent } from './purchaselist/purchaselist.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductComponent,
    CategoryComponent,
    ListComponent,
    UserComponent,
    LoginComponent,
    ShoppingCartComponent,
    PurchaseComponent,
    PurchaselistComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    RouterModule.forRoot([
      {path: '', component: LoginComponent},
      {path: 'products', component: ProductComponent },
      {path: 'categories', component: CategoryComponent },
      {path: 'users', component: UserComponent },
      {path: 'list', component: ListComponent },
      {path: 'shoppingcart', component: ShoppingCartComponent },
      {path: 'purchase', component: PurchaseComponent },
      {path: 'purchaselist', component: PurchaselistComponent}

    ])
  ],
  providers: [ShoppingCartService, CookieService],
  bootstrap: [AppComponent]
})

export class AppModule { }
