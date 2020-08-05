package database

import java.io.Closeable

import io.getquill.{PostgresJdbcContext, SnakeCase}
import javax.inject.{Inject, Singleton}
import javax.sql.DataSource
import play.api.db.DBApiProvider

@Singleton
class DatabaseConnector @Inject()(val provider: DBApiProvider) {
  lazy val ctx = new PostgresJdbcContext(SnakeCase, provider.get.database("default").dataSource.asInstanceOf[DataSource with Closeable])
}
