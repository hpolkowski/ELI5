package utils

import io.getquill.MappedEncoding

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
