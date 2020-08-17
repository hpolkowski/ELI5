package services

import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

/**
  * Obsługa plików
  */
trait FileService {

  /**
    * Zapisuje plik na dysku
    *
    * @param data dane pliku z requestu
    * @return na lewo błąd, na prawo adres zapisanego pliku
    */
  def save(data: FilePart[TemporaryFile]): Either[String, String]

}
