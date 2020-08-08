package controllers

import javax.inject._
import play.api.mvc._

/**
 * Kontroler głównej strony aplikacji
 */
@Singleton
class HomeController @Inject()(
  components: ControllerComponents
) extends AbstractController(components) {

  /**
   * Główna strona aplikacji
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
