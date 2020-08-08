package forms

import play.api.data.Form
import play.api.data.Forms._
import utils.FormFieldImplicits.enum
import utils.RoleType.RoleType
import utils.RoleType

/**
  * Formularz danych tworzenia użytkownika
  *
  * @param email    adres email użytkownika
  * @param role     rola użytkownika w systemie
  * @param fullName imie i nazwisko
  */
case class CreateUserForm (
  email: String,
  role: RoleType,
  fullName: String
)

/**
  * Obiekt towarzyszący klasy CreateUserForm
  */
object CreateUserForm {
  val form = Form(
    mapping(
      "email" -> email,
      "role" -> enum[RoleType](RoleType),
      "fullName" -> text
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  )
}
