package services

import bootstrap.AppConfig
import controllers.routes
import javax.inject.Inject
import models.User
import play.api.libs.mailer._

/**
  * Serwis do wysyłania maili
  */
class MailerServiceImpl @Inject()(mailerClient: MailerClient, appConfig: AppConfig) extends MailerService {

  /**
    * Wysyła wiadomość z linkiem do zmiany hasła po utworzeniu nowego konta
    *
    * @param recipient adresat wiadomości
    */
  override def sendPasswordResetTokenAfterAccountCreation(recipient: User): Unit = recipient.resetPasswordToken.foreach { resetPasswordToken =>
    val url = s"http://${appConfig.appDomain}${routes.UserController.resetPassword(recipient.id, resetPasswordToken)}"
    val email = Email(
      subject = "Welcome in ELI5 team",
      from = s"${appConfig.appName} <${appConfig.noReplyEmail}>",
      to = Seq(s"${recipient.fullName} <${recipient.email}>"),
      bodyText = Some(
        s"""
        |Welcome in ELI5 team!
        |You can change your password here: $url
        |
        |With love,
        |Team ELI5
        """.stripMargin
      )
    )
    mailerClient.send(email)
  }

  /**
    * Wysyła wiadomość z linkiem do zmiany hasła
    *
    * @param recipient adresat wiadomości
    */
  override def sendPasswordResetToken(recipient: User): Unit = recipient.resetPasswordToken.foreach { resetPasswordToken =>
    val url = s"http://${appConfig.appDomain}${routes.UserController.resetPassword(recipient.id, resetPasswordToken)}"
    val email = Email(
      subject = "ELI5 change password",
      from = s"${appConfig.appName} <${appConfig.noReplyEmail}>",
      to = Seq(s"${recipient.fullName} <${recipient.email}>"),
      bodyText = Some(
        s"""
        |Someone requested to change password for this email in ELI5.
        |If it was you, you can change your password here: $url
        |Otherwise contact our support ${appConfig.contactEmail}
        |
        |With love,
        |Team ELI5""".stripMargin
      )
    )
    mailerClient.send(email)
  }
}
