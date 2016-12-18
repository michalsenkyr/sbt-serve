sbtPlugin := true

name := "sbt-serve"

organization := "com.github.michalsenkyr"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.16",
  "io.spray" %% "spray-routing" % "1.3.3",
  "io.spray" %% "spray-can" % "1.3.3"
)