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
  * @param recap      podsumowanie
  * @param url        ofijalny adres dostępowy
  * @param state      status artykułu
  * @param content    zawartość artykułu
  * @param tags       tagi artykułu
  */
case class EditArticleForm (
  title: String,
  recap: String,
  url: String,
  state: ArticleState.ArticleState,
  content: String,
  tags: String
) {

  /**
    * Konwertuje dane z formularza na obiekt
    *
    * @param  id identyfikator artykułu jeżeli istnieje
    * @param  filepath ścieżka do zdjęcia głównego artykułu
    * @return obiekt artykułu
    */
  def toArticle(id: UUID, filepath: String)(implicit loggedIn: User): Article =
    Article(id, loggedIn.id, url, filepath, title, content, state, LocalDateTime.now, LocalDateTime.now, recap, tags, 0)
}

/**
  * Obiekt towarzyszący klasy EditArticleForm
  */
object EditArticleForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "recap" -> nonEmptyText(maxLength = 35),
      "url" -> text.verifying("error.illegalCharacters", _.matches("[A-z0-9_-]+")),
      "state" -> enum[ArticleState](ArticleState),
      "content" -> nonEmptyText,
      "tags" -> nonEmptyText
    )(EditArticleForm.apply)(EditArticleForm.unapply)
      .verifying("article.edit.url.empty", data =>
        !(data.state == ArticleState.ACTIVE && data.url.isEmpty)
      )
  )
}
