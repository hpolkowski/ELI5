package utils

import io.getquill.MappedEncoding

/**
  * Role użytkownika
  */
object RoleType extends Enumeration {
  type RoleType = Value

  val ADMIN = Value("ADMIN")                        // Administrator systemu

  implicit val encode = MappedEncoding[RoleType, String](_.toString)
  implicit val decode = MappedEncoding[String, RoleType](withName)
}
