package controllers

import javax.inject._
import play.api.data.{Form, Forms}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.HomeService

import scala.concurrent.{ExecutionContext, Future}

/**
 * Kontroler głównej strony aplikacji
 */
@Singleton
class HomeController @Inject()(
  components: ControllerComponents,
  homeService: HomeService
)(
  implicit
  assets: AssetsFinder,
  context: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  val Home = Redirect(routes.HomeController.index())

  /**
    * Główna strona aplikacji
    */
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  /**
    * Obsługa zapisu do listy mailingowej newsletrtera
    */
  def newsletterSignUp = Action.async { implicit request =>

    Form("newsletterEmail" -> Forms.email).bindFromRequest().fold(
      formWithErrors => Future.successful(Home),

      data =>
        homeService.signUpForNewsletter(data).map { _ =>
          Home.flashing("success" -> "landing.newsletter.signup.success")
        }
    )
  }

}
