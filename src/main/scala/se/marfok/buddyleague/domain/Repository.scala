package se.marfok.buddyleague.domain

import akka.dispatch.Future
import akka.actor.ActorRef
import se.marfok.buddyleague.domain.RepositoryMessages._

trait Repository {

  def storeActor(): ActorRef

  def createLeague(league: League): Boolean = {
    (storeActor ? CreateLeague(league)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def getLeague(name: String): Option[League] = {
    (storeActor ? GetLeague(name)).as[Option[League]] match {
      case Some(Some(league)) => Some(league)
      case Some(None) => None
      case None => None
    }
  }

  def getLeagues(): List[League] = {
    (storeActor ? GetLeagues()).as[List[League]] match {
      case Some(leagues) => leagues
      case None => List()
    }
  }

  def deleteLeague(name: String): Boolean = {
    (storeActor ? DeleteLeague(name)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def addGameToLeague(leagueName: String, game: Game): Boolean = {
    (storeActor ? AddGameToLeague(leagueName, game)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def deleteGameFromLeague(leagueName: String, gameTimestamp: Long): Boolean = {
    (storeActor ? DeleteGameFromLeague(leagueName, gameTimestamp)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def addPlayerToLeague(leagueName: String, player: Player): Boolean = {
    (storeActor ? AddPlayerToLeague(leagueName, player)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def deletePlayerFromLeague(leagueName: String, playerName: String): Boolean = {
    (storeActor ? DeletePlayerFromLeague(leagueName, playerName)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  def populateTestData(): Unit = {
    val player1 = Player("Erik")
    val player2 = Player("Sven")
    val player3 = Player("Adam")
    val players: List[Player] = List(player1, player2, player3)
    val games: List[Game] = List(
      Game(1, Set(player1.name, player2.name), Set(player3.name), Score(6, 3)),
      Game(2, Set(player1.name), Set(player3.name), Score(2, 4)),
      Game(3, Set(player3.name), Set(player1.name, player2.name), Score(0, 8)))
    val league = League("Innebandytimmen", players, games)
    createLeague(league)
  }
}
