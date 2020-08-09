package bootstrap

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppConfig @Inject()(config: Configuration) {

  val appName = config.get[String]("app.name")

  val appVersion = config.get[String]("app.version")

  val appDomain = config.getOptional[String]("app.domain").getOrElse("localhost:9000")

  val contactEmail = config.getOptional[String]("play.mailer.email.contact").getOrElse("contact@eli5.com")

  val noReplyEmail = config.getOptional[String]("play.mailer.email.no-reply").getOrElse("no-reply@eli5.com")
}
