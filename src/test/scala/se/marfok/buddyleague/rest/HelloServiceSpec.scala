package se.marfok.buddyleague.rest

import org.specs2.mutable.Specification

import cc.spray.http.HttpContent.pimpHttpContentWithAs2
import cc.spray.http.HttpMethods.GET
import cc.spray.http.HttpMethods.POST
import cc.spray.http.StatusCodes.MethodNotAllowed
import cc.spray.http.HttpRequest
import cc.spray.http.HttpResponse
import cc.spray.test.SprayTest

class HelloServiceSpec extends Specification with SprayTest with BuddyLeagueService {
  
  "The HelloService" should {
    "leave GET requests to other paths unhandled" in {
      testService(HttpRequest(GET, "/kermit")) {
        restService
      }.handled must beFalse
    }
  }
}
