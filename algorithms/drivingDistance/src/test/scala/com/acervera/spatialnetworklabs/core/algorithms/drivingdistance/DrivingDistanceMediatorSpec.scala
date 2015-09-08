package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import akka.actor.ActorSystem
import akka.testkit.EventFilter
import org.scalatest.{WordSpec, Matchers, BeforeAndAfterAll}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DrivingDistanceMediatorSpec extends WordSpec with Matchers with BeforeAndAfterAll {

  implicit val system = ActorSystem("driving-distance-mediator-spec-system")

  "A DrivingDistanceMediator" should {
    "be suited for getting started" in {
      EventFilter.debug(pattern = s"started.*${classOf[DrivingDistanceMediator].getName}", occurrences = 1).intercept {
        system.actorOf(DrivingDistanceMediator.props)
      }
    }
  }

  override protected def afterAll() = {
    Await.ready(system.terminate(), Duration.Inf)
    super.afterAll()
  }
}
