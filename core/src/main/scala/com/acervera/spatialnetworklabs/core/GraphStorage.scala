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
  def retrieveVertex(id: Long): Vertex

}
