package com.acervera.spatialnetworklabs.storage.inmemory

import com.acervera.spatialnetworklabs.core.{Coordinate, Vertex, Edge, GraphStorage}

class InMemoryGraphStorage extends GraphStorage {

  /**
   * Retrieve a vertex, searching by Id.
   *
   * @param id Unique Id vertex
   * @return An Vertex Object.
   */
  override def vertex(id: Long): Vertex = {
    Vertex(0, Coordinate(0, 0), "", Map()) // TODO
  }

  /**
   * Return outs from the Vertex
   *
   * @param id Id of the Vertex.
   * @return List of Edges.
   */
  override def vertexOuts(id: Long): List[Edge] = {
    List()
  }

}
