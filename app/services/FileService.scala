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
    * @param data    dane pliku z requestu
    * @param subpath dodatkowa ścieżka do zapisu pliku
    * @return na lewo błąd, na prawo adres zapisanego pliku
    */
  def save(data: FilePart[TemporaryFile], subpath: String = ""): Either[String, String]

}
