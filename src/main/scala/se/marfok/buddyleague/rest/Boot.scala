package se.marfok.buddyleague.rest

import akka.actor.Actor.actorOf
import akka.actor.Supervisor
import akka.config.Supervision.OneForOneStrategy
import akka.config.Supervision.Permanent
import akka.config.Supervision.Supervise
import akka.config.Supervision.SupervisorConfig
import cc.spray.HttpService
import cc.spray.RootService
import se.marfok.buddyleague.domain.MemoryRepository

class Boot {
  
  val mainModule = new BuddyLeagueService {
    // bake your module cake here
  }

  val restService = actorOf(new HttpService(mainModule.restService))
  val rootService = actorOf(new RootService(restService))

  Supervisor(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Exception]), 3, 100),
      List(
        Supervise(restService, Permanent),
        Supervise(MemoryRepository.storeActor, Permanent),
        Supervise(rootService, Permanent)
      )
    )
  )
}