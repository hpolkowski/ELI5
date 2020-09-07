package utils

import io.getquill.MappedEncoding
import play.api.i18n.Messages

/**
  * Role użytkownika
  */
object RoleType extends Enumeration {
  type RoleType = Value

  val ADMIN = Value("ADMIN")                        // Administrator systemu
  val MODERATOR = Value("MODERATOR")                // Moderator
  val CREATOR = Value("CREATOR")                    // Twórca treści

  implicit val encode = MappedEncoding[RoleType, String](_.toString)
  implicit val decode = MappedEncoding[String, RoleType](withName)
}

/**
  * Statusy artykułu
  */
object ArticleState extends Enumeration {
  type ArticleState = Value

  val TO_REVIEW = Value("TO_REVIEW")                // Do sprawdzenia
  val ACTIVE = Value("ACTIVE")                      // Zatwierdzony
  val REJECTED = Value("REJECTED")                  // Odrzucony

  implicit val encode = MappedEncoding[ArticleState, String](_.toString)
  implicit val decode = MappedEncoding[String, ArticleState](withName)
}


/**
  * Języki treści
  */
object Language extends Enumeration {
  type Language = Value

  val PL = Value("pl")                              // Polski
  val US = Value("us")                              // Angielski

  implicit val encode = MappedEncoding[Language, String](_.toString)
  implicit val decode = MappedEncoding[String, Language](withName)

  /**
    * Zwraca obiekt języka w zależności od ustawień messages
    */
  def fromMessages(implicit messages: Messages): Language.Language = {
    messages.lang.language match {

      case "pl" => PL

      case _ => US

    }
  }
}
