package controllers

import _root_.services.UserService
import bootstrap.AppConfig
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.{RequestResetPasswordForm, SignInForm}
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import utils.auth.CookieEnvironment

import scala.concurrent.{ExecutionContext, Future}

/**
  * Kontroler zapytań obsługi autoryzacji
  */
@Singleton
class AuthController @Inject()(
  components: ControllerComponents,
  silhouette: Silhouette[CookieEnvironment],
  userService: UserService,
  credentialsProvider: CredentialsProvider
)(
  implicit
  appConfig: AppConfig,
  assets: AssetsFinder,
  context: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  /**
    * Przekierowanie do głównej strony
    */
  val Home: Result = Redirect(routes.HomeController.index())

  /**
    * Przekierowanie do strony logowania
    */
  val Login: Result = Redirect(routes.AuthController.login())

  /**
    * Wyświetla stronę logowania
    */
  def login = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.auth.signIn(SignInForm.form))
  }

  /**
    * Loguje do serwisu
    */
  def authenticate = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    SignInForm.form.bindFromRequest.fold(
      formWithErrors =>
        Future.successful(BadRequest(views.html.auth.signIn(formWithErrors))),

      data => {
        val credentials = Credentials(data.email, data.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {

            case Some(user) => for {
              authenticator <- silhouette.env.authenticatorService.create(loginInfo)
              cookie <- silhouette.env.authenticatorService.init(authenticator)
              result <- silhouette.env.authenticatorService.embed(cookie, Home)
            } yield {
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }

            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case _: ProviderException =>
            Redirect(routes.AuthController.login()).flashing("error" -> Messages("auth.credentials.invalid"))
        }
      }
    )
  }

  /**
    * Wylogowuje z serwisu
    */
  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[CookieEnvironment, AnyContent] =>
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, Login)
  }

  /**
    * Wyświetla formularz żądania zmiany hasła
    */
  def requestResetPassword = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.auth.requestResetPassword(RequestResetPasswordForm.form))
  }

  /**
    * Wysyła wiadomość z instrukcjami zmiany hasła
    */
  def sendResetPassword = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    RequestResetPasswordForm.form.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.auth.requestResetPassword(formWithErrors)),

      data => {
        for {
          optionalUser <- userService.retrieve(data.email)
        } yield for {
          user <- optionalUser
        } yield for {
          successUser <- userService.generateResetPasswordToken(user.id)
        } yield successUser

        Login.flashing("success" -> Messages("auth.requestResetPassword.success"))
      }
    )
  }
}
