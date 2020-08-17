package services
import java.time.LocalDateTime
import java.util.UUID

import daos.ArticleDAO
import javax.inject.Inject
import models.{Article, Page, User}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Zarządzanie artykułami
  */
class ArticleServiceImpl @Inject()(articleDAO: ArticleDAO)(implicit context: ExecutionContext) extends ArticleService {

  /**
    * Wyszukuje artykułu po identyfikatorze
    *
    * @param id identyfikator do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  override def retrieve(id: UUID): Future[Option[Article]] = articleDAO.find(id)

  /**
    * Wyszukuje artykułu po adresie
    *
    * @param url adres do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  override def retrieve(url: String): Future[Option[Article]] = articleDAO.find(url)

  /**
    * Zapisuje artykuł w bazie
    *
    * @param article artykuł do zapisania
    */
  override def save(article: Article): Future[Unit] = articleDAO.save(article)

  /**
    * Aktualizuje artykuł
    *
    * @param article artykuł do aktualizacji
    */
  override def update(article: Article): Future[Unit] = articleDAO.update(article.copy(editDate = LocalDateTime.now))

  /**
    * Zlicza ilość artykułów w bazie
    *
    * @return liczba zapisanych artykułów
    */
  override def count: Future[Long] = articleDAO.count

  /**
    * Zwraca listę artykułów przefiltrowaną, posortowaną z podziałem na strony
    *
    * @param page     aktualna strona
    * @param pageSize ilość elementów na stronie
    * @param orderBy  sortowanie
    * @param filter   filtrowanie
    * @param owner    twórca artykułu
    * @return lista artykułów
    */
  override def list(page: Int, pageSize: Int, orderBy: Int, filter: String, owner: Option[User] = None): Future[Page[Article]] =
    articleDAO.list(page, pageSize, orderBy, filter, owner = owner)
}
