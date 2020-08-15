package forms

import java.time.LocalDateTime
import java.util.UUID

import models.{Article, User}
import play.api.data.Form
import play.api.data.Forms._
import utils.ArticleState

/**
  * Formularz danych tworzenia artykułu
  *
  * @param title      tytuł artykułu
  * @param content    zawartość artykułu
  */
case class CreateArticleForm (
  title: String,
  content: String
) {

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param id identyfikator artykułu jeżeli istnieje
    * @return obiekt artykułu
    */
  def toArticle(id: UUID )(implicit loggedIn: User): Article =
    Article(id, loggedIn.id, "", title, content, ArticleState.TO_REVIEW, LocalDateTime.now, LocalDateTime.now)

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @return obiekt artykułu
    */
  def toArticle(implicit loggedIn: User): Article = toArticle(UUID.randomUUID())
}

/**
  * Obiekt towarzyszący klasy CreateArticleForm
  */
object CreateArticleForm {
  val form = Form(
    mapping(
      "title" -> text,
      "content" -> text
    )(CreateArticleForm.apply)(CreateArticleForm.unapply)
  )
}
