package daos

import database.{DatabaseConnector, QueryExtension}
import javax.inject.Inject

import scala.concurrent.{ExecutionContext, Future}

/**
  * Obsługa zapytań bazodanowych newslettera
  */
class NewsletterDAOImpl @Inject() (val database: DatabaseConnector) (implicit context: ExecutionContext)
  extends NewsletterDAO with QueryExtension {

  import database.ctx._

  private val newsletter = quote(querySchema[String]("newsletter", _ -> "email"))

  /**
    * Zapisuje do newslettera w bazie
    *
    * @param email adres email
    */
  override def save(email: String): Future[Unit] = {
    run(newsletter.insert(lift(email)))

    Future.successful()
  }

  /**
    * Usuwa zapis newslettera z bazy
    *
    * @param email adres email
    */
  override def delete(email: String): Future[Unit] =  {
    run(newsletter.filter(_ == lift(email)).delete)

    Future.successful()
  }

  /**
    * Zlicza ilość wpisów newslettera w bazie
    *
    * @return ilość wpisów
    */
  override def count: Future[Long] = Future.successful(run(newsletter.size))

  /**
    * Zwraca listę adresów
    *
    * @return lista adresów email
    */
  override def list: Future[List[String]] = Future.successful(
    run(newsletter)
  )

  /**
    * Wyszukuje zapisu w bazie po adresie email
    *
    * @param email adres email
    */
  override def retrieve(email: String): Future[Option[String]] = Future.successful(
    run(newsletter.filter(_ == lift(email)).take(1)).headOption
  )
}
