ThisBuild / organization := "com.scalawags"

name := "KeplerDataReader"
version := "1.0"
scalaVersion := "2.13.4"

lazy val kepler = (project in file(".")).settings(
  name := "Kepler Data Reader",
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  libraryDependencies += "org.mongodb.scala" % "mongo-scala-driver_2.13" % "4.2.0-beta1",
  libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
)