package dao

import javax.inject.Inject

import models.{ ShoppingCart, ShoppingCartREST }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.MySQLDriver

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

class ShoppingCartDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[MySQLDriver] {

  import driver.api._

  val Baskets = TableQuery[CategoriesTable]

  def all: Future[List[ShoppingCartREST]] = {
    val query = Baskets
    val results = query.result
    val futureBaskets = db.run(results)
    futureBaskets.map(
      _.map {
      a => ShoppingCartREST(userId = a.userId, products = a.products)
    }.toList
    )
  }

  def getBasketByUserId(userId: String): Future[List[ShoppingCartREST]] = {
    val query = Baskets.filter(_.userId === userId)
    val results = query.result
    val futureBaskets = db.run(results)
    futureBaskets.map(
      _.map {
      a => ShoppingCartREST(userId = a.userId, products = a.products)
    }.toList
    )
  }

  def insert(basket: ShoppingCart): Future[ShoppingCartREST] = {

    this.deleteByUserId(basket.userId)

    val insertQuery = Baskets returning Baskets.map(_.cartId) into ((basket, cartId) => basket.copy(cartId = cartId))
    val action = insertQuery += basket
    db.run(action).map(a => ShoppingCartREST(userId = a.userId, products = a.products))
  }

  def deleteByUserId(userId: String): Future[Int] = db.run(Baskets.filter(_.userId === userId).delete)

  class CategoriesTable(tag: Tag) extends Table[ShoppingCart](tag, "ShoppingCart") {
    def cartId = column[Long]("cartId", O.PrimaryKey, O.AutoInc)
    def userId = column[String]("userId")
    def products = column[String]("products")
    def * = (cartId, userId, products) <> (models.ShoppingCart.tupled, models.ShoppingCart.unapply)
  }
}
