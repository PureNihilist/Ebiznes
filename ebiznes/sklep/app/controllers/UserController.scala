package controllers

import javax.inject.Inject

import dao.RegisteredUserDAO
import models.{ RegisteredUser, RegisteredUserREST }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }

class UserController @Inject() (registeredUserDAO: RegisteredUserDAO) extends Controller {
  def getAll = Action.async { implicit request =>
    registeredUserDAO.all map {
      categories => Ok(Json.toJson(categories))
    }
  }

  def deleteUser(userId: String) = Action { implicit request =>
    registeredUserDAO.deleteById(userId)
    Ok("OK")
  }

  def getByUserId(userId: String) = Action.async {
    implicit request =>
      registeredUserDAO.getByUserId(userId) map {
        usr => Ok(Json.toJson(usr))
      }
  }
}
