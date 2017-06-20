package models

import play.api.libs.json.Json

case class ShoppingCartREST(userId: String, products: String)

object ShoppingCartREST {
  implicit val productFormat = Json.format[ShoppingCartREST]
}
