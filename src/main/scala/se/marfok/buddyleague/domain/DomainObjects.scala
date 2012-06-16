package se.marfok.buddyleague.domain

import cc.spray.utils.Logging

case class Score(home: Int, away: Int) {
  def +(other: Score): Score = Score(home + other.home, away + other.away)
  def -(other: Score): Score = Score(home - other.home, away - other.away)
  def invert(): Score = Score(away, home)
}
case class Player(name: String)
case class Game(timestamp: Long, home: Set[String], away: Set[String], score: Score)
case class League(name: String, players: List[Player], games: List[Game]) {

  def getTable: List[(Player, Score)] = {
    var playerScores: collection.mutable.Map[Player, Score] = collection.mutable.Map() ++ players.map(p => p -> Score(0, 0)).toMap
    for (game <- games; homePlayerName <- game.home) {
      getPlayer(homePlayerName) match {
        case Some(homePlayer) => playerScores(homePlayer) = playerScores(homePlayer) + game.score
        case None => {}
      }
    }
    for (game <- games; awayPlayerName <- game.away) {
      getPlayer(awayPlayerName) match {
        case Some(awayPlayer) => playerScores(awayPlayer) = playerScores(awayPlayer) + game.score.invert
        case None => {}
      }
    }
    playerScores.toList.sortWith((e1, e2) => e1._2.home - e1._2.away > e2._2.home - e2._2.away)
  }

  def getPlayer(name: String): Option[Player] = {
    players.filter(_.name == name) match {
      case Nil => None
      case player :: Nil => Some(player)
      case player :: tail => Some(player)
    }
  }

  def getGame(timestamp: Long): Option[Game] = {
    games.filter(_.timestamp == timestamp) match {
      case Nil => None
      case game :: Nil => Some(game)
      case game :: tail => Some(game)
    }
  }
}