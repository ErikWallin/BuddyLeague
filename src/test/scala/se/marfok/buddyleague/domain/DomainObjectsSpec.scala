package se.marfok.buddyleague.domain

import org.specs2.mutable.Specification
import cc.spray.http.HttpContent.pimpHttpContentWithAs2
import cc.spray.http.HttpMethods.GET
import cc.spray.http.HttpMethods.POST
import cc.spray.http.StatusCodes.MethodNotAllowed
import cc.spray.http.HttpRequest
import cc.spray.http.HttpResponse
import cc.spray.test.SprayTest
import org.specs2.SpecificationWithJUnit

class DomainObjectsSpec extends Specification {

  "Score" should {
    "sum two scores" in {
      Score(2, 3) + Score(1, 7) must beEqualTo(Score(3, 10))
    }
    "subract two scores" in {
      Score(6, 3) - Score(4, 2) must beEqualTo(Score(2, 1))
    }
    "invert a score" in {
      Score(6, 3).invert must beEqualTo(Score(3, 6))
    }
  }

  "A League with errors" should {
    val player1 = Player(1, "Tore", "Svensson")
    val player2 = Player(2, "Sven", "Turbo")
    val player2_2 = Player(2, "Arne", "Anka")
    val player3 = Player(3, "Kalle", "Anka")
    val game1 = Game(1, Set(1), Set(3), Score(3, 1))
    val game2 = Game(2, Set(3), Set(1), Score(2, 2))
    val game2_2 = Game(2, Set(3), Set(1), Score(4, 0))
    val league = League(1, "Innebandytimmen", List(player1, player2, player2_2), List(game1, game2, game2_2))
    "get right player" in {
      league.getPlayer(1) must beEqualTo(Some(player1))
    }
    "get no player" in {
      league.getPlayer(4) must beEqualTo(None)
    }
    "get one player" in {
      league.getPlayer(2) must beEqualTo(Some(player2)) or beEqualTo(Some(player2_2))
    }
    "get right game" in {
      league.getGame(1) must beEqualTo(Some(game1))
    }
    "get no game" in {
      league.getGame(3) must beEqualTo(None)
    }
    "get one game" in {
      league.getGame(2) must beEqualTo(Some(game2)) or beEqualTo(Some(game2_2))
    }
  }

  "A League" should {
    "return correct standings" in {
      val player1 = Player(1, "Tore", "Svensson")
      val player3 = Player(3, "Kalle", "Anka")
      val game1 = Game(1, Set(1), Set(3), Score(3, 1))
      val game2 = Game(2, Set(3), Set(1), Score(2, 2))
      val game3 = Game(2, Set(3), Set(1), Score(4, 0))
      val league = League(1, "Innebandytimmen", List(player1, player3), List(game1, game2, game3))
      val table = league.getTable
      table.size must beEqualTo(2)
      table(0)._1 must beEqualTo(player3)
      table(0)._2 must beEqualTo(Score(7, 5))
      table(1)._1 must beEqualTo(player1)
      table(1)._2 must beEqualTo(Score(5, 7))
    }
  }
}
