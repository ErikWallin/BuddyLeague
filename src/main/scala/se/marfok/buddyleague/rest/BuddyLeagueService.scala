package se.marfok.buddyleague.rest

import cc.spray.directives.{ LongNumber, PathElement, Remaining }
import cc.spray.http.StatusCodes
import cc.spray.json.pimpAny
import cc.spray.json._
import cc.spray.json.DefaultJsonProtocol._
import cc.spray.typeconversion.SprayJsonSupport.sprayJsonUnmarshaller
import cc.spray.Directives
import se.marfok.buddyleague.domain.DomainObjectJsonConverters.{ gameFormat, scoreFormat, leagueFormat, playerFormat }
import se.marfok.buddyleague.domain.{ Game, League, Player }
import se.marfok.buddyleague.domain.MemoryRepository

trait BuddyLeagueService extends Directives {

  val restService = {
    pathPrefix("league") {
      path("") {
        get {
          _.complete(MemoryRepository.getLeagues.toJson.compactPrint)
        } ~
          post {
            content(as[League]) { league =>
              ctx =>
                MemoryRepository.createLeague(league) match {
                  case true => ctx.complete("League with name=" + league.name + " created.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with name=" + league.name + " could not be created.")
                }
            }
          }
      } ~
        pathPrefix(PathElement) { leagueName =>
          path("") {
            get { ctx =>
              MemoryRepository.getLeague(leagueName) match {
                case Some(league) => ctx.complete(league.toJson.compactPrint)
                case None => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + " is not found.")
              }
            } ~
              delete { ctx =>
                MemoryRepository.deleteLeague(leagueName) match {
                  case true => ctx.complete("League with name=" + leagueName + " deleted.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with name=" + leagueName + " is not found.")
                }
              }
          } ~
            path("table") {
              get { ctx =>
                MemoryRepository.getLeague(leagueName) match {
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
                      MemoryRepository.addPlayerToLeague(leagueName, player) match {
                        case true => ctx.complete("Player with name=" + player.name + " created in league with name=" + leagueName + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Player with name=" + player.name + " could not be created in league with name=" + leagueName + ".")
                      }
                  }
                }
              } ~
                path(Remaining) { playerName =>
                  delete { ctx =>
                    MemoryRepository.deletePlayerFromLeague(leagueName, playerName) match {
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
                      MemoryRepository.addGameToLeague(leagueName, game) match {
                        case true => ctx.complete("Game with timestamp=" + game.timestamp + " created in league with name=" + leagueName + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Game with timestamp=" + game.timestamp + " could not be created in league with name=" + leagueName + ".")
                      }
                  }
                }
              } ~
                path(LongNumber) { gameTimestamp =>
                  delete { ctx =>
                    MemoryRepository.deleteGameFromLeague(leagueName, gameTimestamp) match {
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
