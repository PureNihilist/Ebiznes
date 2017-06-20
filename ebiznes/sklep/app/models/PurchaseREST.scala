package models

import play.api.libs.json.Json

case class PurchaseREST(id: Long, userId: String, adress: String, sendType: String, jsonOrder: String, totalPrice: Double, date: String)

object PurchaseREST {
  implicit val productFormat = Json.format[PurchaseREST]
}

