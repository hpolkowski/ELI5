package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import play.api.i18n.{Messages, MessagesProvider}

object FormHelper {

  /**
    * Zwraca wszystkie możliwe rozmiary tabel
    *
    * @return lista opcji (id,nazwa)
    */
  val allPageSizes: Map[Int, String] = Map(10 -> "10", 25 -> "25", 50 -> "50", 75 -> "75", 100 -> "100")

  /**
    * Zwraca wszystkie opcje roli użytkowników
    *
    * @return lista opcji (id,nazwa)
    */
  def allRoleTypeOptions(implicit provider: MessagesProvider) = RoleType.values.map { enum => enum.toString -> Messages(enum.toString) }.toList

  /**
    * Zwraca wszystkie opcje statusów artykułów
    *
    * @return lista opcji (id,nazwa)
    */
  def allArticleStates(implicit provider: MessagesProvider) = ArticleState.values.map { enum => enum.toString -> Messages(enum.toString) }.toList

}
