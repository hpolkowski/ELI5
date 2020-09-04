package services

import daos.{ArticleDAO, NewsletterDAO}
import javax.inject.Inject
import models.{Article, Page}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Serwis obsługujący zadania głównej strony aplikacji
  */
class HomeServiceImpl @Inject()(
  newsletterDAO: NewsletterDAO,
  articleDAO: ArticleDAO
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

  /**
    * Zwraca listę aktywnych artykułów
    *
    * @param page   numer strony
    * @param orderBy  kolejność sortowania
    * @param filter tekst filtrowania
    */
  override def listArticle(page: Int, orderBy: Int, filter: String): Future[Page[Article]] = {
    articleDAO.list(page, 12, orderBy, filter, onlyActive = true)
  }

  /**
    * Wyszukuje artykułu po url
    *
    * @param url url artykułu
    */
  override def retrieveArticle(url: String): Future[Option[Article]] = {
    articleDAO.find(url).map {

      case Some(article) =>
        articleDAO.bumpViews(article.id)
        Some(article)

      case None =>
        None
    }
  }
}
