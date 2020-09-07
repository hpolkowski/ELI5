package models

import java.util.UUID

import io.getquill.Embedded
import utils.{Language, RoleType}
import utils.auth.IdentitySilhouette

/**
  * Uzytkownik systemu
  *
  * @param id                 unikalny identyfikator
  * @param role               rola w systemie
  * @param providerId         identyfikator dostawcy autoryzacji
  * @param providerKey        klucz dostawcy autoryzacji
  * @param password           hash hasła
  * @param fullName           imię i nazwisko
  * @param lang               preferowany język
  * @param resetPasswordToken token zmiany hasła
  */
case class User(
  id: UUID,
  role: RoleType.RoleType,
  providerId: String,
  providerKey: String,
  password: String,
  fullName: String,
  lang: Language.Language,
  resetPasswordToken: Option[String] = None
) extends IdentitySilhouette with Embedded {

  /**
    * Adres email użytkownika
    */
  val email: String = providerKey

  /**
    * True, jeżeli jest administratorem
    */
  def isAdmin = role == RoleType.ADMIN

  /**
    * True, jeżeli jest moderatorem
    */
  def isModerator = role == RoleType.MODERATOR

  /**
    * True, jeżeli jest twórcą treści
    */
  def isCreator = role == RoleType.CREATOR
}
