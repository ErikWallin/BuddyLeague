package se.marfok.buddyleague.domain

import akka.dispatch.Future

trait Repository {
  def createPlayer(player: Player): Boolean
  def getPlayer(id: Long): Option[Player]
  def getPlayers: Iterable[Player]
  def updatePlayer(player: Player): Boolean
  def deletePlayer(id: Long): Boolean
  
  def createGame(game: Game): Boolean
  def getGame(id: Long): Option[Game]
  def getGames: Iterable[Game]
  
  def createLeague(league: League): Boolean
  def getLeague(id: Long): Option[League]
  def getLeagues: Iterable[League]
  def addGameToLeague(leagueId: Long, game: Game): Boolean
  def addPlayerToLeague(leagueId: Long, player: Player): Boolean
  
}

object RepositoryMessages {
  sealed trait RepositoryMessage
  case class CreatePlayer(player: Player) extends RepositoryMessage
  case class GetPlayer(id: Long) extends RepositoryMessage
  case class GetPlayers() extends RepositoryMessage
  case class UpdatePlayer(player: Player) extends RepositoryMessage
  case class DeletePlayer(id: Long) extends RepositoryMessage
  
  case class CreateGame(game: Game) extends RepositoryMessage
  case class GetGame(id: Long) extends RepositoryMessage
  case class GetGames() extends RepositoryMessage
  
  case class CreateLeague(league: League) extends RepositoryMessage
  case class GetLeague(id: Long) extends RepositoryMessage
  case class GetLeagues() extends RepositoryMessage
  case class AddGameToLeague(leagueId: Long, game: Game) extends RepositoryMessage
  case class AddPlayerToLeague(leagueId: Long, player: Player) extends RepositoryMessage
}