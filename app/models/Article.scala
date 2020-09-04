package models

import java.time.LocalDateTime
import java.util.UUID

import forms.{CreateArticleForm, EditArticleForm}
import play.api.data.Form
import utils.ArticleState

/**
  * Artykuł
  *
  * @param id         unikalny identyfikator artykułu
  * @param creatorId  identyfikator użytkownika który dodał artykuł
  * @param url        adres url artykułu
  * @param leadPhoto  główne zdjęcie
  * @param title      tytuł artykułu
  * @param content    zawartość artykułu
  * @param state      status artykułu
  * @param createDate data dodania
  * @param editDate   data ostatniej edycji
  * @param recap      podsumowanie
  * @param tags       tagi
  */
case class Article(
  id: UUID,
  creatorId: UUID,
  url: String,
  leadPhoto: String,
  title: String,
  content: String,
  state: ArticleState.ArticleState,
  createDate: LocalDateTime,
  editDate: LocalDateTime,
  recap: String,
  tags: String
) {

  /**
    * Uzupełnia danymi formularz tworzenia artykułu
    *
    * @return formularz tworzenia artykułu
    */
  def toCreateArticleForm: Form[CreateArticleForm] = CreateArticleForm.form.fill(CreateArticleForm(title, recap, content))

  /**
    * Uzupełnia danymi formularz tworzenia artykułu
    *
    * @return formularz tworzenia artykułu
    */
  def toEditArticleForm: Form[EditArticleForm] = EditArticleForm.form.fill(EditArticleForm(title, recap, url, state, content, tags))

  /**
    * Zwraca true jeżeli artykuł jest do sprawdzenia
    *
    * @return true jeżeli status jest TO_REVIEW
    */
  def isToReview: Boolean = state == ArticleState.TO_REVIEW
}
