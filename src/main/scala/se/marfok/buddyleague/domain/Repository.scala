package se.marfok.buddyleague.domain

import akka.dispatch.Future

trait Repository {
  def createLeague(league: League): Boolean
  def getLeague(id: Long): Option[League]
  def getLeagues: List[League]
  def deleteLeague(id: Long): Boolean
  def addGameToLeague(leagueId: Long, game: Game): Boolean
  def deleteGameFromLeague(leagueId: Long, gameId: Long): Boolean
  def addPlayerToLeague(leagueId: Long, player: Player): Boolean
  def deletePlayerFromLeague(leagueId: Long, playerName: String): Boolean
}

object RepositoryMessages {
  sealed trait RepositoryMessage
  case class CreateLeague(league: League) extends RepositoryMessage
  case class GetLeague(id: Long) extends RepositoryMessage
  case class GetLeagues() extends RepositoryMessage
  case class DeleteLeague(id: Long) extends RepositoryMessage
  case class AddGameToLeague(leagueId: Long, game: Game) extends RepositoryMessage
  case class DeleteGameFromLeague(leagueId: Long, gameId: Long) extends RepositoryMessage
  case class AddPlayerToLeague(leagueId: Long, player: Player) extends RepositoryMessage
  case class DeletePlayerFromLeague(leagueId: Long, playerName: String) extends RepositoryMessage
}