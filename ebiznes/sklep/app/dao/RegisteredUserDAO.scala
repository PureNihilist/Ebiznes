package dao

import javax.inject.Inject

import models.{ RegisteredUserREST, RegisteredUser }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.MySQLDriver
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

class RegisteredUserDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[MySQLDriver] {

  import driver.api._

  val RegisteredUsers = TableQuery[RegisteredUsersTable]

  def all(implicit ec: ExecutionContext): Future[List[RegisteredUserREST]] = {
    val query = RegisteredUsers
    val results = query.result
    val futureUsers = db.run(results)
    futureUsers.map(
      _.map {
      a => RegisteredUserREST(userId = a.userId, provider = a.provider, firstName = a.firstName, lastName = a.lastName, fullName = a.fullName, email = a.email, avatarUrl = a.avatarUrl, isAdmin = a.isAdmin)
    }.toList
    )
  }

  def getByUserId(userId: String): Future[List[RegisteredUserREST]] =
    {
      var futureUsers = db.run(RegisteredUsers.filter(_.userId === userId).result.headOption)
      futureUsers.map(
        _.map {
        a => RegisteredUserREST(userId = a.userId, provider = a.provider, firstName = a.firstName, lastName = a.lastName, fullName = a.fullName, email = a.email, avatarUrl = a.avatarUrl, isAdmin = a.isAdmin)
      }.toList
      )
    }

  def insert(registeredUser: RegisteredUser): Future[RegisteredUserREST] = {

    val insertQuery = RegisteredUsers returning RegisteredUsers.map(_.id) into ((registeredUser, id) => registeredUser.copy(id = id))
    val action = insertQuery += registeredUser
    val dbAc = db.run(action)
    dbAc.map(a => RegisteredUserREST(userId = a.userId, provider = a.provider, firstName = a.firstName, lastName = a.lastName, fullName = a.fullName, email = a.email, avatarUrl = a.avatarUrl, isAdmin = a.isAdmin))
  }

  def deleteById(userId: String): Future[Int] = db.run(RegisteredUsers.filter(_.userId === userId).delete)

  class RegisteredUsersTable(tag: Tag) extends Table[RegisteredUser](tag, "RegisteredUser") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[String]("userId")
    def provider = column[String]("provider")
    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def fullName = column[String]("fullName")
    def email = column[String]("email")
    def avatarUrl = column[String]("avatarUrl")
    def isAdmin = column[Int]("isAdmin")

    def * = (id, userId, provider, firstName, lastName, fullName, email, avatarUrl, isAdmin) <> (models.RegisteredUser.tupled, models.RegisteredUser.unapply)
  }

}
