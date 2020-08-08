package daos

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO

trait PasswordInfoDAO extends DelegableAuthInfoDAO[PasswordInfo] {
}