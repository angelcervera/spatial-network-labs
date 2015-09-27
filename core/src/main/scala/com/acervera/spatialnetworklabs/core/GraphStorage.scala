package com.acervera.spatialnetworklabs.core

/**
 * Trait tha must implement all storage systems.
 */
trait GraphStorage {

  /**
   * Retrieve a vertex, searching by Id.
   *
   * @param id Unique Id vertex
   * @return An Vertex Object.
   */
  def vertex(id: Long): Vertex

  /**
   * Return outs from the Vertex
   *
   * @param id Id of the Vertex.
   * @return List of Edges.
   */
  def vertexOuts(id: Long): List[Edge]

}
