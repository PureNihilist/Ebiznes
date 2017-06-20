package controllers

import javax.inject.Inject

import dao.ShoppingCartDAO
import models.{ ShoppingCart, ShoppingCartREST }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import scala.concurrent.ExecutionContext.Implicits.global

class ShoppingCartController @Inject() (basketDAO: ShoppingCartDAO) extends Controller {

  def getById(userId: String) = Action.async { implicit request =>
    basketDAO.getBasketByUserId(userId) map {
      baskets => Ok(Json.toJson(baskets))
    }
  }

  def newBasket = Action.async { implicit request =>
    var json: ShoppingCartREST = request.body.asJson.get.as[ShoppingCartREST]
    var basket = ShoppingCart(cartId = 0, userId = json.userId, products = json.products)
    var basketResult = basketDAO.insert(basket)
    basketResult.map {
      basket => Ok(Json.toJson(basket))
    }
  }

  def deleteBasket(userId: String) = Action { implicit request =>
    basketDAO.deleteByUserId(userId)
    Ok("OK")
  }
}
