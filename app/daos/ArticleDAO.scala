package daos

import java.util.UUID

import models.{Article, Page, User}
import utils.Language

import scala.concurrent.Future

/**
  * Dostęp do obiektów artykułów
  */
trait ArticleDAO {

  /**
    * Wyszukuje artykułu po identyfikatorze
    *
    * @param id identyfikator do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  def find(id: UUID): Future[Option[Article]]

  /**
    * Wyszukuje artykułu po adresie
    *
    * @param url adres do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  def find(url: String): Future[Option[Article]]

  /**
    * Zapisuje artykuł w bazie
    *
    * @param article artykuł do zapisania
    */
  def save(article: Article): Future[Unit]

  /**
    * Aktualizuje artykuł
    *
    * @param article artykuł do aktualizacji
    */
  def update(article: Article): Future[Unit]

  /**
    * Aktualizuje ilość wyświetleń
    *
    * @param id identyfikator do aktualizacji wyświetleń
    */
  def bumpViews(id: UUID): Future[Unit]

  /**
    * Zlicza ilość artykułów w bazie
    *
    * @return liczba zapisanych artykułów
    */
  def count: Future[Long]

  /**
    * Zwraca ostatnie artykuły potrzebne do newslwttera
    *
    * @return lista artykułów
    */
  def newsletterList: Future[List[Article]]

  /**
    * Zwraca listę artykułów przefiltrowaną, posortowaną z podziałem na strony
    *
    * @param page       aktualna strona
    * @param pageSize   ilość elementów na stronie
    * @param orderBy    sortowanie
    * @param filter     filtrowanie
    * @param onlyActive jeżeli true to wyszukuje tylko aktywnych artykułów
    * @param owner      twórca artykułu
    * @param lang       język artykułu
    * @return lista artykułów
    */
  def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%", onlyActive: Boolean = false, owner: Option[User] = None, lang: Option[Language.Language] = None): Future[Page[Article]]
}
