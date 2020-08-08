package database

import java.time.{LocalDate, LocalDateTime}

import javax.inject.Singleton

@Singleton
trait QueryExtension {
  val database: DatabaseConnector

  import database.ctx._

  implicit class LikeLowercase(s1: String) {
    def likeLowerCase (s2: String) = quote(infix"lower($s1) like lower($s2)".as[Boolean])
  }

  implicit class CompareLowercase(s1: String) {
    def compareLowerCase (s2: String) = quote(infix"lower($s1) = lower($s2)".as[Boolean])
  }

  implicit class LocalDateExtension(left: LocalDate) {
    def isEqualDate (right: LocalDate) = quote(infix"$left = $right".as[Boolean])
    def isAfterDate (right: LocalDate) = quote(infix"$left > $right".as[Boolean])
    def isBeforeDate (right: LocalDate) = quote(infix"$left < $right".as[Boolean])
    def isAfterOrEqualDate (right: LocalDate) = quote(infix"$left >= $right".as[Boolean])
    def isBeforeOrEqualDate (right: LocalDate) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class LocalDateTimeExtension(s1: LocalDateTime) {
    def isAfterDateTime (s2: LocalDateTime) = quote(infix"$s1 > $s2".as[Boolean])
    def isBeforeDateTime (s2: LocalDateTime) = quote(infix"$s1 < $s2".as[Boolean])
    def isAfterOrEqualDateTime (s2: LocalDateTime) = quote(infix"$s1 >= $s2".as[Boolean])
    def isBeforeOrEqualDateTime (s2: LocalDateTime) = quote(infix"$s1 <= $s2".as[Boolean])
  }
}
