package dao

import javax.inject.Inject

import models.{ Category, CategoryREST }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import slick.driver.MySQLDriver
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.{ ExecutionContext, Future }

class CategoryDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[MySQLDriver] {

  import driver.api._

  val Categories = TableQuery[CategoriesTable]

  def all(implicit ec: ExecutionContext): Future[List[CategoryREST]] = {
    val query = Categories
    val results = query.result
    val futureCategories = db.run(results)
    futureCategories.map(
      _.map {
      a => CategoryREST(catId = a.catId, nazwa = a.nazwa)
    }.toList
    )
  }

  def insert(category: Category): Future[CategoryREST] = {

    val insertQuery = Categories returning Categories.map(_.catId) into ((category, catId) => category.copy(catId = catId))

    val action = insertQuery += category
    db.run(action).map(a => CategoryREST(catId = a.catId, nazwa = a.nazwa))
  }

  def updateById(category: Category): Future[Int] = db.run(Categories.filter(_.catId === category.catId).update(category))

  def deleteById(id: Long): Future[Int] = db.run(Categories.filter(_.catId === id).delete)

  class CategoriesTable(tag: Tag) extends Table[Category](tag, "Kategoria") {
    def catId = column[Long]("catId", O.PrimaryKey, O.AutoInc)
    def nazwa = column[String]("nazwa")
    def * = (catId, nazwa) <> (models.Category.tupled, models.Category.unapply)
  }

}
