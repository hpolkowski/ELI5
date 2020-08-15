package utils

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

object FormatHelper {

  // Wzór daty 22.08.2020
  val DATE_PATTERN = "dd.MM.yyyy"

  /**
    * Formatuje datę według odgórnego wzoru
    *
    * @param date data
    * @return data jako tekst
    */
  def formatDate(date: LocalDate) = {
    val fmt = DateTimeFormatter.ofPattern(DATE_PATTERN)
    date.format(fmt)
  }

  /**
    * Formatuje datę według odgórnego wzoru
    *
    * @param date data
    * @return data jako tekst
    */
  def formatDate(date: LocalDateTime) = {
    val fmt = DateTimeFormatter.ofPattern(DATE_PATTERN)
    date.format(fmt)
  }

}
