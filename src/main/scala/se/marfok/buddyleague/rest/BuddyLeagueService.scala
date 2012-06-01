package se.marfok.buddyleague.rest

import cc.spray.directives.LongNumber
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
    pathPrefix("player") {
      path(LongNumber) { id =>
        get { ctx =>
          MemoryRepository.getPlayer(id) match {
            case Some(player) => ctx.complete(player.toJson.compactPrint)
            case None => ctx.fail(StatusCodes.NotFound, "Player with id=" + id + " is not found.")
          }
        } ~
          put {
            content(as[Player]) { player =>
              ctx =>
                MemoryRepository.updatePlayer(player) match {
                  case true => ctx.complete("Player with id=" + player.id + " updated.")
                  case false => ctx.fail(StatusCodes.NotFound, "Player with id=" + player.id + " could not be updated.")
                }
            }
          } ~
          delete { ctx =>
            MemoryRepository.deletePlayer(id) match {
              case true => ctx.complete("Player with id=" + id + " deleted.")
              case false => ctx.fail(StatusCodes.NotFound, "Player with id=" + id + " is not found.")
            }
          }
      } ~
        path("") {
          get {
            _.complete(MemoryRepository.getPlayers.toJson.compactPrint)
          } ~
            post {
              content(as[Player]) { player =>
                ctx =>
                  MemoryRepository.createPlayer(player) match {
                    case true => ctx.complete("Player with id=" + player.id + " created.")
                    case false => ctx.fail(StatusCodes.NotFound, "Player with id=" + player.id + " could not be created.")
                  }
              }
            }
        }
    } ~
      pathPrefix("game") {
        path(LongNumber) { id =>
          get { ctx =>
            MemoryRepository.getGame(id) match {
              case Some(game) => ctx.complete(game.toJson.compactPrint)
              case None => ctx.fail(StatusCodes.NotFound, "Game with id=" + id + " is not found.")
            }
          }
        } ~
          path("") {
            get {
              _.complete(MemoryRepository.getGames.toJson.compactPrint)
            } ~
              post {
                content(as[Game]) { game =>
                  ctx =>
                    MemoryRepository.createGame(game) match {
                      case true => ctx.complete("Game with id=" + game.id + " created.")
                      case false => ctx.fail(StatusCodes.NotFound, "Game with id=" + game.id + " could not be created.")
                    }
                }
              }
          }
      } ~
      pathPrefix("league") {
        path(LongNumber) { id =>
          get { ctx =>
            MemoryRepository.getLeague(id) match {
              case Some(league) => ctx.complete(league.toJson.compactPrint)
              case None => ctx.fail(StatusCodes.NotFound, "League with id=" + id + " is not found.")
            }
          }
        } ~
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
          path(LongNumber / "table") { id =>
            get { ctx =>
              MemoryRepository.getLeague(id) match {
                case Some(league) => ctx.complete(league.getTable.toJson.compactPrint)
                case None => ctx.fail(StatusCodes.NotFound, "League with id=" + id + " is not found.")
              }
            }
          } ~
          path(LongNumber / "game") { leagueId =>
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
          path(LongNumber / "player") { leagueId =>
            post {
              content(as[Player]) { player =>
                ctx =>
                  MemoryRepository.addPlayerToLeague(leagueId, player) match {
                    case true => ctx.complete("Player with id=" + player.id + " created in league with id " + leagueId + ".")
                    case false => ctx.fail(StatusCodes.NotFound, "Player with id=" + player.id + " could not be created in league with id " + leagueId + ".")
                  }
              }
            }
          }
      }
  }
}