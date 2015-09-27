package com.acervera.spatialnetworklabs.core.algorithms.drivingdistance

import com.acervera.spatialnetworklabs.core.{Coordinate, Vertex, GraphStorage}

/**
 * Implementation of the Driving Distance algorithm, used to create spanning trees of coverage.
 */
class DrivingDistanceMain(storage: GraphStorage) {

  /**
   * Starting from the source vertex, calculate all the spanning tree up the nodes with a cost to arrive near or equal
   * to "weight", but only return the set of vertexes that are in the perimeter (the last node in every path).
   * Edges must have two attributes:
   * - "costBackward"
   * - "costForward"
   *
   * @param source Origin from will start.
   * @param weight List of weights.
   * @return A map with one set of Vertexes (the value) per weight (the key).
   */
  def start(source: Vertex, weight: Long*): Map[Long, Set[Vertex]] = {

    // TODO: costBackward and costForward keys should be parameterizable.

    // TODO: Implement. At the moment, Mocked
    Map(1L -> Set(
      new Vertex(0, Coordinate(0, 0), "label", Map("prop1" -> "val1", "prop2" -> 3d)),
      new Vertex(1, Coordinate(1, 1), "label", Map())
    ))
  }

}
