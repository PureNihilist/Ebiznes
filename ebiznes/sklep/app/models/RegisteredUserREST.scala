package models

import play.api.libs.json.Json

case class RegisteredUserREST(userId: String, provider: String, firstName: String, lastName: String, fullName: String, email: String, avatarUrl: String, isAdmin: Int)

object RegisteredUserREST {
  implicit val productFormat = Json.format[RegisteredUserREST]
}