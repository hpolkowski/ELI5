package utils.auth

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User
import play.api.mvc.Request
import utils.RoleType
import utils.RoleType.RoleType

import scala.concurrent.Future

/**
  * Model do sprawdzania czy dana rola jest uprawniona do zasobu
  *
  * @param role rola użytkownika w systemie
  */
case class WithRole(role: RoleType) extends Authorization[User, CookieAuthenticator] {
  override def isAuthorized[B](user: User, authenticator: CookieAuthenticator)(implicit request: Request[B]): Future[Boolean] = Future.successful
  {
    user.role.equals(role) || user.role.equals(RoleType.ADMIN)
  }
}
