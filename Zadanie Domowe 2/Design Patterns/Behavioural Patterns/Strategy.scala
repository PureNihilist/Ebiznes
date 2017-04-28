package controllers
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.io.Source

/**
  * Created by nihilist on 4/25/17.
  */

trait Parser [T] {
  def parse(FileName : String) : String
}
class CSVParser extends Parser[Object] {
  override def parse(FileName : String): String  = {
    val builder = StringBuilder.newBuilder
    for (line <- Source.fromFile(FileName).getLines) {
      val cols = line.split(",").map(_.trim)
      builder.append(s"${cols(0)}|${cols(1)}|${cols(2)}")
    }
  return builder.toString()
  }
}
class JsonParser extends Parser[Object]{
  override def parse(FileName : String) : String = {
    val JsonFile : String = Source.fromFile(FileName).getLines.mkString
    val json : JsValue = Json.parse(JsonFile)
    return json.toString()
  }
}
class Application () {
  def apply(FileName : String) : Parser [Object] = {
    FileName match {
      case f if f.endsWith(".json") => new JsonParser
      case f if f.endsWith(".csv") => new CSVParser
      case f => throw new RuntimeException(s"Unknown format: $f")
    }
  }
}
object Application extends Controller {
  def index = Action {
    val AppJSon = new Application()
    val JSonParser : Parser[Object] = AppJSon.apply("person.json")
    val json = JSonParser.parse("person.json")
    val AppCSV = new Application()
    val CSVParser : Parser[Object] = AppCSV.apply("person.csv")
    val csv = CSVParser.parse("person.csv")

    Ok(views.html.index(json.toString + "\n" + csv.toString ))
  }

}