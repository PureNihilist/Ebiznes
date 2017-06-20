package dao

import javax.inject.Inject

import models.{ Purchase, PurchaseREST }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.MySQLDriver
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

class PurchaseDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[MySQLDriver] {

  import driver.api._

  val Purchases = TableQuery[PurchaseTable]

  def all(implicit ec: ExecutionContext): Future[List[PurchaseREST]] = {
    val query = Purchases
    val results = query.result
    val futurePurchases = db.run(results)
    futurePurchases.map(
      _.map {
      a => PurchaseREST(id = a.id, userId = a.userId, adress = a.adress, sendType = a.sendType, jsonOrder = a.jsonOrder, totalPrice = a.totalPrice, date = a.date)
    }.toList
    )
  }

  def insert(purchase: Purchase): Future[PurchaseREST] = {

    val insertQuery = Purchases returning Purchases.map(_.id) into ((purchase, id) => purchase.copy(id = id))
    val action = insertQuery += purchase
    val dbAc = db.run(action)
    dbAc.map(a => PurchaseREST(id = a.id, userId = a.userId, adress = a.adress, sendType = a.sendType, jsonOrder = a.jsonOrder, totalPrice = a.totalPrice, date = a.date))
  }

  def deleteById(id: Long): Future[Int] = db.run(Purchases.filter(_.id === id).delete)

  class PurchaseTable(tag: Tag) extends Table[Purchase](tag, "Purchase") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[String]("userId")
    def adress = column[String]("adress")
    def sendType = column[String]("sendType")
    def jsonOrder = column[String]("jsonOrder")
    def totalPrice = column[Double]("totalPrice")
    def date = column[String]("date")

    def * = (id, userId, adress, sendType, jsonOrder, totalPrice, date) <> (models.Purchase.tupled, models.Purchase.unapply)
  }

}
