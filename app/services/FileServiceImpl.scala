package services
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import bootstrap.AppConfig
import javax.inject.Inject
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData

/**
  * Obsługa plików
  */
class FileServiceImpl @Inject()(appConfig: AppConfig) extends FileService {

  /**
    * Zapisuje plik na dysku
    *
    * @param data dane pliku z requestu
    * @return na lewo błąd, na prawo adres zapisanego pliku
    */
  override def save(data: MultipartFormData.FilePart[TemporaryFile]): Either[String, String] = if(data.filename.trim.nonEmpty)  {
    val pathname = s"${LocalDateTime.now.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"))}${(Math.random * 100000L).toLong}"
    val filename = "thumbnail.png"

    new java.io.File(s"${appConfig.filePath}$pathname").mkdirs()

    val file = new java.io.File(s"${appConfig.filePath}$pathname/$filename")

    Files.copy(data.ref.path, file.toPath)

    Right(s"$pathname/$filename")
  } else
    Left("error.file.filename.empty")

}
