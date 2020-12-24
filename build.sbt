ThisBuild / organization := "com.scalawags"

name := "KeplerDataReader"
version := "1.0"
scalaVersion := "2.13.4"
fork in (IntegrationTest, run) := true

lazy val kepler = (project in file(".")).settings(
  name := "Kepler Data Reader"
)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
libraryDependencies += "org.mongodb.scala" % "mongo-scala-driver_2.13" % "4.2.0-beta1"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
