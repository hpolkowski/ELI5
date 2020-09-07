package services

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import models.{Page, User}
import play.api.i18n.Messages
import utils.RoleType.RoleType

import scala.concurrent.Future

/**
  * Obsługuje wszystkie zapytania użytkowników
  */
trait UserService extends IdentityService[User] {

  /**
    * Retrieves a user that matches the specified ID.
    *
    * @param id The ID to retrieve a user.
    * @return The retrieved user or None if no user could be retrieved for the given ID.
    */
  def retrieve(id: UUID): Future[Option[User]]

  /**
    * Retrieves a user that matches the specified email.
    *
    * @param email The email to retrieve a user.
    * @return The retrieved user or None if no user could be retrieved for the given email.
    */
  def retrieve(email: String): Future[Option[User]]

  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user.
    */
  def save(user: User): Future[Option[User]]

  /**
    * Updates a user.
    *
    * @param user The user to update.
    * @return The updated user.
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
    * Aktualizuje hasło użytkownika
    *
    * @param userId   identyfikator użytkownika
    * @param token    token autoryzacji zmiany hasła
    * @param password nowe hasło
    * @return użytkownik jeżeli odnaleziono
    */
  def updatePassword(userId: UUID, token: String, password: String)(implicit authInfoRepository: AuthInfoRepository, passwordHasherRegistry: PasswordHasherRegistry): Future[Option[User]]

  /**
    * Counts amount of registered users
    *
    * @return number of registered users
    */
  def count: Future[Long]

  /**
    * Zwraca listę użytkowników
    *
    * @param page     aktualna strona
    * @param pageSize ilość elementów na stronie
    * @param orderBy  sortowanie
    * @param filter   filtrowanie
    * @return lista użytkowników podzielona na strony
    */
  def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Future[Page[User]]

  /**
    * Generuje token zmiany hasła użytkownika
    *
    * @param id identyfikator użytkownika
    * @return obiekt użytkownika jeżeli został odnaleziony
    */
  def generateResetPasswordToken(id: UUID): Future[Option[User]]

  /**
    * Generuje token zmiany hasła użytkownika
    *
    * @param email adres email
    * @return obiekt użytkownika jeżeli został odnaleziony
    */
  def generateResetPasswordToken(email: String): Future[Option[User]]

  /**
    * Wyszukuje użytkowników względem roli w systemie
    *
    * @param role rola w systemie
    * @return lista użytkowników
    */
  def listUsersByRole(role: RoleType): Future[List[User]]
}
