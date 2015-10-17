package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import akka.actor.{Props, Actor}
import akka.event.Logging
import com.acervera.spatialnetworklabs.core.algorithms.drivingdistance.DrivingDistanceActor.GoForwardEvent
import com.acervera.spatialnetworklabs.core.{Coordinate, GraphStorage, Edge, Vertex}
import com.acervera.spatialnetworklabs.storage.inmemory.InMemoryGraphStorage

object DrivingDistanceActor {

  // val inMemoryStorage = InMemoryGraphStorage()

  // def props: Props = Props(new DrivingDistanceActor(inMemoryStorage))

  // List of messages.

  /**
   * Message sended to the next forward.
   *
   * @param sourceVertexId Origin id
   * @param sourceEdgeId Path id to forward.
   * @param currentWeight Current weight
   * @param limits Ordered list of weight limits.
   * @param weightAttribute Name of the parameter that contain the weight of the path
   */
  case class GoForwardEvent(sourceVertexId: Long, sourceEdgeId: Long, currentWeight: Double, limits: List[Double], weightAttribute: String)

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

class DrivingDistanceActor(graphStorage: GraphStorage) extends Actor {

  val log = Logging(context.system, this)

  var limitCoordenates: Set[Coordinate] = Set()

  override def receive = {

    // Retrieve next vertexes and go forward.
    case GoForwardEvent(sourceVertexId, sourceEdgeId, currentWeight, limits, weightAttribute) => {

      val outPaths = graphStorage vertexOuts sourceVertexId

      outPaths foreach (path => {
        if (path.id != sourceEdgeId) {
          val w = path.attributes(weightAttribute).asInstanceOf[Double] // FIXME: Must be a Double. Convert if it is not ??


          val newWeight = currentWeight + w;
          if (newWeight < limits(0)) {
            sender() ! GoForwardEvent(path.outId, path.id, newWeight, limits, weightAttribute)
          } else {
            limitCoordenates += graphStorage.vertex(sourceVertexId).coordinate // FIXME: Calculate the real coordenate in the path.
          }
        }

      })


    }

  }
}
