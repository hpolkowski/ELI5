package bootstrap

import javax.inject.{Inject, Singleton}
import play.api.Logger

import scala.concurrent.ExecutionContext

/**
  * Dane inicjalizacyjne aplikacji
  */
@Singleton
private[bootstrap] class InitialData @Inject() (
)(implicit context: ExecutionContext) {

  /**
    * Inicjalizuje bazę danych domyślnymi wartościami
    */
  def insertInitialData() = {
  }

  insertInitialData()
}

private[bootstrap] object InitialData {
}
