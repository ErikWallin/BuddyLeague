package se.marfok.buddyleague.domain

case class Score(home: Int, away: Int) {
  def +(other: Score): Score = Score(home + other.home, away + other.away)
  def -(other: Score): Score = Score(home + other.away, away + other.home)
}
case class Player(id: Long, firstName: String, surName: String)
case class Game(id: Long, home: Set[Player], away: Set[Player], score: Score)
case class League(id: Long, players: List[Player], games: List[Game]) {
  def getTable: List[(Player, Score)] = {
    var playerScores: collection.mutable.Map[Player, Score] = collection.mutable.Map() ++ players.map(p => p -> Score(0, 0)).toMap
    for (game <- games; homePlayer <- game.home) {
      playerScores(homePlayer) = playerScores(homePlayer) + game.score
    }
    for (game <- games; awayPlayer <- game.away) {
      playerScores(awayPlayer) = playerScores(awayPlayer) - game.score
    }
    playerScores.toList
  }
}