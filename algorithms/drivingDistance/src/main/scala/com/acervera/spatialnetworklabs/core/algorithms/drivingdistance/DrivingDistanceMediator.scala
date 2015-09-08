package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import akka.actor.{Props, Actor}

object DrivingDistanceMediator {
  def props: Props = Props(new DrivingDistanceMediator)
}

class DrivingDistanceMediator extends Actor {
  override def receive: Receive = Actor.emptyBehavior
}
