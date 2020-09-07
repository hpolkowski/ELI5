package controllers

import com.mohiva.play.silhouette.api.Silhouette
import javax.inject._
import models.User
import play.api.i18n.{I18nSupport, Lang}
import play.api.mvc._
import utils.RoleType
import utils.auth.{CookieEnvironment, WithRole, WithRoles}

/**
 * Kontrolej części administracjnej
 */
@Singleton
class AdminController @Inject()(
  silhouette: Silhouette[CookieEnvironment],
  components: ControllerComponents
)(
  implicit
  assets: AssetsFinder
) extends AbstractController(components) with I18nSupport {

  /**
   * Główna strona administracyjna
   */
  def index = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))) { implicit request =>
    implicit val loggedIn: User = request.identity

    Ok(views.html.admin.index())
  }

  /**
    * Zmiana języka
    *
    * @param lang język docelowy
    */
  def changeLanguage(lang: String) = silhouette.SecuredAction(WithRoles(Seq(RoleType.ADMIN, RoleType.MODERATOR, RoleType.CREATOR))) { implicit request =>
    val url = request.headers.toMap("referer").headOption.getOrElse(routes.AdminController.index.absoluteURL)

    Redirect(url).withLang(Lang(lang))
  }

}
