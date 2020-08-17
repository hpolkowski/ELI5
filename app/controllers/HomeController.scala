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
    * Lista artykułów
    *
    * @param page     aktualna strona
    * @param filter   filtry
    */
  def index(page: Int, filter: String) = Action.async { implicit request =>
    homeService.listArticle(page, filter).map { articles =>
      Ok(views.html.index(articles, filter))
    }
  }

  /**
    * Przeszukiwanie listy artykułów
    */
  def search = Action.async { implicit request =>

    Form("search" -> Forms.text).bindFromRequest().fold(
      formWithErrors => Future.successful(Home),

      filter =>
        homeService.listArticle(1, filter).map { articles =>
          Ok(views.html.index(articles, filter))
        }
    )
  }

  /**
    * Szczeguły artykułu
    *
    * @param url adres artykułu
    */
  def details(url: String) = Action.async { implicit request =>
    homeService.retrieveArticle(url).map {

      case Some(article) =>
        Ok(views.html.details(article))

      case None =>
        Home.flashing("failure" -> "landing.articles.details.notFound")
    }
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
