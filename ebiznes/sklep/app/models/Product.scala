package models
import java.sql.Timestamp
import play.api.libs.json.Format

case class Product(prodId: Long, tytul: String, opis: String, img: String, cena: Double, catId: Long)