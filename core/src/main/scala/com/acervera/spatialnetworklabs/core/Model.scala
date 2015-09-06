package com.acervera.spatialnetworklabs.core

/**
 * Spatial node in the network.
 *
 * @param id OSM Id
 * @param longitude Longitude in grades
 * @param latitude Latitude in grades
 */
class Node(id: Long, longitude: Long, latitude: Long)

/**
 * Represent the union between two different nodes.
 *
 * @param id OSM Id
 * @param source Node source
 * @param target Node target
 * @param linestring Spatial representation.
 * @param wayType Type of way
 * @param length meters
 * @param costCarForward Cost for forward direction in minutes.
 * @param costCarBackward Cost for backward direction in minutes.
 * @param costPedestrian Cost for pedestrian in minutes and any direction.
 */
class Vertex(
              id: Long, // OSM id
              source: Node,
              target: Node,
              linestring: String,
              wayType: String,
              length: Double,
              costCarForward: Double,
              costCarBackward: Double,
              costPedestrian: Double
              )

class HighwayType(country: String, typeWay: String, carMaxSpeed: Double)

class Way(
           id: Long = -1,
           oneWay: Integer = 0,
           country: String,
           carMaxSpeedForward: Double = -1,
           carMaxSpeedBackward: Double = -1
           )
