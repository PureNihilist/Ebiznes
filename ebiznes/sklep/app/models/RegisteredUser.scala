package models

case class RegisteredUser(id: Long, userId: String, provider: String, firstName: String, lastName: String, fullName: String, email: String, avatarUrl: String, isAdmin: Int)

