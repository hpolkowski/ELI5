package models

import java.util.UUID

import io.getquill.Embedded
import utils.RoleType
import utils.auth.IdentitySilhouette

case class User(
  id: UUID,
  role: RoleType.RoleType,
  providerId: String,
  providerKey: String,
  password: String,
  fullName: String,
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
