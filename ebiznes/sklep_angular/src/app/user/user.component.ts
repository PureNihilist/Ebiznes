import { Component, OnInit } from '@angular/core';
import {UserService} from "./user.service";
import {User} from "./user";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
  providers: [UserService]
})
export class UserComponent implements OnInit {

  users: User[] = [];

  constructor(private userService: UserService)
  {
    this.userService.getUsers().subscribe( data => this.users = data.json() );
  }

  removeUser(userId: string)
  {
    var result = confirm("Do you really wish to delete user with id: "+userId+" ?");
    if (result) {
      this.userService.deleteProduct(userId);
      var index = this.users.findIndex(x => x.userId == userId);
      if (index > -1) {
        this.users.splice(index, 1);
      }
    }
  }

  ngOnInit() {
  }

}
