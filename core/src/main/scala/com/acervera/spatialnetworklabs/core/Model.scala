package com.acervera.spatialnetworklabs.core

/**
 * Spatial node in the network.
 *
 * @param id Unique id that represent a node.
 * @param longitude Longitude in grades
 * @param latitude Latitude in grades
 * @param attributes Attributes related with the vertex
 */
class Vertex(val id: Long, val longitude: Long, val latitude: Long, val label: String, val attributes: Map[Any, Any])

/**
 * Represent the union between two different nodes.
 *
 * @param id Unique id that represent a vertex
 * @param source Node source
 * @param target Node target
 * @param attributes Attributes related with the edge
 */
class Edge(val id: Long, val source: Vertex, val target: Vertex, val label: String, val attributes: Map[Any, Any])
