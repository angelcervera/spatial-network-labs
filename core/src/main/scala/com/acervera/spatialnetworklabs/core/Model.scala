package com.acervera.spatialnetworklabs.core

import scala.collection.immutable.HashMap

/**
 * Spatial node in the network.
 *
 * @param id Unique id that represent a node.
 * @param longitude Longitude in grades
 * @param latitude Latitude in grades
 * @param attributes Attributes related with the vertex
 */
class Vertex(id: Long, longitude: Long, latitude: Long, attributes: HashMap[Serializable, Serializable])

/**
 * Represent the union between two different nodes.
 *
 * @param id Unique id that represent a vertex
 * @param source Node source
 * @param target Node target
 * @param attributes Attributes related with the edge
 */
class Edge(id: Long, source: Vertex, target: Vertex, attributes: HashMap[Serializable, Serializable])
