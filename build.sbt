name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator



//PlayKeys.fileWatchService := play.runsupport.FileWatchService.sbt(pollInterval.value)
PlayKeys.fileWatchService := play.runsupport.FileWatchService.sbt(2000)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.1"
