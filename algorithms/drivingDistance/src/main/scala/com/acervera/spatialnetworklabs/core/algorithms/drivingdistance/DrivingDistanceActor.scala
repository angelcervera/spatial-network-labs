package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import akka.actor.{Props, Actor}
import akka.event.Logging
import com.acervera.spatialnetworklabs.core.algorithms.drivingdistance.DrivingDistanceActor.GoForwardEvent
import com.acervera.spatialnetworklabs.core.{Edge, Vertex}

object DrivingDistanceActor {
  def props: Props = Props(new DrivingDistanceActor)

  // List of messages.

  /**
   * Message sended to the next forward.
   *
   * @param source Origin
   * @param path Path to forward.
   * @param weight Current weight
   * @param limits Ordered list of weight limits.
   * @param weightAttribute Name of the parameter that contain the weight of the path
   */
  case class GoForwardEvent(source: Vertex, path: Edge, weight: Double, limits: List[Double], weightAttribute: String)

  /**
   * Limit point.
   *
   * @param path Path info where cross the limit.
   * @param longitude Longitude coordenate where crossed
   * @param latitude Latitud coordenate where crossed
   * @param limit Limited crossed
   */
  case class CrossedLimitEvent(path: Edge, longitude: Long, latitude: Long, limit: Double)

}

class DrivingDistanceActor extends Actor {

  val log = Logging(context.system, this)

  override def receive = {

    // Retrieve next vertexes and go forward.
    case GoForwardEvent(source, path, weight, limits, weightAttribute) => {

      // TODO: Retrieve next vertexes from storage system.
      val vertexes = List(
        new Vertex(0, 0, 0, "label", Map("prop1" -> "val1", "prop2" -> 3d)),
        new Vertex(1, 1, 1, "label", Map("prop3" -> "val1", "prop4" -> 3d))
      )

      vertexes foreach (vertex => {
        val att = vertex.attributes(weightAttribute)
        // TODO: If cross the limit, send an CrossedLimitEvent to the sender, and stop if it is the last limit.
        // TODO: If does not cross the limit, continue with the next vertex.
      })


    }

  }
}
