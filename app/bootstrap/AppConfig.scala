package bootstrap

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppConfig @Inject()(config: Configuration) {

  val appName = config.get[String]("app.name")

  val appVersion = config.get[String]("app.version")

  val noReply = config.getOptional[String]("app.email.no-reply").getOrElse("no-reply@goodsoft.pl")

  val appDomain = config.getOptional[String]("app.domain").getOrElse("localhost:9000")

  val filePath = config.getOptional[String]("fileSystem.basePath").getOrElse("kameleon_files/")
}
