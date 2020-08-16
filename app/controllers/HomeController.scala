package controllers

import javax.inject._
import play.api.data.{Form, Forms}
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.{ArticleService, HomeService}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Kontroler głównej strony aplikacji
 */
@Singleton
class HomeController @Inject()(
  components: ControllerComponents,
  homeService: HomeService,
  articleService: ArticleService
)(
  implicit
  assets: AssetsFinder,
  context: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  val Home = Redirect(routes.HomeController.index())

  /**
    * Główna strona aplikacji
    *
    * @param page     aktualna strona
    * @param pageSize rozmiar strony
    * @param orderBy  parametr sortowania
    * @param filter   filtry
    */
  def index(page: Int, pageSize: Int, orderBy: Int, filter: String) = Action.async { implicit request =>
    articleService.list(page, pageSize, orderBy, filter).map { articles =>
      Ok(views.html.index(articles, filter))
    }
  }

  /**
    * Główna strona aplikacji
    *
    * @param page     aktualna strona
    * @param pageSize rozmiar strony
    * @param orderBy  parametr sortowania
    */
  def search(page: Int, pageSize: Int, orderBy: Int) = Action.async { implicit request =>

    Form("search" -> Forms.text).bindFromRequest().fold(
      formWithErrors => Future.successful(Home),

      filter =>
        articleService.list(page, pageSize, orderBy, filter).map { articles =>
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
    articleService.retrieve(url).map {

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
