import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Headers, Http, RequestOptions} from "@angular/http";
import {Location} from "@angular/common";
import {CookieService} from "angular2-cookie/core";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  constructor(public activatedRoute: ActivatedRoute, private cookieService: CookieService, private router: Router, private location:Location,)
  {
    let params: any = this.activatedRoute.snapshot.queryParams["auth"];
    if(params === undefined)
    {
    }
    else {
      this.cookieService.put("userAuth", params);
      //this.location.replaceState('');
      this.router.navigate(['list']);
      window.location.reload();
    }
    //console.log(params);
  }

  ngOnInit() {
    //let param1 = this.activatedRoute.snapshot.queryParams["auth"];
    //console.log(param1);
  }

}
