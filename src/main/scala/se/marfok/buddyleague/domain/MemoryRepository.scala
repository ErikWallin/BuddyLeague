package se.marfok.buddyleague.domain

import akka.actor.Actor.actorOf
import akka.actor.actorRef2Scala
import akka.actor.Actor
import se.marfok.buddyleague.domain.RepositoryMessages._
import akka.dispatch.Future

object MemoryRepository extends Repository {

  val storeActor = actorOf[MemoryRepository]

  override def createLeague(league: League): Boolean = {
    (storeActor ? CreateLeague(league)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def getLeague(id: Long): Option[League] = {
    (storeActor ? GetLeague(id)).as[Option[League]] match {
      case Some(Some(league)) => Some(league)
      case Some(None) => None
      case None => None
    }
  }

  override def getLeagues(): List[League] = {
    (storeActor ? GetLeagues()).as[Iterable[League]] match {
      case Some(leagues) => leagues.toList
      case None => List()
    }
  }
  override def deleteLeague(id: Long): Boolean = {
    (storeActor ? DeleteLeague(id)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def addGameToLeague(leagueId: Long, game: Game): Boolean = {
    (storeActor ? AddGameToLeague(leagueId, game)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def deleteGameFromLeague(leagueId: Long, gameId: Long): Boolean = {
    (storeActor ? DeleteGameFromLeague(leagueId, gameId)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def addPlayerToLeague(leagueId: Long, player: Player): Boolean = {
    (storeActor ? AddPlayerToLeague(leagueId, player)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def deletePlayerFromLeague(leagueId: Long, playerName: String): Boolean = {
    (storeActor ? DeletePlayerFromLeague(leagueId, playerName)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }
}

class MemoryRepository extends Actor {

  val player1 = Player("Erik")
  val player2 = Player("Sven")
  val player3 = Player("Adam")
  val players: List[Player] = List(player1, player2, player3)
  val games: List[Game] = List(
    Game(1, Set(player1.name, player2.name), Set(player3.name), Score(6, 3)),
    Game(2, Set(player1.name), Set(player3.name), Score(2, 4)),
    Game(3, Set(player3.name), Set(player1.name, player2.name), Score(0, 8)))
  var leagues: Map[Long, League] = Map(1l -> League(1, "Innebandytimmen", players, games))

  protected def receive = {
    case CreateLeague(league) => {
      leagues.contains(league.id) match {
        case true => self.reply(false)
        case false => {
          leagues = leagues + (league.id -> league)
          self.reply(true)
        }
      }
    }
    case GetLeague(id) => self.reply(leagues.get(id))
    case GetLeagues() => self.reply(leagues.values)
    case DeleteLeague(id) => {
      leagues.contains(id) match {
        case false => self.reply(false)
        case true => {
          leagues = leagues - id
          self.reply(true)
        }
      }
    }
    case AddGameToLeague(leagueId, game) => {
      leagues.get(leagueId) match {
        case None => self.reply(false)
        case Some(league) => {
          leagues = leagues - leagueId
          leagues = leagues + (leagueId -> league.copy(games = game :: league.games))
          self.reply(true)
        }
      }
    }
    case DeleteGameFromLeague(leagueId, gameId) => {
      leagues.get(leagueId) match {
        case None => self.reply(false)
        case Some(league) => {
          league.getGame(gameId) match {
            case Some(game) => {
              leagues = leagues - leagueId
              leagues = leagues + (leagueId -> league.copy(games = league.games.filterNot(_ == game)))
            }
            case None => self.reply(false)
          }
        }
      }
    }
    case AddPlayerToLeague(leagueId, player) => {
      leagues.get(leagueId) match {
        case None => self.reply(false)
        case Some(league) => {
          leagues = leagues - leagueId
          leagues = leagues + (leagueId -> league.copy(players = player :: league.players))
          self.reply(true)
        }
      }
    }
    case DeletePlayerFromLeague(leagueId, playerName) => {
      leagues.get(leagueId) match {
        case None => self.reply(false)
        case Some(league) => {
          league.getPlayer(playerName) match {
            case Some(player) => {
              leagues = leagues - leagueId
              leagues = leagues + (leagueId -> league.copy(players = league.players.filterNot(_ == player)))
            }
            case None => self.reply(false)
          }
        }
      }
    }
  }
}
