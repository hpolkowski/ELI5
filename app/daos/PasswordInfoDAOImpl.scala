package daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import javax.inject.Inject
import services.UserService

import scala.concurrent.ExecutionContext

class PasswordInfoDAOImpl @Inject() (userService: UserService) (implicit context: ExecutionContext) extends PasswordInfoDAO {
  /**
   * Finds the auth info which is linked to the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The found auth info or None if no auth info could be found for the given login info.
   */
  override def find(loginInfo: LoginInfo) = {
    userService.retrieve(loginInfo).map {
      case Some(user) => Some(user.passwordInfo)

      case _ => None
    }
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = update(loginInfo, authInfo)

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    userService.retrieve(loginInfo).map {
      case Some(user) =>
        userService.updatePassword(user, authInfo.password)
        authInfo

      case _ => throw new Exception("PasswordInfoDAO - update : the user must exists to update its password")
    }
  }

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)

      case None => add(loginInfo, authInfo)
    }
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  override def remove(loginInfo: LoginInfo) = ???
}
