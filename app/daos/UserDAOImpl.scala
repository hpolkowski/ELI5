package daos

import java.util.UUID

import bootstrap.AppConfig
import com.mohiva.play.silhouette.api.LoginInfo
import database.{DatabaseConnector, QueryExtension}
import javax.inject.Inject
import models.{Page, User}
import utils.RoleType.RoleType

import scala.concurrent.{ExecutionContext, Future}

/**
  * Zapytania bazodanowe dotyczące użytkoników
  */
class UserDAOImpl @Inject() (val database: DatabaseConnector, appConfig: AppConfig) (implicit context: ExecutionContext)
  extends UserDAO with QueryExtension {

  import database.ctx._

  private val users = quote(querySchema[User]("users"))

  /**
    * Finds a user by its login info.
    *
    * @param loginInfo The login info of the user to find.
    * @return The found user or None if no user for the given login info could be found.
    */
  def find(loginInfo: LoginInfo) = Future.successful(
    run(users.filter { data =>
      (data.providerId == lift(loginInfo.providerID)) && data.providerKey.compareLowerCase(lift(loginInfo.providerKey))
    }).headOption
  )

  /**
    * Finds a user by its user ID.
    *
    * @param userID The ID of the user to find.
    * @return The found user or None if no user for the given ID could be found.
    */
  def find(userID: UUID) = Future.successful(
    run(users.filter(_.id == lift(userID))).headOption
  )

  /**
    * Sprawdza, czy wprowadzone dane nie są już zapisane w bazie
    *
    * @param loginInfo The login info of the user to find.
    * @return true jeżeli nie istnieje w bazie użytkownik o podanych danych
    */
  def isUnique(loginInfo: LoginInfo) = find(loginInfo).map(_.isEmpty)

  /**
    * Zlicza ilość użytkowników w bazie
    *
    * @return ilość użytkowników
    */
  def count = Future.successful(run(users.size))

  /**
    * Saves a user.
    *
    * @param userData The user to save.
    * @return The saved user.
    */
  def save(userData: User) = transaction {
    isUnique(userData.loginInfo).map { isUnique =>
      if (isUnique) {
        run(users.insert(lift(userData)))
        Some(userData)
      } else
        None
    }
  }

  /**
    * Updates User data
    *
    * @param user The user with new data to update.
    * @return The user with new data.
    */
  override def update(user: User) = {
    run(users.filter(_.id == lift(user.id)).update(
      _.role -> lift(user.role),
      _.fullName -> lift(user.fullName),
      _.providerKey -> lift(user.providerKey)
    ))
    Future.successful(user)
  }

  /**
    * Updates user password
    *
    * @param user     The user to update password.
    * @param password New password hash.
    * @return The saved user.
    */
  override def updatePassword(user: User, password: String) = transaction {
    run(users.filter(_.id == lift(user.id)).update(_.password -> lift(password)))
    Future.successful(user.copy(password = password))
  }

  /**
    * Lista użytkowników systemu
    *
    * @param page     aktualna strona
    * @param pageSize ilość elementów na stronie
    * @param orderBy  sortowanie
    * @param filter   filtrowanie
    * @return lista danych zamówienia podzielona na strony
    */
  override def list(page: Int = 1, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%") = {
    val query: Quoted[Query[User]] = users

    val filteredQuery: Quoted[Query[User]] = if (filter.nonEmpty)
      query.filter { u =>
        (u.providerKey likeLowerCase lift(s"%$filter%")) || (u.fullName likeLowerCase lift(s"%$filter%"))
      }
    else query

    val sortedQuery: Quoted[Query[User]] = orderBy match {
      case 1 => filteredQuery.sortBy(_.providerKey)
      case 2 => filteredQuery.sortBy(_.fullName)

      case -1 => filteredQuery.sortBy(_.providerKey)(Ord.desc)
      case -2 => filteredQuery.sortBy(_.fullName)(Ord.desc)

      case _ => filteredQuery.sortBy(_.id)(Ord.desc)
    }

    val offset = pageSize * (page - 1)
    val result = run(sortedQuery.drop(lift(offset)).take(lift(pageSize)))

    Future.successful(Page(result, page, offset, run(query.size), pageSize))
  }

  /**
    * Generuje token zmiany hasła i zapisuje go w obiekcie użytkowinka
    *
    * @param  id identyfikator użytkownika
    * @return token zmiany hasła
    */
  override def generateResetPasswordToken(id: UUID) = {
    val token: Option[String] = Some(UUID.randomUUID().toString)

    run(users.filter(_.id == lift(id)).update(_.resetPasswordToken -> lift(token)))

    Future.successful(run(users.filter(_.id == lift(id))).headOption)
  }

  /**
    * Usuwa token zmiany hasła
    *
    * @param user użytkownik
    */
  override def clearResetPasswordToken(user: User) = {
    run(users.filter(_.id == lift(user.id)).update(_.resetPasswordToken -> lift(None: Option[String])))
    Future.successful(user)
  }

  /**
    * Wyszukuje użytkowników po roli w systemie
    *
    * @param role rola w systemie
    * @return lista uzytkowników
    */
  override def findByRole(role: RoleType): Future[List[User]] = Future.successful(
    run(users.filter(_.role == lift(role)))
  )
}
