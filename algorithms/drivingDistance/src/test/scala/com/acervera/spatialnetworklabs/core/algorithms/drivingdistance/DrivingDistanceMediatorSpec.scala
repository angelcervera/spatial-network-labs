package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import akka.actor.ActorSystem
import akka.testkit.EventFilter
import org.scalatest.{WordSpec, Matchers, BeforeAndAfterAll}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DrivingDistanceMediatorSpec extends WordSpec with Matchers with BeforeAndAfterAll {

  implicit val system = ActorSystem("driving-distance-mediator-spec")

  "A DrivingDistanceMediator" should {
    "send messages to next nodes" in {
      // fail("Not implemented.")
    }

    "send messages to next nodes, excluding previously routes" in {
      // fail("Not implemented.")
    }
  }

  override protected def afterAll() = {
    Await.ready(system.terminate(), Duration.Inf)
    super.afterAll()
  }
}
