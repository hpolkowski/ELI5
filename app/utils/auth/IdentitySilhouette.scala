package utils.auth

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import com.mohiva.play.silhouette.password.BCryptSha256PasswordHasher

trait IdentitySilhouette extends Identity {
  def providerId: String
  def providerKey: String
  def password: String

  def passwordInfo: PasswordInfo = PasswordInfo(BCryptSha256PasswordHasher.ID, password)
  def loginInfo: LoginInfo = LoginInfo(providerId, providerKey)
}