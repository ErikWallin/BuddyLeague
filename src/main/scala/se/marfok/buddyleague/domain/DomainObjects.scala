package se.marfok.buddyleague.domain

import cc.spray.utils.Logging

case class Score(home: Int, away: Int) {
  def +(other: Score): Score = Score(home + other.home, away + other.away)
  def -(other: Score): Score = Score(home - other.home, away - other.away)
  def invert(): Score = Score(away, home)
}
case class Player(id: Long, firstName: String, surName: String)
case class Game(id: Long, home: Set[Long], away: Set[Long], score: Score)
case class League(id: Long, players: List[Player], games: List[Game]) {
  def getTable: List[(Player, Score)] = {
    var playerScores: collection.mutable.Map[Player, Score] = collection.mutable.Map() ++ players.map(p => p -> Score(0, 0)).toMap
    for (game <- games; homePlayerId <- game.home) {
      getPlayer(homePlayerId) match {
        case Some(homePlayer) => playerScores(homePlayer) = playerScores(homePlayer) + game.score
        case None => {}
      }
    }
    for (game <- games; awayPlayerId <- game.away) {
      getPlayer(awayPlayerId) match {
        case Some(awayPlayer) => playerScores(awayPlayer) = playerScores(awayPlayer) + game.score.invert
        case None => {}
      }
    }
    playerScores.toList.sortWith((e1, e2) => e1._2.home - e1._2.away > e2._2.home - e2._2.away)
  }

  def getPlayer(id: Long): Option[Player] = {
    players.filter(_.id == id) match {
      case Nil => None
      case player :: Nil => Some(player)
      case player :: tail => Some(player)
    }
  }

  def getGame(id: Long): Option[Game] = {
    games.filter(_.id == id) match {
      case Nil => None
      case game :: Nil => Some(game)
      case game :: tail => Some(game)
    }
  }
}