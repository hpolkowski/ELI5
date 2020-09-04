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
  * @param recap      podsumowanie
  * @param content    zawartość artykułu
  */
case class CreateArticleForm (
  title: String,
  recap: String,
  content: String
) {

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param  filepath ścieżka do zdjęcia głównego artykułu
    * @return obiekt artykułu
    */
  def toArticle(filepath: String)(implicit loggedIn: User): Article =
    Article(UUID.randomUUID(), loggedIn.id, "", filepath, title, content, ArticleState.TO_REVIEW,
      LocalDateTime.now, LocalDateTime.now, recap, "")
}

/**
  * Obiekt towarzyszący klasy CreateArticleForm
  */
object CreateArticleForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "recap" -> nonEmptyText(maxLength = 35),
      "content" -> nonEmptyText
    )(CreateArticleForm.apply)(CreateArticleForm.unapply)
  )
}
