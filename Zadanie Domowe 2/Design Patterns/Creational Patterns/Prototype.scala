package controllers

import play.api._
import play.api.mvc._

class CloneableProduct {
  override def clone = {
    super.clone
  }
}

case class ProductPrototype(var ProductName : String,var ID : Int) extends CloneableProduct {

  override def clone = {
    super.clone
  }

  override def hashCode(): Int = super.hashCode

  override def toString: String = this.ID + ": " + this.ProductName

  def getName = this.ProductName
  def getID = this.ID
  def setName (Name : String) = {
    this.ProductName = Name
  }
  def setID (id : Int) = {
    this.ID = id
  }
}

object Application extends Controller {

  val product = new ProductPrototype("produkt", 0)
  val product_disc = product.toString
  val myclone : ProductPrototype = product.copy()
  println(myclone.getID + " " + myclone.getName)
  myclone.setID(1)
  myclone.setName("Klon")
  val myclone_disc = myclone.toString
  def index = Action {
    Ok(views.html.index(product_disc + " , " + myclone_disc))
  }
}