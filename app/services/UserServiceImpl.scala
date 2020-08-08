package services

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.UserDAO
import javax.inject.Inject
import models.User
import utils.RoleType.RoleType

import scala.concurrent.{ExecutionContext, Future}

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 * @param context The execution context.
 */
class UserServiceImpl @Inject()(userDAO: UserDAO)(implicit context: ExecutionContext) extends UserService {

  /**
    * Retrieves a user that matches the specified ID.
    *
    * @param id The ID to retrieve a user.
    * @return The retrieved user or None if no user could be retrieved for the given ID.
    */
  def retrieve(id: UUID) = userDAO.find(id)

  /**
    * Retrieves a user that matches the specified email.
    *
    * @param email The email to retrieve a user.
    * @return The retrieved user or None if no user could be retrieved for the given email.
    */
  def retrieve(email: String) = retrieve(LoginInfo(CredentialsProvider.ID, email))

  /**
    * Retrieves a user that matches the specified login info.
    *
    * @param loginInfo The login info to retrieve a user.
    * @return The retrieved user or None if no user could be retrieved for the given login info.
    */
  def retrieve(loginInfo: LoginInfo) = userDAO.find(loginInfo)

  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user.
    */
  def save(user: User) = {
    userDAO.save(user)
  }

  /**
    * Aktualizuje dane użytkownika
    *
    * @param user użytkownik do aktualizacji
    * @return użytkownik zaktualizowany
    */
  override def update(user: User) = userDAO.update(user)

  /**
    * Aktualizuje hasło
    *
    * @param user     użytkownik któremu należy zaktualizować hasło
    * @param password hash hasła
    * @return zaktualizowany użytkownik
    */
  override def updatePassword(user: User, password: String) = userDAO.updatePassword(user, password)

  /**
    * Aktualizuje hasło użytkownika
    *
    * @param userId   identyfikator użytkownika
    * @param token    token autoryzacji zmiany hasła
    * @param password nowe hasło
    * @return użytkownik jeżeli odnaleziono
    */
  override def updatePassword(userId: UUID, token: String, password: String)(implicit authInfoRepository: AuthInfoRepository, passwordHasherRegistry: PasswordHasherRegistry): Future[Option[User]] = {
    val result = for {
      optionalUser <- userDAO.find(userId)
    } yield for {
      user <- optionalUser.filter(_.resetPasswordToken.contains(token))
    } yield user

    result.foreach(_.foreach { user =>
      userDAO.clearResetPasswordToken(user)
      authInfoRepository.add(user.loginInfo, passwordHasherRegistry.current.hash(password))
    })

    result
  }

  /**
    * Zlicza ilość użytkowników w bazie
    *
    * @return ilość użytkowników
    */
  override def count = userDAO.count

  /**
    * Zwraca listę użytkowników
    *
    * @param page     aktualna strona
    * @param pageSize ilość elementów na stronie
    * @param orderBy  sortowanie
    * @param filter   filtrowanie
    * @return lista użytkowników podzielona na strony
    */
  override def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%") =
    userDAO.list(page, pageSize, orderBy, filter)

  /**
    * Generuje token zmiany hasła użytkownika
    *
    * @param id identyfikator użytkownika
    * @return obiekt użytkownika
    */
  override def generateResetPasswordToken(id: UUID): Future[Option[User]] = userDAO.generateResetPasswordToken(id)

  /**
    * Wyszukuje użytkowników względem roli w systemie
    *
    * @param role rola w systemie
    * @return lista użytkowników
    */
  override def listUsersByRole(role: RoleType): Future[List[User]] = userDAO.findByRole(role)
}
