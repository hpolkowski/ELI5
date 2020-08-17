package forms

import java.time.LocalDateTime
import java.util.UUID

import models.{Article, User}
import play.api.data.Form
import play.api.data.Forms._
import utils.FormFieldImplicits.enum
import utils.ArticleState.ArticleState
import utils.ArticleState

/**
  * Formularz danych tworzenia artykułu
  *
  * @param title      tytuł artykułu
  * @param url        ofijalny adres dostępowy
  * @param state      status artykułu
  * @param content    zawartość artykułu
  */
case class CreateArticleForm (
  title: String,
  url: Option[String],
  state: Option[ArticleState.ArticleState],
  content: String
) {

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param  id identyfikator artykułu jeżeli istnieje
    * @param  filepath ścieżka do zdjęcia głównego artykułu
    * @return obiekt artykułu
    */
  def toArticle(id: UUID, filepath: String)(implicit loggedIn: User): Article =
    Article(id, loggedIn.id, url.getOrElse(""), filepath, title, content, state.getOrElse(ArticleState.TO_REVIEW),
      LocalDateTime.now, LocalDateTime.now)

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param  filepath ścieżka do zdjęcia głównego artykułu
    * @return obiekt artykułu
    */
  def toArticle(filepath: String)(implicit loggedIn: User): Article = toArticle(UUID.randomUUID(), filepath)
}

/**
  * Obiekt towarzyszący klasy CreateArticleForm
  */
object CreateArticleForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "url" -> optional(text.verifying("error.illegalCharacters", _.matches("[A-z0-9_-]+"))),
      "state" -> optional(enum[ArticleState](ArticleState)),
      "content" -> nonEmptyText
    )(CreateArticleForm.apply)(CreateArticleForm.unapply)
      .verifying("article.edit.url.empty", data =>
        !(data.state.contains(ArticleState.ACTIVE) && data.url.isEmpty)
      )
  )
}
