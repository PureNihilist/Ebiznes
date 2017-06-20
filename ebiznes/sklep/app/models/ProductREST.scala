package models

import play.api.libs.json.Json

case class ProductREST(prodId: Long, tytul: String, opis: String, img: String, cena: Double, catId: Long)

object ProductREST {
  implicit val productFormat = Json.format[ProductREST]
}
