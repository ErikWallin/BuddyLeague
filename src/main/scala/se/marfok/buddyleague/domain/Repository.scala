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