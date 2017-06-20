package models

import play.api.libs.json.Json

case class CategoryREST(catId: Long, nazwa: String)

object CategoryREST {
  implicit val productFormat = Json.format[CategoryREST]
}
