import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.nobbyknox"
ThisBuild / organizationName := "file-watch-poc.lion"

lazy val root = (project in file("."))
  .settings(
    name := "lion",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.h2database" % "h2" % "1.4.199",
      "com.sparkjava" % "spark-core" % "2.9.0",
      "commons-cli" % "commons-cli" % "1.4",
      "org.slf4j" % "slf4j-log4j12" % "1.7.26",
      "org.clapper" %% "grizzled-slf4j" % "1.3.3"
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
