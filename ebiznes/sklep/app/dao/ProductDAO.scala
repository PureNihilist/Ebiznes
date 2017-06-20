package dao

import javax.inject.Inject
import models.{ Product, ProductREST }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.MySQLDriver
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ ExecutionContext, Future }

class ProductDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[MySQLDriver] {

  import driver.api._

  val Products = TableQuery[ProductsTable]

  def all(implicit ec: ExecutionContext): Future[List[ProductREST]] = {
    val query = Products
    val results = query.result
    val futureProducts = db.run(results)
    futureProducts.map(
      _.map {
      a => ProductREST(prodId = a.prodId, opis = a.opis, tytul = a.tytul, img = a.img, cena = a.cena, catId = a.catId)
    }.toList
    )
  }

  def insert(product: Product): Future[ProductREST] = {

    val insertQuery = Products returning Products.map(_.prodId) into ((product, prodId) => product.copy(prodId = prodId))

    val action = insertQuery += product
    db.run(action).map(a => ProductREST(prodId = a.prodId, opis = a.opis, tytul = a.tytul, img = a.img, cena = a.cena, catId = a.catId))
  }

  def updateById(product: Product): Future[Int] = db.run(Products.filter(_.prodId === product.prodId).update(product))

  def deleteById(id: Long): Future[Int] = db.run(Products.filter(_.prodId === id).delete)

  class ProductsTable(tag: Tag) extends Table[Product](tag, "Produkt") {
    def prodId = column[Long]("prodId", O.PrimaryKey, O.AutoInc)
    def tytul = column[String]("tytul")
    def opis = column[String]("opis")
    def img = column[String]("img")
    def cena = column[Double]("cena")
    def catId = column[Long]("catId")
    def * = (prodId, tytul, opis, img, cena, catId) <> (models.Product.tupled, models.Product.unapply)
  }
}
