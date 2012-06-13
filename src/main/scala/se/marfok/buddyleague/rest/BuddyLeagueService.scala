package se.marfok.buddyleague.rest

import cc.spray.directives.{LongNumber, Remaining}
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
                  case true => ctx.complete("League with id=" + league.id + " created.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with id=" + league.id + " could not be created.")
                }
            }
          }
      } ~
        pathPrefix(LongNumber) { leagueId =>
          path("") {
            get { ctx =>
              MemoryRepository.getLeague(leagueId) match {
                case Some(league) => ctx.complete(league.toJson.compactPrint)
                case None => ctx.fail(StatusCodes.NotFound, "League with id=" + leagueId + " is not found.")
              }
            } ~
              delete { ctx =>
                MemoryRepository.deleteLeague(leagueId) match {
                  case true => ctx.complete("League with id=" + leagueId + " deleted.")
                  case false => ctx.fail(StatusCodes.NotFound, "League with id=" + leagueId + " is not found.")
                }
              }
          } ~
            path("table") {
              get { ctx =>
                MemoryRepository.getLeague(leagueId) match {
                  case Some(league) => ctx.complete(league.getTable.toJson.compactPrint)
                  case None => ctx.fail(StatusCodes.NotFound, "League with id=" + leagueId + " is not found.")
                }
              }
            } ~
            pathPrefix("player") {
              path("") {
                post {
                  content(as[Player]) { player =>
                    ctx =>
                      MemoryRepository.addPlayerToLeague(leagueId, player) match {
                        case true => ctx.complete("Player with name=" + player.name + " created in league with id " + leagueId + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Player with name=" + player.name + " could not be created in league with id " + leagueId + ".")
                      }
                  }
                }
              } ~
                path(Remaining) { playerName =>
                  delete { ctx =>
                    MemoryRepository.deletePlayerFromLeague(leagueId, playerName) match {
                      case true => ctx.complete("Player with name=" + playerName + " deleted in league with id " + leagueId + ".")
                      case false => ctx.fail(StatusCodes.NotFound, "League with id=" + leagueId + "or player with name " + playerName + " is not found.")
                    }
                  }
                }
            } ~
            pathPrefix("game") {
              path("") {
                post {
                  content(as[Game]) { game =>
                    ctx =>
                      MemoryRepository.addGameToLeague(leagueId, game) match {
                        case true => ctx.complete("Game with id=" + game.id + " created in league with id " + leagueId + ".")
                        case false => ctx.fail(StatusCodes.NotFound, "Game with id=" + game.id + " could not be created in league with id " + leagueId + ".")
                      }
                  }
                }
              } ~
                path(LongNumber) { gameId =>
                  delete { ctx =>
                    MemoryRepository.deleteGameFromLeague(leagueId, gameId) match {
                      case true => ctx.complete("Game with id=" + gameId + " deleted in league with id " + leagueId + ".")
                      case false => ctx.fail(StatusCodes.NotFound, "League with id=" + leagueId + "or game with id " + gameId + " is not found.")
                    }
                  }
                }
            }
        }
    }
  }
}
