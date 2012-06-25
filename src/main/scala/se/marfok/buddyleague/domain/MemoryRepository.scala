package se.marfok.buddyleague.domain

import akka.actor.Actor.actorOf
import akka.actor.actorRef2Scala
import akka.actor.Actor
import se.marfok.buddyleague.domain.RepositoryMessages._
import akka.dispatch.Future

object MemoryRepository extends Repository {

  override val storeActor = actorOf[MemoryStore]
}

class MemoryStore extends Actor {

  var leagues: Map[String, League] = Map()

  protected def receive = {
    case CreateLeague(league) => {
      leagues.contains(league.name) match {
        case true => self.reply(false)
        case false => {
          leagues = leagues + (league.name -> league)
          self.reply(true)
        }
      }
    }
    case GetLeague(name) => self.reply(leagues.get(name))
    case GetLeagues() => self.reply(leagues.values.toList)
    case DeleteLeague(name) => {
      leagues.contains(name) match {
        case false => self.reply(false)
        case true => {
          leagues = leagues - name
          self.reply(true)
        }
      }
    }
    case AddGameToLeague(leagueName, game) => {
      leagues.get(leagueName) match {
        case None => self.reply(false)
        case Some(league) => {
          leagues = leagues - leagueName
          leagues = leagues + (leagueName -> league.copy(games = game :: league.games))
          self.reply(true)
        }
      }
    }
    case DeleteGameFromLeague(leagueName, gameTimestamp) => {
      leagues.get(leagueName) match {
        case None => self.reply(false)
        case Some(league) => {
          league.getGame(gameTimestamp) match {
            case Some(game) => {
              leagues = leagues - leagueName
              leagues = leagues + (leagueName -> league.copy(games = league.games.filterNot(_ == game)))
              self.reply(true)
            }
            case None => self.reply(false)
          }
        }
      }
    }
    case AddPlayerToLeague(leagueName, player) => {
      leagues.get(leagueName) match {
        case None => self.reply(false)
        case Some(league) => {
          leagues = leagues - leagueName
          leagues = leagues + (leagueName -> league.copy(players = player :: league.players))
          self.reply(true)
        }
      }
    }
    case DeletePlayerFromLeague(leagueName, playerName) => {
      leagues.get(leagueName) match {
        case None => self.reply(false)
        case Some(league) => {
          league.getPlayer(playerName) match {
            case Some(player) => {
              leagues = leagues - leagueName
              leagues = leagues + (leagueName -> league.copy(players = league.players.filterNot(_ == player)))
              self.reply(true)
            }
            case None => self.reply(false)
          }
        }
      }
    }
  }
}
