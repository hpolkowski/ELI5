package daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.{Page, User}
import play.api.i18n.Messages
import utils.RoleType.RoleType

import scala.concurrent.Future

/**
 * Dostęp do obiektów użytkowników
 */
trait UserDAO {

  /**
    * Wyszukuje użytkownika po danych logowania
    *
    * @param loginInfo dane logowania
    * @return użytkownik jeżeli odnaleziono w przeciwnym wypadku None
    */
  def find(loginInfo: LoginInfo): Future[Option[User]]

  /**
    * Wyszukuje użytkownika po adresie email
    *
    * @param email adres do odnalezienia
    * @return użytkownik jeżeli odnaleziono w przeciwnym wypadku None
    */
  def find(email: String): Future[Option[User]]

  /**
    * Wyszukuje użytkownika po identyfikatorze
    *
    * @param userID identyfikator
    * @return użytkownik jeżeli odnaleziono w przeciwnym wypadku None
    */
  def find(userID: UUID): Future[Option[User]]

  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user or None if user already exists.
    */
  def save(user: User): Future[Option[User]]

  /**
    * Updates User data
    *
    * @param user The user with new data to update.
    * @return The user with new data.
    */
  def update(user: User): Future[User]

  /**
    * Aktualizuje preferowany język użytkownika
    *
    * @param user użytkownik do aktualizacji
    */
  def updateLang(user: User)(implicit messages: Messages): Future[Unit]

  /**
    * Updates user password
    *
    * @param user     The user to update password.
    * @param password New password hash.
    * @return The saved user.
    */
  def updatePassword(user: User, password: String): Future[User]

  /**
    * Counts amount of registered users
    *
    * @return number of registered users
    */
  def count: Future[Long]

  /**
    * Lista użytkowników systemu
    *
    * @param page     aktualna strona
    * @param pageSize ilość elementów na stronie
    * @param orderBy  sortowanie
    * @param filter   filtrowanie
    * @return lista użytkowników podzielona na strony
    */
  def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[User]]

  /**
    * Generuje token zmiany hasła i zapisuje go w obiekcie użytkowinka
    *
    * @param  id identyfikator użytkownika
    * @return token zmiany hasła
    */
  def generateResetPasswordToken(id: UUID): Future[Option[User]]

  /**
    * Usuwa token zmiany hasła
    *
    * @param user użytkownik
    */
  def clearResetPasswordToken(user: User): Future[User]

  /**
    * Wyszukuje użytkowników po roli w systemie
    *
    * @param role rola w systemie
    * @return lista uzytkowników
    */
  def findByRole(role: RoleType): Future[List[User]]
}
