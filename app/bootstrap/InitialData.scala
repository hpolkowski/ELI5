package bootstrap

import java.util.UUID

import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import javax.inject.{Inject, Singleton}
import models.User
import play.api.Logger
import services.UserService
import utils.RoleType

import scala.concurrent.ExecutionContext

/**
  * Dane inicjalizacyjne aplikacji
  */
@Singleton
private[bootstrap] class InitialData @Inject() (
  userService: UserService,
  authInfoRepository: AuthInfoRepository,
  passwordHasherRegistry: PasswordHasherRegistry
)(implicit context: ExecutionContext) {
  val logger = Logger("application")

  /**
    * Inicjalizuje bazę danych domyślnymi wartościami
    */
  def insertInitialData() = {
    userService.count.map { userCount =>
      if (userCount == 0) {
        logger.info("InitialData: Created admin user.")

        InitialData.users.foreach { userData =>
          for {
            user <- userService.save(userData)
            _ <- authInfoRepository.add(userData.loginInfo, passwordHasherRegistry.current.hash(userData.password))
          } yield {
            user
          }
        }
      }
    }
  }

  insertInitialData()
}

private[bootstrap] object InitialData {
  def users = List(
    User(UUID.randomUUID(), RoleType.ADMIN, CredentialsProvider.ID, "admin@admin.pl", "admin", "Administrator Systemu", None)
  )
}
