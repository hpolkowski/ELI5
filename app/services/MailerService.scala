package services

import bootstrap.AppConfig
import controllers.routes
import javax.inject.Inject
import models.User
import play.api.libs.mailer._

/**
  * Serwis do wysyłania maili
  */
trait MailerService {

  /**
    * Wysyła wiadomość z linkiem do zmiany hasła po utworzeniu nowego konta
    *
    * @param recipient adresat wiadomości
    */
  def sendPasswordResetTokenAfterAccountCreation(recipient: User, resetPasswordToken: String): Unit

  /**
    * Wysyła wiadomość z linkiem do zmiany hasła
    *
    * @param recipient adresat wiadomości
    */
  def sendPasswordResetToken(recipient: User): Unit
}
