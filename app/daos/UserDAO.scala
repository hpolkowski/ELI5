package daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.{Page, User}
import utils.RoleType.RoleType

import scala.concurrent.Future

/**
 * Dostęp do obiektów użytkowników
 */
trait UserDAO {

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def find(loginInfo: LoginInfo): Future[Option[User]]

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
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
