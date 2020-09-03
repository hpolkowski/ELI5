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
  "io.getquill" %% "quill-jdbc" % "3.0.1",

  // Silhouette
  "com.mohiva" %% "play-silhouette" % "5.0.6",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.6",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.6",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.6",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.6" % "test",

  // Ficus wymagany do Silhouette
  "com.iheart" %% "ficus" % "1.4.3",

  // Scala Guice wymagany do Silhouette
  "net.codingwell" %% "scala-guice" % "4.1.0",

  // Bootstrap 4
  "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4",
  "org.webjars" % "bootstrap" % "4.1.3",
  "org.webjars" % "datatables" % "1.10.21",

  // JQuery
  "org.webjars" % "jquery" % "2.2.3",

  // Font Awesome
  "org.webjars" % "font-awesome" % "4.7.0",

  // Simple Line Icons
  "org.webjars.bower" % "simple-line-icons" % "2.4.1",

  // Popper.js
  "org.webjars" % "popper.js" % "1.14.4",

  // TinyMCE - edytor tekstu
  "org.webjars" % "tinymce" % "5.0.9",

  // Obsługa maili
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
