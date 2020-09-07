package models

import utils.Language

/**
  * Obiekt osoby zapisanej do newslettera
  *
  * @param email adres email
  * @param lang  preferowany język wiadomości
  */
case class Newsletter(
  email: String,
  lang: Language.Language
)
