package se.marfok.buddyleague.domain

import cc.spray.json._
import cc.spray.json.DefaultJsonProtocol._

object DomainObjectJsonConverters extends DefaultJsonProtocol {
  implicit val scoreFormat = jsonFormat2(Score)
  implicit val playerFormat = jsonFormat3(Player)
  implicit val gameFormat = jsonFormat4(Game)
  implicit val leagueFormat = jsonFormat3(League)
}