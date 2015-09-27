package com.acervera.spatialnetworklabs.core

/**
 * Spatial node in the network.
 *
 * @param id Unique id that represent a node.

 * @param attributes Attributes related with the vertex
 */
case class Vertex(val id: Long, val coordinate: Coordinate, val label: String, val attributes: Map[Any, Any])

/**
 * Represent the union between two different nodes.
 *
 * @param id Unique id that represent a vertex
 * @param inId Vertex source id
 * @param outId Vertex target id
 * @param attributes Attributes related with the edge
 */
case class Edge(val id: Long, val inId: Long, val outId: Long, val label: String, val attributes: Map[Any, Any])

/**
 *
 * @param longitude Longitude in grades
 * @param latitude Latitude in grades
 * @param altitude Altitude in meters.
 */
case class Coordinate(val longitude: Long, val latitude: Long, var altitude: Double = 0)
