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
import se.marfok.buddyleague.domain.when_necessary_context._

object MongoDbRepository extends Repository {

  override val storeActor = actorOf[MongoDbStore]
}

class MongoDbStore extends Actor {
  
  val leagues = connect.right.get
  
  def connect(): Either[Exception, MongoCollection] = {
    val mongolabUri = Properties.envOrElse("MONGOLAB_URI", "mongodb://127.0.0.1:27017/buddyleague")
	  val mongoURI = MongoURI(mongolabUri)
	  val mongoDB = mongoURI.connectDB
	  if (mongoURI.username != null && mongoURI.password != null) {
	    val result = mongoDB.authenticate(mongoURI.username, mongoURI.password.foldLeft("")(_ + _.toString))
	  }
  	Right(mongoDB("leagues"))
  }
  
  def getDBObjectForString(id: String) = MongoDBObject("_id" -> id)
  def getDBObjectForLong(id: Long) = MongoDBObject("_id" -> id)

  protected def receive = {
    case CreateLeague(league) => {
      leagues.findOne(getDBObjectForString(league.name)) match {
        case Some(dbObject) => self.reply(false)
        case None => {
          leagues += grater[League].asDBObject(league)
          self.reply(true)
        }
      }
    }
    case GetLeague(name) => {
      leagues.findOne(getDBObjectForString(name)) match {
        case Some(dbObject) => self.reply(Some(grater[League].asObject(dbObject)))
        case None => self.reply(None)
      }
    }
    case GetLeagues() => {
      self.reply(leagues.map(grater[League].asObject(_)).toList)
    }
    case DeleteLeague(name) => {
      leagues.remove(getDBObjectForString(name))
      self.reply(true)
    }
    case AddGameToLeague(leagueName, game) => {
      leagues.update(getDBObjectForString(leagueName), $push("games" -> grater[Game].asDBObject(game)))
      self.reply(true)
    }
    case DeleteGameFromLeague(leagueName, gameTimestamp) => {
      leagues.update(getDBObjectForString(leagueName), $pull("games" -> getDBObjectForLong(gameTimestamp)))
      self.reply(true)
    }
    case AddPlayerToLeague(leagueName, player) => {
      leagues.update(getDBObjectForString(leagueName), $push("players" -> grater[Player].asDBObject(player)))
      self.reply(true)
    }
    case DeletePlayerFromLeague(leagueName, playerName) => {
      leagues.update(getDBObjectForString(leagueName), $pull("games" -> getDBObjectForString(playerName)))
      self.reply(true)
    }
  }
}
