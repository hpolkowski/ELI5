package controllers

import java.util.UUID

import bootstrap.AppConfig
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.{CreateUserForm, ResetPasswordForm}
import javax.inject._
import models.User
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import services.{MailerService, _}
import utils.RoleType
import utils.auth.{CookieEnvironment, WithRole}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Kontroler obsługi zapytań dotyczących użytkowników
  */
@Singleton
class UserController @Inject()(
  components: ControllerComponents,
  silhouette: Silhouette[CookieEnvironment],
  userService: UserService,
  mailerService: MailerService
)(
  implicit
  appConfig: AppConfig,
  assets: AssetsFinder,
  context: ExecutionContext,
  authInfoRepository: AuthInfoRepository,
  passwordHasherRegistry: PasswordHasherRegistry
) extends AbstractController(components) with I18nSupport {

  /**
    * Przekeirowanie do listy użytkowników
    */
  val Home: Result = Redirect(routes.UserController.list())

  /**
    * Przekeirowanie do strony logowania
    */
  val Login: Result = Redirect(routes.AuthController.login())

  /**
    * Lista wszystkich użytkowników
    *
    * @param page     aktualna strona
    * @param pageSize rozmiar strony
    * @param orderBy  parametr sortowania
    * @param filter   filtry
    */
  def list(page: Int, pageSize: Int, orderBy: Int, filter: String) = silhouette.SecuredAction(WithRole(RoleType.ADMIN)).async { implicit request =>
    implicit val loggedIn: User = request.identity

    userService.list(page, pageSize, orderBy, filter).map { userList =>
      Ok(views.html.admin.user.list(userList, orderBy, filter))
    }
  }

  /**
    * Tworzenie nowego użytkownika
    */
  def create = silhouette.SecuredAction(WithRole(RoleType.ADMIN)).async { implicit request =>
    implicit val loggedIn: User = request.identity

    Future.successful(Ok(views.html.admin.user.create(CreateUserForm.form)))
  }

  /**
    * Zapisywanie nowego użytkownika
    */
  def save = silhouette.SecuredAction(WithRole(RoleType.ADMIN)).async { implicit request =>
    implicit val loggedIn: User = request.identity

    CreateUserForm.form.bindFromRequest.fold(

      formWithErrors => Future.successful(BadRequest(views.html.admin.user.create(formWithErrors))),

      data => {
        val userData = User(UUID.randomUUID(), data.role, CredentialsProvider.ID, data.email, "", data.fullName)

        for {
          user <- userService.save(userData)
          _ <- authInfoRepository.add(userData.loginInfo, passwordHasherRegistry.current.hash(userData.password))
        } yield user.map { user =>
          userService.generateResetPasswordToken(user.id).foreach(_.foreach(mailerService.sendPasswordResetTokenAfterAccountCreation))
          Home.flashing("success" -> Messages("user.create.success", user.email))
        }.getOrElse {
          Home.flashing("failure" -> Messages("users.create.exists", userData.email))
        }
      }
    )
  }

  /**
    * Edycja danych użytkownika
    */
  def edit(id: UUID) = silhouette.SecuredAction(WithRole(RoleType.ADMIN)).async { implicit request =>
    implicit val loggedIn: User = request.identity

    userService.retrieve(id).map {

      case Some(user) =>
        val form = CreateUserForm.form.fill(CreateUserForm(user.email, user.role, user.fullName))

        Ok(views.html.admin.user.edit(id, form))

      case None =>
        NotFound

    }
  }

  /**
    * Zapisywnie edytowanych danych użytkownika
    */
  def update(id: UUID) = silhouette.SecuredAction(WithRole(RoleType.ADMIN)).async { implicit request =>
    implicit val loggedIn: User = request.identity

    CreateUserForm.form.bindFromRequest.fold(

      formWithErrors => Future.successful(BadRequest(views.html.admin.user.edit(id, formWithErrors))),

      data => {
        val userData = User(id, data.role, CredentialsProvider.ID, data.email, "", data.fullName)

        userService.update(userData).map { user =>
          Home.flashing("success" -> Messages("user.edit.success", user.email))
        }
      }
    )
  }

  /**
    * Generowanie linku zmiany hasła użytkownika
    */
  def sendResetPassword(id: UUID) = silhouette.UserAwareAction.async { implicit request =>
    userService.generateResetPasswordToken(id).map {

      case Some(user) =>
        mailerService.sendPasswordResetToken(user)
        Home.flashing("success" -> Messages("user.resetPassword.send.success", user.email))

      case None =>
        NotFound

    }
  }

  /**
    * Formularz zmiany hasła użytkownika
    */
  def resetPassword(id: UUID, token: String) = silhouette.UserAwareAction.async { implicit request =>
    implicit val loggedIn: Option[User] = request.identity

    Future.successful(Ok(views.html.admin.user.resetPassword(id, token, ResetPasswordForm.form)))
  }

  /**
    * Zmiana hasła użytkownika
    */
  def savePassword(id: UUID, token: String) = silhouette.UserAwareAction.async { implicit request =>
    implicit val loggedIn: Option[User] = request.identity

    ResetPasswordForm.form.bindFromRequest.fold(

      formWithErrors => Future.successful(BadRequest(views.html.admin.user.resetPassword(id, token, formWithErrors))),

      data => {
        userService.updatePassword(id, token, data.password).map {

          case Some(user) =>
            Login.flashing("success" -> Messages("user.resetPassword.success"))

          case None =>
            NotFound

        }
      }
    )
  }
}
