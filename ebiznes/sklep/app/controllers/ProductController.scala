package controllers

import javax.inject.Inject

import dao.ProductDAO
import models.{ Product, ProductREST }
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class ProductController @Inject() (productDAO: ProductDAO) extends Controller {

  def getAll = Action.async { implicit request =>
    productDAO.all map {
      products => Ok(Json.toJson(products))
    }
  }

  def newProduct = Action.async { implicit request =>
    var json: ProductREST = request.body.asJson.get.as[ProductREST]
    var product = Product(prodId = 0, opis = json.opis, tytul = json.tytul, img = json.img, cena = json.cena, catId = json.catId)
    var productResult = productDAO.insert(product)
    productResult.map {
      prod => Ok(Json.toJson(prod))
    }

  }

  def updateProduct = Action { implicit request =>
    var json: ProductREST = request.body.asJson.get.as[ProductREST]
    var product = Product(prodId = json.prodId, opis = json.opis, tytul = json.tytul, img = json.img, cena = json.cena, catId = json.catId)
    productDAO.updateById(product)
    Ok(Json.toJson(json))
  }

  def deleteProduct(id: Long) = Action { implicit request =>
    productDAO.deleteById(id)
    Ok("OK")
  }
}
