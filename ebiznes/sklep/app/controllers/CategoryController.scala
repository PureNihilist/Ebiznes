package controllers

import javax.inject.Inject

import dao.CategoryDAO
import models.{ Category, CategoryREST }
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class CategoryController @Inject() (categoryDAO: CategoryDAO) extends Controller {
  def getAll = Action.async { implicit request =>
    categoryDAO.all map {
      categories => Ok(Json.toJson(categories))
    }
  }

  def newCategory = Action.async { implicit request =>
    var json: CategoryREST = request.body.asJson.get.as[CategoryREST]
    var category = Category(catId = 0, nazwa = json.nazwa)
    var categoryResult = categoryDAO.insert(category)
    categoryResult.map {
      cat => Ok(Json.toJson(cat))
    }

  }

  def updateCategory = Action { implicit request =>
    var json: CategoryREST = request.body.asJson.get.as[CategoryREST]
    var category = Category(catId = json.catId, nazwa = json.nazwa)
    println(categoryDAO.updateById(category))
    Ok(Json.toJson(json))
  }

  def deleteCategory(id: Long) = Action { implicit request =>
    categoryDAO.deleteById(id)
    Ok("OK")
  }
}
