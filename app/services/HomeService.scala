package services

import scala.concurrent.Future

/**
  * Serwis obsługujący zadania głównej strony aplikacji
  */
trait HomeService {

  /**
    * Zapisuje adres email do listy mailingowej
    *
    * @param email adres email
    */
  def signUpForNewsletter(email: String): Future[Unit]

}
