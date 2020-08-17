package controllers

import java.util.UUID

import bootstrap.AppConfig
import com.mohiva.play.silhouette.api.Silhouette
import forms.CreateArticleForm
import javax.inject._
import models.User
import play.api.data.Form
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import services.{ArticleService, FileService}
import utils.RoleType
import utils.auth.{CookieEnvironment, WithRoles}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Kontroler obsługi zapytań dotyczących artykułów
  */
@Singleton
class ArticleController @Inject()(
  components: ControllerComponents,
  silhouette: Silhouette[CookieEnvironment],
  articleService: ArticleService,
  fileService: FileService
)(
  implicit
  appConfig: AppConfig,
  assets: AssetsFinder,
  context: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  /**
    * Przekeirowanie do listy artykułów
    */
  val Home: Result = Redirect(routes.ArticleController.list())

  /**
    * Lista wszystkich artykułów
    *
    * @param page     aktualna strona
    * @param pageSize rozmiar strony
    * @param orderBy  parametr sortowania
    * @param filter   filtry
    */
  def list(page: Int, pageSize: Int, orderBy: Int, filter: String) = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))).async { implicit request =>
    implicit val loggedIn: User = request.identity

    articleService.list(page, pageSize, orderBy, filter, owner = if(loggedIn.isCreator) Some(loggedIn) else None).map { articleList =>
      Ok(views.html.admin.article.list(articleList, orderBy, filter))
    }
  }

  /**
    * Tworzenie nowego artykułu
    */
  def create = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))) { implicit request =>
    implicit val loggedIn: User = request.identity

    Ok(views.html.admin.article.create(CreateArticleForm.form))
  }

  /**
    * Zapisywanie nowego artykułu
    */
  def save = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))).async { implicit request =>
    implicit val loggedIn: User = request.identity

    def badRequest(formWithErrors: Form[CreateArticleForm]) = Future.successful(BadRequest(views.html.admin.article.create(formWithErrors)))
    
    CreateArticleForm.form.bindFromRequest.fold(
      formWithErrors => badRequest(formWithErrors),

      data => request.body.asMultipartFormData.flatMap(_.file("leadPhoto")).map(fileService.save).map {

        case Left(error) =>
          badRequest(CreateArticleForm.form.fill(data).withError("leadPhoto", error))

        case Right(filepath) =>
          articleService.save(data.toArticle(filepath)).map { _ =>
            Home.flashing("success" -> "article.save.success")
          }

      }.getOrElse {
        badRequest(CreateArticleForm.form.fill(data).withError("leadPhoto", "error.file.filename.empty"))
      }
    )
  }

  /**
    * Edycja danych artykułu
    */
  def edit(id: UUID) = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))).async { implicit request =>
    implicit val loggedIn: User = request.identity
    
    articleService.retrieve(id).map {

      case Some(article) =>
        val readonly = loggedIn.isCreator && !article.isToReview

        Ok(views.html.admin.article.edit(article.id, article.toCreateArticleForm, readonly))

      case None =>
        Home.flashing("failure" -> "article.edit.notFound")

    }
  }

  /**
    * Zapisywnie edytowanych danych artykułu
    */
  def update(id: UUID) = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))).async { implicit request =>
    implicit val loggedIn: User = request.identity

    def badRequest(formWithErrors: Form[CreateArticleForm]) = Future.successful(BadRequest(views.html.admin.article.edit(id, formWithErrors)))

    CreateArticleForm.form.bindFromRequest.fold(

      formWithErrors => badRequest(formWithErrors),

      data => {
        val filepath = request.body.asMultipartFormData.flatMap(_.file("leadPhoto")).map(fileService.save).flatMap {

          case Left(_) =>
            None

          case Right(filepath) =>
            Some(filepath)

        }.getOrElse("")

        articleService.update(data.toArticle(id, filepath)).map { _ =>
          Home.flashing("success" -> "article.update.success")
        }
      }
    )
  }
}
