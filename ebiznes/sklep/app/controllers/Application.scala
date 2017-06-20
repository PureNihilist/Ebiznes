package controllers

import javax.inject.Inject
import dao.ProductDAO
import models.ProductREST
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application @Inject() (productDAO: ProductDAO) extends Controller {

  def index = Action.async { implicit request =>
    productDAO.all map {
      products => Ok(Json.toJson(products))
    }
  }

  def order = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}