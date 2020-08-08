package utils.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import models.User

/**
  * Środowisko autoryzacji oparte o ciasteczka.
  */
trait CookieEnvironment extends Env {
  type I = User
  type A = CookieAuthenticator
}
