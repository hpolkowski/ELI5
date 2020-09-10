package daos

import models.Newsletter
import play.api.i18n.Messages

import scala.concurrent.Future

/**
  * Obsługa zapytań bazodanowych newslettera
  */
trait NewsletterDAO {

  /**
    * Zapisuje do newslettera w bazie
    *
    * @param email adres email
    */
  def save(email: String)(implicit messages: Messages): Future[Unit]

  /**
    * Wyszukuje zapisu w bazie po adresie email
    *
    * @param email adres email
    */
  def retrieve(email: String): Future[Option[String]]

  /**
    * Usuwa zapis newslettera z bazy
    *
    * @param email adres email
    */
  def delete(email: String): Future[Unit]

  /**
    * Zlicza ilość wpisów newslettera w bazie
    *
    * @return ilość wpisów
    */
  def count: Future[Long]

  /**
    * Zwraca listę adresów
    *
    * @return lista adresów email
    */
  def list: Future[List[Newsletter]]

}
