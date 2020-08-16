package services

import daos.NewsletterDAO
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

/**
  * Serwis obsługujący zadania głównej strony aplikacji
  */
class HomeServiceImpl @Inject()(
  newsletterDAO: NewsletterDAO
)(
  implicit
  context: ExecutionContext
) extends HomeService {

  /**
    * Zapisuje adres email do listy mailingowej
    *
    * @param email adres email
    */
  override def signUpForNewsletter(email: String): Future[Unit] = {
    newsletterDAO.retrieve(email).map {

      case None =>
        newsletterDAO.save(email)

      case _ =>
    }
  }

}
