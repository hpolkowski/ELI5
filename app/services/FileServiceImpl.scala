package services
import java.nio.file.Files

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
    val directory = s"${appConfig.filePath}${(Math.random * 1000000000L).toLong}/"
    val filename = s"thumbnail.png"
    val pathname = s"$directory$filename"

    new java.io.File(directory).mkdirs()

    val file = new java.io.File(pathname)

    Files.copy(data.ref.path, file.toPath)

    Right(pathname)
  } else
    Left("error.file.filename.empty")

}
