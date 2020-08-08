package forms

import play.api.data.Form
import play.api.data.Forms._

/**
  * Formularz danych logowania
  *
  * @param email    adres email użytkownika
  * @param password hasło użytkownika
  */
case class SignInForm (
  email: String,
  password: String
)

/**
  * Obiekt towarzyszący klasy SignInForm
  */
object SignInForm {
  val form = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText
    )(SignInForm.apply)(SignInForm.unapply)
  )
}
