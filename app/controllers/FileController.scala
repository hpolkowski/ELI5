package controllers

import java.nio.file.{Files, Paths}

import bootstrap.AppConfig
import com.mohiva.play.silhouette.api.Silhouette
import javax.inject._
import models.User
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc._
import services.FileService
import utils.auth.CookieEnvironment

import scala.concurrent.ExecutionContext

/**
  * Kontroler zapytań plików
  */
@Singleton
class FileController @Inject()(
  components: ControllerComponents
)(
  implicit
  appConfig: AppConfig,
  assets: AssetsFinder,
  context: ExecutionContext
) extends AbstractController(components) with I18nSupport {

  /**
    * Wyświetlanie zdjęcia
    *
    * @param path identyfikator pliku
    */
  def photo(path: String) = Action { implicit request =>
    try {
      Ok(Files.readAllBytes(Paths.get(s"${appConfig.filePath}$path"))).as("image/png")
    } catch {
      case e: Exception => Ok(Files.readAllBytes(Paths.get("public/images/missing.png")))
    }
  }
}
