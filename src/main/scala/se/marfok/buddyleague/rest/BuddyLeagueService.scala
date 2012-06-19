package se.marfok.buddyleague.rest

import cc.spray.directives.LongNumber
import cc.spray.directives.PathElement
import cc.spray.directives.Remaining
import cc.spray.http.StatusCodes
import cc.spray.json.DefaultJsonProtocol._
import cc.spray.json.pimpAny
import cc.spray.json._
import cc.spray.typeconversion.SprayJsonSupport.sprayJsonUnmarshaller
import cc.spray.Directives
import se.marfok.buddyleague.domain.DomainObjectJsonConverters.{gameFormat, leagueFormat, playerFormat, scoreFormat}
import se.marfok.buddyleague.domain.Game
import se.marfok.buddyleague.domain.League
import se.marfok.buddyleague.domain.MemoryRepository
import se.marfok.buddyleague.domain.MongoDbRepository
import se.marfok.buddyleague.domain.Player
import se.marfok.buddyleague.domain.Repository

trait BuddyLeagueService extends Directives {
  
  val repository: Repository = MongoDbRepository

  val restService = {
    pathPrefix("league") {
      path("") {
        get {
          _.complete(repository.getLeagues.toJson.compactPrint)
        } ~
          post {
            content(as[League]) { league =>
              ctx =>
                repository.createLeague(league) match {
                  case true => ctx.complete("League with name=" + league.name + " created.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with name=" + league.name + " could not be created.")
                }
            }
          }
      } ~
        pathPrefix(PathElement) { leagueName =>
          path("") {
            get { ctx =>
              repository.getLeague(leagueName) match {
                case Some(league) => ctx.complete(league.toJson.compactPrint)
                case None => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + " is not found.")
              }
            } ~
              delete { ctx =>
                repository.deleteLeague(leagueName) match {
                  case true => ctx.complete("League with name=" + leagueName + " deleted.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + " is not found.")
                }
              }
          } ~
            path("table") {
              get { ctx =>
                repository.getLeague(leagueName) match {
                  case Some(league) => ctx.complete(league.getTable.toJson.compactPrint)
                  case None => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + " is not found.")
                }
              }
            } ~
            pathPrefix("player") {
              path("") {
                post {
                  content(as[Player]) { player =>
                    ctx =>
                      repository.addPlayerToLeague(leagueName, player) match {
                        case true => ctx.complete("Player with name=" + player.name + " created in league with name=" + leagueName + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Player with name=" + player.name + " could not be created in league with name=" + leagueName + ".")
                      }
                  }
                }
              } ~
                path(Remaining) { playerName =>
                  delete { ctx =>
                    repository.deletePlayerFromLeague(leagueName, playerName) match {
                      case true => ctx.complete("Player with name=" + playerName + " deleted in league with name=" + leagueName + ".")
                      case false => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + "or player with name=" + playerName + " is not found.")
                    }
                  }
                }
            } ~
            pathPrefix("game") {
              path("") {
                post {
                  content(as[Game]) { game =>
                    ctx =>
                      repository.addGameToLeague(leagueName, game) match {
                        case true => ctx.complete("Game with timestamp=" + game.timestamp + " created in league with name=" + leagueName + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Game with timestamp=" + game.timestamp + " could not be created in league with name=" + leagueName + ".")
                      }
                  }
                }
              } ~
                path(LongNumber) { gameTimestamp =>
                  delete { ctx =>
                    repository.deleteGameFromLeague(leagueName, gameTimestamp) match {
                      case true => ctx.complete("Game with timestamp=" + gameTimestamp + " deleted in league with name=" + leagueName + ".")
                      case false => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + "or game with timestamp=" + gameTimestamp + " is not found.")
                    }
                  }
                }
            }
        }
    }
  }
}
