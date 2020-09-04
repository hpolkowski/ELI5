package services

import models.{Article, Page}

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

  /**
    * Zwraca listę aktywnych artykułów
    *
    * @param page     numer strony
    * @param orderBy  kolejność sortowania
    * @param filter   tekst filtrowania
    */
  def listArticle(page: Int, orderBy: Int, filter: String): Future[Page[Article]]

  /**
    * Wyszukuje artykułu po url
    *
    * @param url url artykułu
    */
  def retrieveArticle(url: String): Future[Option[Article]]

}
