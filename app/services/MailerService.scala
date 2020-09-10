package services

import bootstrap.AppConfig
import controllers.routes
import javax.inject.Inject
import models.{Article, User}
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
  def sendPasswordResetTokenAfterAccountCreation(recipient: User): Unit

  /**
    * Wysyła wiadomość z linkiem do zmiany hasła
    *
    * @param recipient adresat wiadomości
    */
  def sendPasswordResetToken(recipient: User): Unit

  /**
    * Wysyła wiadomość newslettera w języku Polskim
    *
    * @param addresses lista adreswo na jaki ma być dostarczona wiadomość
    * @param articles lista artykułów do opisania w wiadomości
    */
  def sendNewsletterPL(addresses: List[String], articles: List[Article])

  /**
    * Wysyła wiadomość newslettera w języku Angielskim
    *
    * @param addresses lista adreswo na jaki ma być dostarczona wiadomość
    * @param articles lista artykułów do opisania w wiadomości
    */
  def sendNewsletterUS(addresses: List[String], articles: List[Article])
}
