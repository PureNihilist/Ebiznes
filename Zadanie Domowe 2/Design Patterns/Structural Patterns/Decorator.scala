/**
  * Created by nihilist on 4/24/17.
  */
import scala.io.StdIn.readLine
import scala.io.Source
trait Reader {
  val FileName = "tekst.txt"
  def print() : Unit
}

class InputReader extends Reader {
  override def print() : Unit = print()
}

class FileReader extends Reader {
  override def print() : Unit = print()
}

class NoWhiteSpaceInputReader(decor : Reader) extends InputReader {
  override def print() : Unit = {
    println(readLine().replaceAll("\\s",""))
  }
}

class UpperCaseInputReader(decor : Reader) extends InputReader {
  override def print() : Unit = {
    println(readLine().toUpperCase)
  }
}

class NoWhiteSpaceFileReader(decor : Reader) extends FileReader {
  override def print() : Unit = {
    println(Source.fromFile(FileName).getLines.mkString.replaceAll("\\s",""))
  }
}

class UpperCaseFileReader(decor : Reader) extends FileReader {
  override def print() : Unit = {
    for (str <- Source.fromFile(FileName).getLines)
    {
      println(str.toUpperCase)
    }
  }
}

val reader1 : Reader = new UpperCaseInputReader(new InputReader())
val reader2 : Reader = new NoWhiteSpaceInputReader(new InputReader())
val reader3 : Reader = new UpperCaseFileReader(new FileReader())
val reader4 : Reader = new NoWhiteSpaceFileReader(new FileReader())

reader1.print()
reader2.print()
reader3.print()
reader4.print()
