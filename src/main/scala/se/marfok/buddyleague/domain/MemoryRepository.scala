package se.marfok.buddyleague.domain

import akka.actor.Actor.actorOf
import akka.actor.actorRef2Scala
import akka.actor.Actor
import se.marfok.buddyleague.domain.RepositoryMessages._
import akka.dispatch.Future

object MemoryRepository extends Repository {

  val storeActor = actorOf[MemoryRepository]
  
  override def createPlayer(player: Player): Boolean = {
    (storeActor ? CreatePlayer(player)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def getPlayer(id: Long): Option[Player] = {
    (storeActor ? GetPlayer(id)).as[Option[Player]] match {
      case Some(Some(player)) => Some(player)
      case Some(None) => None
      case None => None
    }
  }

  override def getPlayers(): Iterable[Player] = {
    (storeActor ? GetPlayers()).as[Iterable[Player]] match {
      case Some(players) => players
      case None => Iterable()
    }
  }

  override def updatePlayer(player: Player): Boolean = {
    (storeActor ? UpdatePlayer(player)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }
  
  override def deletePlayer(id: Long): Boolean = {
    (storeActor ? DeletePlayer(id)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }
  
  override def createGame(game: Game): Boolean = {
    (storeActor ? CreateGame(game)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def getGame(id: Long): Option[Game] = {
    (storeActor ? GetGame(id)).as[Option[Game]] match {
      case Some(Some(game)) => Some(game)
      case Some(None) => None
      case None => None
    }
  }
  
  override def getGames(): Iterable[Game] = {
    (storeActor ? GetGames()).as[Iterable[Game]] match {
      case Some(games) => games
      case None => Iterable()
    }
  }
  
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
  
  override def getLeagues(): Iterable[League] = {
    (storeActor ? GetLeagues()).as[Iterable[League]] match {
      case Some(leagues) => leagues
      case None => Iterable()
    }
  }
  
  override def addGameToLeague(leagueId: Long, game: Game): Boolean = {
    (storeActor ? AddGameToLeague(leagueId, game)).as[Boolean] match {
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
}

class MemoryRepository extends Actor {

  var players: Map[Long, Player] = Map(
    1l -> Player(1, "Erik", "Wallin"),
    2l -> Player(2, "Erik", "Ytterberg"),
    3l -> Player(3, "Erik", "Gohde"),
    4l -> Player(4, "Daniel", "Aarno"),
    5l -> Player(5, "Thomas", "Wincent"),
    6l -> Player(6, "Andreas", "Ragnerstam"),
    7l -> Player(7, "Stefan", "Tollbäck"))

  var games: Map[Long, Game] = Map(
    1l -> Game(1, Set(players(1), players(2), players(3), players(4)), Set(players(5), players(6), players(7)), Score(6, 3)),
    2l -> Game(2, Set(players(4), players(6)), Set(players(5), players(7)), Score(6, 3)),
    3l -> Game(3, Set(players(1), players(5), players(2)), Set(players(6), players(7)), Score(6, 3))
  )

  var leagues: Map[Long, League] = Map(
    1l -> League(1, List(players(1), players(2), players(3), players(4), players(5), players(6), players(7)), List(games(1), games(2), games(3))))

  protected def receive = {
    case CreatePlayer(player) => {
      players.contains(player.id) match {
        case true => self.reply(false)
        case false => {
          players = players + (player.id -> player)
          self.reply(true)
        }
      }
    }
    case GetPlayer(id) => self.reply(players.get(id))
    case GetPlayers() => self.reply(players.values)
    case UpdatePlayer(player) => {
      players.contains(player.id) match {
        case false => self.reply(false)
        case true => {
          players = players - player.id
          players = players + (player.id -> player)
          self.reply(true)
        }
      }
    }
    case DeletePlayer(id) => {
      players.contains(id) match {
        case false => self.reply(false)
        case true => {
          players = players - id
          self.reply(true)
        }
      }
    }

    case CreateGame(game) => {
      games.contains(game.id) match {
        case true => self.reply(false)
        case false => {
          games = games + (game.id -> game)
          self.reply(true)
        }
      }
    }
    case GetGame(id) => self.reply(games.get(id))
    case GetGames() => self.reply(games.values)

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
  }
}