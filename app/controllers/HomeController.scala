package controllers

import javax.inject._
import play.api.data.{Form, Forms}
import play.api.i18n.{I18nSupport, Lang}
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

  val Home = Redirect(routes.HomeController.index(1, 0))

  /**
    * Lista artykułów
    *
    * @param page     aktualna strona
    * @param orderBy  kolejność sortowania
    * @param filter   filtry
    */
  def index(page: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    homeService.listArticle(page, orderBy, filter).map { articles =>
      Ok(views.html.index(articles, orderBy, filter))
    }
  }

  /**
    * Przeszukiwanie listy artykułów
    */
  def search = Action.async { implicit request =>

    Form("search" -> Forms.text).bindFromRequest().fold(
      formWithErrors => Future.successful(Home),

      filter =>
        homeService.listArticle(1, 0, filter).map { articles =>
          Ok(views.html.index(articles, 0, filter))
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

  /**
    * Zmiana języka
    *
    * @param lang język docelowy
    */
  def changeLanguage(lang: String) = Action { implicit request =>
    val url = request.headers.toMap("referer").headOption.getOrElse(routes.HomeController.index(1, 0).absoluteURL)

    Redirect(url).withLang(Lang(lang))
  }

}
