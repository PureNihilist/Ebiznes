package controllers

import javax.inject.Inject

import dao.PurchaseDAO
import models.{ Purchase, PurchaseREST }
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }

import scala.concurrent.ExecutionContext.Implicits.global

class PurchaseController @Inject() (purchaseDAO: PurchaseDAO) extends Controller {

  def getAll = Action.async { implicit request =>
    purchaseDAO.all map {
      purchases => Ok(Json.toJson(purchases))
    }
  }

  def newPurchase = Action.async { implicit request =>
    var json: PurchaseREST = request.body.asJson.get.as[PurchaseREST]
    var purchase = Purchase(id = json.id, userId = json.userId, adress = json.adress, sendType = json.sendType, jsonOrder = json.jsonOrder, totalPrice = json.totalPrice, date = json.date)
    var purchaseResult = purchaseDAO.insert(purchase)
    purchaseResult.map {
      pur => Ok(Json.toJson(pur))
    }

  }

  def deletePurchase(id: Long) = Action { implicit request =>
    purchaseDAO.deleteById(id)
    Ok("OK")
  }
}
