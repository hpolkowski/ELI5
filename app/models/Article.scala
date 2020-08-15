package models

import java.time.LocalDateTime
import java.util.UUID

import forms.CreateArticleForm
import play.api.data.Form
import utils.ArticleState

/**
  * Artykuł
  *
  * @param id         unikalny identyfikator artykułu
  * @param creatorId  identyfikator użytkownika który dodał artykuł
  * @param url        adres url artykułu
  * @param title      tytuł artykułu
  * @param content    zawartość artykułu
  * @param state      status artykułu
  * @param createDate data dodania
  * @param editDate   data ostatniej edycji
  */
case class Article(
  id: UUID,
  creatorId: UUID,
  url: String,
  title: String,
  content: String,
  state: ArticleState.ArticleState,
  createDate: LocalDateTime,
  editDate: LocalDateTime
) {

  /**
    * Uzupełnia danymi formularz tworzenia artykułu
    *
    * @return formularz tworzenia artykułu
    */
  def toCreateArticleForm: Form[CreateArticleForm] = CreateArticleForm.form.fill(CreateArticleForm(title, content))

}
