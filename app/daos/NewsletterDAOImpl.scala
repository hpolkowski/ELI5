package daos

import database.{DatabaseConnector, QueryExtension}
import javax.inject.Inject
import models.Newsletter
import play.api.i18n.Messages
import utils.Language

import scala.concurrent.{ExecutionContext, Future}

/**
  * Obsługa zapytań bazodanowych newslettera
  */
class NewsletterDAOImpl @Inject() (val database: DatabaseConnector) (implicit context: ExecutionContext)
  extends NewsletterDAO with QueryExtension {

  import database.ctx._

  private val newsletters = quote(querySchema[Newsletter]("newsletter"))

  /**
    * Zapisuje do newslettera w bazie
    *
    * @param email adres email
    */
  override def save(email: String)(implicit messages: Messages): Future[Unit] = {
    val newsletter = Newsletter(email, Language.fromMessages)

    run(newsletters.insert(lift(newsletter)))

    Future.successful()
  }

  /**
    * Usuwa zapis newslettera z bazy
    *
    * @param email adres email
    */
  override def delete(email: String): Future[Unit] =  {
    run(newsletters.filter(_.email == lift(email)).delete)

    Future.successful()
  }

  /**
    * Zlicza ilość wpisów newslettera w bazie
    *
    * @return ilość wpisów
    */
  override def count: Future[Long] = Future.successful(run(newsletters.size))

  /**
    * Zwraca listę adresów
    *
    * @return lista adresów email
    */
  override def list: Future[List[Newsletter]] = Future.successful(
    run(newsletters)
  )

  /**
    * Wyszukuje zapisu w bazie po adresie email
    *
    * @param email adres email
    */
  override def retrieve(email: String): Future[Option[String]] = Future.successful(
    run(newsletters.filter(_.email == lift(email)).take(1)).headOption.map(_.email)
  )
}
