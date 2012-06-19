package se.marfok.buddyleague.domain

import akka.dispatch.Future

trait Repository {
  def createLeague(league: League): Boolean
  def getLeague(name: String): Option[League]
  def getLeagues: List[League]
  def deleteLeague(name: String): Boolean
  def addGameToLeague(leagueName: String, game: Game): Boolean
  def deleteGameFromLeague(leagueName: String, gameTimestamp: Long): Boolean
  def addPlayerToLeague(leagueName: String, player: Player): Boolean
  def deletePlayerFromLeague(leagueName: String, playerName: String): Boolean
  
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

object RepositoryMessages {
  sealed trait RepositoryMessage
  case class CreateLeague(league: League) extends RepositoryMessage
  case class GetLeague(name: String) extends RepositoryMessage
  case class GetLeagues() extends RepositoryMessage
  case class DeleteLeague(name: String) extends RepositoryMessage
  case class AddGameToLeague(leagueName: String, game: Game) extends RepositoryMessage
  case class DeleteGameFromLeague(leagueName: String, gameTimestamp: Long) extends RepositoryMessage
  case class AddPlayerToLeague(leagueName: String, player: Player) extends RepositoryMessage
  case class DeletePlayerFromLeague(leagueName: String, playerName: String) extends RepositoryMessage
}