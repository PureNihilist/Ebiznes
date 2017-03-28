/**
  * Created by nihilist on 3/21/17.
  */
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine

class Result
{
  def victory_status(status: ListBuffer[String]) : Boolean =
  {
    val s1 = List("a1","b1","c1")
    val s2 = List("a2","b2","c2")
    val s3 = List("a3","b3","c3")

    val s4 = List("a1","a2","a3")
    val s5 = List("b1","b2","b3")
    val s6 = List("c1","c2","c3")

    val s7 = List("a1","b2","c3")
    val s8 = List("a3","b2","c1")

    val player = status.sorted
    val list = player.toList
    val array_of_list  = Array(s1,s2,s3,s4,s5,s6,s7,s8)
    for( i <- 0 to 7)
    {
      if(list == array_of_list(i))
      {
        return true
      }
    }
    return false
  }
}
class Game
{
  def check_pos(pos: String) : Boolean =
  {
    val board = List("a1","a2","a3","b1","b2","b3","c1","c2","c3") //litery to wiersze, cyfry to kolumny
    val current_pos = pos
    if (board.contains(current_pos))
    {
      return true
    }
    return false
  }
  def start
  {
    val player1_moves = ListBuffer[String]()
    val player2_moves = ListBuffer[String]()
    var pos = ""
    for ( i <- 1 to 3)
    {
      var ward = 0
      while (ward != -1)
      {
        println("Ruch gracza 1: Wybierz niewybrana wczesniej pozycje na planszy")
        pos = readLine()
        val pos_status = check_pos(pos)
        if (pos_status)
        {
          if (player1_moves.contains(pos))
          {
            println("Gracz 1 juz zaznaczy to pole wczesniej")
          }
          else if (player2_moves.contains(pos))
          {
            println("Gracz 2 juz zaznaczy to pole wczesniej")
          }
          else
          {
            player1_moves += pos
            println("Gracz 1 wybral poprawne pole")
            ward = -1
          }
        }
        else
        {
          println("Gracz 1: Wybierz pozycje pomiedzy A1 a C3")
        }
      }
      val mid_result = new Result
      val player1_win = mid_result.victory_status(player1_moves)
      if (!player1_win)
      {
        for ( j <- 1 to 1)
        {
          ward = 0
          while (ward != -1)
          {
            println("Ruch gracza 2: Wybierz niewybrana wczesniej pozycje na planszy")
            pos = readLine()
            val pos_status = check_pos(pos)
            if (pos_status)
            {
              if (player2_moves.contains(pos))
              {
                println("Gracz 2 juz zaznaczy to pole wczesniej")
              }
              else if (player1_moves.contains(pos))
              {
                println("Gracz 1 juz zaznaczyl to pole wczesniej")
              }
            else
            {
              player2_moves += pos
              println("Gracz 2 wybral poprawne pole")
              ward = -1
            }
          }
        }
      }
     }
    }
    val end_result = new Result
    val player1_win = end_result.victory_status(player1_moves)
    val player2_win = end_result.victory_status(player2_moves)
    if (player1_win)
    {
      println("KONIEC: GRACZ 1 WYGRYWA!")
    }
    else if (player2_win)
    {
      println("KONIEC: GRACZ 2 WYGRYWA!")
    }
    else
      println("KONIEC: REMIS")
  }
}
var startgame = new Game
startgame.start
