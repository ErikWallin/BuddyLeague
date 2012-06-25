package se.marfok.buddyleague.domain

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
