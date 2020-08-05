import com.typesafe.config.ConfigFactory

val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
lazy val `eli5` = (project in file(".")).enablePlugins(PlayScala)

name := conf.getString("app.name")

version := conf.getString("app.version")

packageDescription := """ELI5 Application"""

maintainer := """Hubert Polkowski <hpolkowski@wp.pl>"""

serverLoading in Debian := Some(ServerLoader.Systemd)

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)
      
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  jdbc , ehcache , ws , specs2 % Test , guice, logback, evolutions, akkaHttpServer,

  // Sterownik Postresa
  "org.postgresql" % "postgresql" % "9.4.1208",

  // Quill
  "io.getquill" %% "quill-jdbc" % "3.0.1"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
