package forms

import java.time.LocalDateTime
import java.util.UUID

import models.{Article, User}
import play.api.data.Form
import play.api.data.Forms._
import utils.Language.Language
import utils.{ArticleState, Language}
import utils.FormFieldImplicits.enum

/**
  * Formularz danych tworzenia artykułu
  *
  * @param title      tytuł artykułu
  * @param recap      podsumowanie
  * @param lang       język artykułu
  * @param content    zawartość artykułu
  */
case class CreateArticleForm (
  title: String,
  recap: String,
  lang: Language.Language,
  content: String
) {

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param  filepath ścieżka do zdjęcia głównego artykułu
    * @return obiekt artykułu
    */
  def toArticle(filepath: String)(implicit loggedIn: User): Article = Article(UUID.randomUUID(), loggedIn.id, "",
    filepath, title, content, ArticleState.TO_REVIEW, LocalDateTime.now, LocalDateTime.now, recap, "", 0, lang)
}

/**
  * Obiekt towarzyszący klasy CreateArticleForm
  */
object CreateArticleForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "recap" -> nonEmptyText(maxLength = 35),
      "lang" -> enum[Language](Language),
      "content" -> nonEmptyText
    )(CreateArticleForm.apply)(CreateArticleForm.unapply)
  )
}
