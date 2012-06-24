package se.marfok.buddyleague.rest

import org.slf4j.LoggerFactory
import akka.config.Supervision._
import akka.actor.{ Supervisor, Actor }
import cc.spray.{ SprayCanRootService, HttpService }
import cc.spray.can.{ HttpServer, ServerConfig }
import se.marfok.buddyleague.domain.{ MemoryRepository, MongoDbRepository }

object Boot extends App {

  LoggerFactory.getLogger(getClass) // initialize SLF4J early

  val mainModule = new BuddyLeagueService {
    // bake your module cake here
  }

  val host = "0.0.0.0"
  val port = Option(System.getenv("PORT")).getOrElse("8080").toInt

  val httpService = Actor.actorOf(new HttpService(mainModule.restService))
  val rootService = Actor.actorOf(new SprayCanRootService(httpService))
  val sprayCanServer = Actor.actorOf(new HttpServer(new ServerConfig(host = host, port = port)))

  Supervisor(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Exception]), 3, 100),
      List(
        Supervise(MemoryRepository.storeActor, Permanent),
        Supervise(MongoDbRepository.storeActor, Permanent),
        Supervise(httpService, Permanent),
        Supervise(rootService, Permanent),
        Supervise(sprayCanServer, Permanent))))

  populateTestData()

  def populateTestData(): Unit = {
    MemoryRepository.populateTestData()
    MongoDbRepository.populateTestData()
  }
}