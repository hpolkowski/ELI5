package forms

import play.api.data.Form
import play.api.data.Forms._

/**
  * Formularz danych zmiany hasła
  *
  * @param password         hasło
  * @param reenterPassword  powtórzone hasło
  */
case class ResetPasswordForm (
  password: String,
  reenterPassword: String
)

/**
  * Obiekt towarzyszący klasy ResetPasswordForm
  */
object ResetPasswordForm {
  val form = Form(
    mapping(
      "password" -> nonEmptyText,
      "re-enterPassword" -> nonEmptyText
    )(ResetPasswordForm.apply)(ResetPasswordForm.unapply)
      .verifying("user.passwordReset.not-match", passwords =>
        passwords.password == passwords.reenterPassword
      )
  )
}
