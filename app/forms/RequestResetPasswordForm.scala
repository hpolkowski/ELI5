package forms

import play.api.data.Form
import play.api.data.Forms._

/**
  * Formularz rządania zmiany hasła
  *
  * @param email adres email
  */
case class RequestResetPasswordForm (
  email: String
)

/**
  * Obiekt towarzyszący klasy RequestResetPasswordForm
  */
object RequestResetPasswordForm {
  val form = Form(
    mapping(
      "email" -> nonEmptyText
    )(RequestResetPasswordForm.apply)(RequestResetPasswordForm.unapply)
  )
}
