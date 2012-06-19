package se.marfok.buddyleague.domain

import akka.actor.Actor.actorOf
import akka.actor.actorRef2Scala
import akka.actor.Actor
import se.marfok.buddyleague.domain.RepositoryMessages._
import akka.dispatch.Future
import cc.spray.json.DefaultJsonProtocol._
import se.marfok.buddyleague.domain.DomainObjectJsonConverters.{ gameFormat, scoreFormat, leagueFormat, playerFormat }
import scala.util.Properties
import com.mongodb.casbah.MongoURI
import com.mongodb.casbah.Imports._
import cc.spray.json._
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._

object MongoDbRepository extends Repository {

  val storeActor = actorOf[MongoDbRepository]

  override def createLeague(league: League): Boolean = {
    (storeActor ? CreateLeague(league)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def getLeague(name: String): Option[League] = {
    (storeActor ? GetLeague(name)).as[Option[League]] match {
      case Some(Some(league)) => Some(league)
      case Some(None) => None
      case None => None
    }
  }

  override def getLeagues(): List[League] = {
    (storeActor ? GetLeagues()).as[List[League]] match {
      case Some(leagues) => leagues
      case None => List()
    }
  }
  override def deleteLeague(name: String): Boolean = {
    (storeActor ? DeleteLeague(name)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def addGameToLeague(leagueName: String, game: Game): Boolean = {
    (storeActor ? AddGameToLeague(leagueName, game)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def deleteGameFromLeague(leagueName: String, gameTimestamp: Long): Boolean = {
    (storeActor ? DeleteGameFromLeague(leagueName, gameTimestamp)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def addPlayerToLeague(leagueName: String, player: Player): Boolean = {
    (storeActor ? AddPlayerToLeague(leagueName, player)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }

  override def deletePlayerFromLeague(leagueName: String, playerName: String): Boolean = {
    (storeActor ? DeletePlayerFromLeague(leagueName, playerName)).as[Boolean] match {
      case Some(true) => true
      case Some(false) => false
      case None => false
    }
  }
}

class MongoDbRepository extends Actor {
  val mongolabUri = Properties.envOrElse("MONGOLAB_URI", "mongodb://127.0.0.1:27017/buddyleague")
  val mongoURI = MongoURI(mongolabUri)
  val mongoDB = mongoURI.connectDB
  if (mongoURI.username != null && mongoURI.password != null) mongoDB.authenticate(mongoURI.username, mongoURI.password.toString())
  var leagues = mongoDB("leagues")

  protected def receive = {
    case CreateLeague(league) => {
      leagues.findOne(MongoDBObject("_id" -> league.name)) match {
        case Some(dbObject) => self.reply(false)
        case None => {
          leagues += grater[League].asDBObject(league)
          self.reply(true)
        }
      }
    }
    case GetLeague(name) => {
      leagues.findOne(MongoDBObject("_id" -> name)) match {
        case Some(dbObject) => self.reply(Some(grater[League].asObject(dbObject)))
        case None => self.reply(None)
      }
    }
    case GetLeagues() => {
      self.reply(leagues.map(grater[League].asObject(_)).toList)
    }
    case DeleteLeague(leagueName) => {
      self.reply(true)
//      leagues.contains(leagueName) match {
//        case false => self.reply(false)
//        case true => {
//          leagues = leagues - leagueName
//          self.reply(true)
//        }
//      }
    }
    case AddGameToLeague(leagueName, game) => {
      self.reply(true)
//      leagues.get(leagueName) match {
//        case None => self.reply(false)
//        case Some(league) => {
//          leagues = leagues - leagueName
//          leagues = leagues + (leagueName -> league.copy(games = game :: league.games))
//          self.reply(true)
//        }
//      }
    }
    case DeleteGameFromLeague(leagueName, gameTimestamp) => {
      self.reply(true)
//      leagues.get(leagueName) match {
//        case None => self.reply(false)
//        case Some(league) => {
//          league.getGame(gameTimestamp) match {
//            case Some(game) => {
//              leagues = leagues - leagueName
//              leagues = leagues + (leagueName -> league.copy(games = league.games.filterNot(_ == game)))
//            }
//            case None => self.reply(false)
//          }
//        }
//      }
    }
    case AddPlayerToLeague(leagueName, player) => {
      self.reply(true)
//      leagues.get(leagueName) match {
//        case None => self.reply(false)
//        case Some(league) => {
//          leagues = leagues - leagueName
//          leagues = leagues + (leagueName -> league.copy(players = player :: league.players))
//          self.reply(true)
//        }
//      }
    }
    case DeletePlayerFromLeague(leagueName, playerName) => {
      self.reply(true)
//      leagues.get(leagueName) match {
//        case None => self.reply(false)
//        case Some(league) => {
//          league.getPlayer(playerName) match {
//            case Some(player) => {
//              leagues = leagues - leagueName
//              leagues = leagues + (leagueName -> league.copy(players = league.players.filterNot(_ == player)))
//            }
//            case None => self.reply(false)
//          }
//        }
//      }
    }
  }
}
