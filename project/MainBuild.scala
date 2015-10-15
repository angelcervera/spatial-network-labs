import sbt.Keys._
import sbt._

object MainBuild extends Build {

  lazy val akkaVersion = "2.4.0-RC3"
  lazy val akkaLibraries = List(
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion % "test",
    "org.scalacheck" %% "scalacheck" % "1.12.4" % "test",
    "org.scalatest" %% "scalatest" % "2.2.5" % "test"
  )

  lazy val sparkVersion = "1.5.1"
  lazy val osmosisVersion = "0.44.1"

  lazy val commonSettings = Seq(
    organization := "com.acervera.labs.spatial",
    organizationHomepage := Some(url("http://www.acervera.com")),
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7",
    publishMavenStyle := true
  )

  lazy val algorithmsCommonSettings = commonSettings ++ Seq(
    organization := "com.acervera.labs.spatial.algorithms"
  )

  lazy val root = project.in(file("."))
    .settings(commonSettings: _*)
    .settings(
      description := "Implementation of a server to improve different algorithms over spatial networks and concurrent / parallel processing.",
      libraryDependencies ++= akkaLibraries
    )
    .aggregate(drivingDistance, inmemory, core, loadOsm)

  lazy val core = project.in(file("core"))
    .settings(commonSettings: _*)
    .settings(
      description := "Main resources used by all modules. Contains the network model, etc...",
      libraryDependencies ++= akkaLibraries
    )

  lazy val drivingDistance = project.in(file("algorithms/drivingDistance"))
    .settings(algorithmsCommonSettings: _*)
    .settings(
      description := "Algorithm that from one node, retrieve the list of node where you can arrive under a determined cost.",
      libraryDependencies ++= akkaLibraries
      // libraryDependencies += "org.fluentd" % "fluent-logger" % "0.2.10"
    )
    .dependsOn(inmemory, core)

  lazy val inmemory = project.in(file("network-storages/inmemory"))
    .settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= akkaLibraries
    )
    .dependsOn(core)

  lazy val loadOsm = project.in(file("load-osm"))
    .settings(commonSettings: _*)
    .settings(
      description := "Load OSM file into GraphX",
      libraryDependencies ++= List(
        // "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
        "org.openstreetmap.osmosis" % "osmosis-osm-binary" % osmosisVersion,
        "org.clapper" %% "argot" % "1.0.3"
      )
    )

}

