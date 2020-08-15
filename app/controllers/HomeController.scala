package controllers

import bootstrap.AppConfig
import com.mohiva.play.silhouette.api.Silhouette
import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.auth.CookieEnvironment

import scala.concurrent.ExecutionContext

/**
 * Kontroler głównej strony aplikacji
 */
@Singleton
class HomeController @Inject()(
  components: ControllerComponents
)(
  implicit
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {

  /**
   * Główna strona aplikacji
   */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

}
