package bootstrap

import javax.inject.{Inject, Singleton}
import play.api.Configuration

@Singleton
class AppConfig @Inject()(config: Configuration) {

  val appName = config.get[String]("app.name")

  val appVersion = config.get[String]("app.version")
}
