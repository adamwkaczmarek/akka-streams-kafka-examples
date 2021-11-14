lazy val commonSettings = commonSmlBuildSettings ++ ossPublishSettings ++ Seq(
  organization := "com.softwaremill.k8s",
  scalaVersion := "2.12.8"
)

val http4sVersion = "0.20.1"
val prometheusVersion = "0.6.0"
val AkkaVersion = "2.5.31"

lazy val rootProject = (project in file("."))
  .settings(commonSettings: _*)
  .settings(publishArtifact := false, name := "akka-streams-kafka-example-app")
  .aggregate(core)

lazy val core: Project = (project in file("core"))
  .enablePlugins(JavaServerAppPackaging)
  .enablePlugins(DockerPlugin)
  .settings(commonSettings: _*)
  .settings(
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "openjdk:8u212-jdk-stretch",
    dockerUsername := Some("softwaremill"),
    packageName in Docker := "akka-streams-kafka-example-app",
    dockerUpdateLatest := true
  )
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "io.prometheus" % "simpleclient_common" % prometheusVersion,
      "io.prometheus" % "simpleclient_hotspot" % prometheusVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.7",
      "org.testcontainers" % "kafka" % "1.15.1" % Test,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "com.typesafe.akka" %% "akka-stream-kafka-testkit" % "2.0.7" % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
    )
  )

