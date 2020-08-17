package services

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import models.{Article, Page, User}
import utils.RoleType.RoleType

import scala.concurrent.Future

/**
  * Zarządzanie artykułami
  */
trait ArticleService {
  
  /**
    * Wyszukuje artykułu po identyfikatorze
    *
    * @param id identyfikator do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  def retrieve(id: UUID): Future[Option[Article]]

  /**
    * Wyszukuje artykułu po adresie
    *
    * @param url adres do wyszukania
    * @return artykuł jeśli znaleziono, w przeciwnym wypadku None
    */
  def retrieve(url: String): Future[Option[Article]]

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
    * Zlicza ilość artykułów w bazie
    *
    * @return liczba zapisanych artykułów
    */
  def count: Future[Long]

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
  def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%", owner: Option[User] = None): Future[Page[Article]]
}
