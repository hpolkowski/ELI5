package daos
import java.util.UUID

import database.{DatabaseConnector, QueryExtension}
import javax.inject.Inject
import models.{Article, Page, User}
import utils.ArticleState

import scala.concurrent.{ExecutionContext, Future}

/**
  * Dostęp do obiektów artykułów
  */
class ArticleDAOImpl @Inject() (val database: DatabaseConnector) (implicit context: ExecutionContext)
  extends ArticleDAO with QueryExtension {

  import database.ctx._

  private val articles = quote(querySchema[Article]("articles"))

  /**
    * Wyszukuje artykułu po identyfikatorze
    *
    * @param id identyfikator do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  override def find(id: UUID): Future[Option[Article]] =
    Future.successful(run(articles.filter(_.id == lift(id))).headOption)

  /**
    * Wyszukuje artykułu po adresie
    *
    * @param url adres do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  override def find(url: String): Future[Option[Article]] =
    Future.successful(run(articles.filter(_.url == lift(url))).headOption)

  /**
    * Zapisuje artykuł w bazie
    *
    * @param data artykuł do zapisania
    */
  override def save(data: Article): Future[Unit] =
    Future.successful(run(articles.insert(lift(data))))

  /**
    * Aktualizuje artykuł
    *
    * @param data artykuł do aktualizacji
    */
  override def update(data: Article): Future[Unit] =
    Future.successful(run(articles.filter(_.id == lift(data.id)).update(
      _.title -> lift(data.title),
      _.url -> lift(data.url),
      _.state -> lift(data.state),
      _.content -> lift(data.content),
      _.editDate -> lift(data.editDate)
    )))

  /**
    * Zlicza ilość artykułów w bazie
    *
    * @return liczba zapisanych artykułów
    */
  override def count: Future[Long] = Future.successful(run(articles.size))

  /**
    * Zwraca listę artykułów przefiltrowaną, posortowaną z podziałem na strony
    *
    * @param page       aktualna strona
    * @param pageSize   ilość elementów na stronie
    * @param orderBy    sortowanie
    * @param filter     filtrowanie
    * @param onlyActive jeżeli true to wyszukuje tylko aktywnych artykułów
    * @param owner    twórca artykułu
    * @return lista artykułów
    */
  override def list(page: Int, pageSize: Int, orderBy: Int, filter: String, onlyActive: Boolean, owner: Option[User]): Future[Page[Article]] = {
    val query: Quoted[Query[Article]] = articles

    val onlyActiveQuery = if(onlyActive)
      quote(query.filter(_.state == lift(ArticleState.ACTIVE)))
    else query

    val ownerQuery = owner.map { user =>
      quote(onlyActiveQuery.filter(_.creatorId == lift(user.id)))
    }.getOrElse {
      onlyActiveQuery
    }

    val filteredQuery = if (filter.nonEmpty)
      quote(ownerQuery.filter { data => data.title likeLowerCase lift(s"%$filter%") })
    else ownerQuery

    val sortedQuery: Quoted[Query[Article]] = orderBy match {
      case 1 => filteredQuery.sortBy(_.title)
      case 2 => filteredQuery.sortBy(_.state)
      case 3 => filteredQuery.sortBy(_.createDate)
      case 4 => filteredQuery.sortBy(_.editDate)

      case -1 => filteredQuery.sortBy(_.title)(Ord.desc)
      case -2 => filteredQuery.sortBy(_.state)(Ord.desc)
      case -3 => filteredQuery.sortBy(_.createDate)(Ord.desc)
      case -4 => filteredQuery.sortBy(_.editDate)(Ord.desc)

      case 0 => filteredQuery.sortBy(_.editDate)(Ord.desc)

      case _ => filteredQuery.sortBy(_.id)(Ord.desc)
    }

    val offset = pageSize * (page - 1)
    val result = run(sortedQuery.drop(lift(offset)).take(lift(pageSize)))

    Future.successful(Page(result, page, offset, run(filteredQuery.size), pageSize))
  }
}
