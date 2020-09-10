package services

import akka.actor.ActorSystem
import daos.{ArticleDAO, NewsletterDAO}
import javax.inject.Inject
import org.joda.time.{DateTime, Minutes}
import utils.Language

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Serwis czasowego uruchamiania
  */
class ScheduleService @Inject()(implicit system: ActorSystem, mailerService: MailerService, articleDAO: ArticleDAO, newsletterDAO: NewsletterDAO) {

  /**
    * Oblicza ilość minut do najbliższego poniedziałku rano
    */
  private def minutesTillMondayMorning: Int = {
    val now = DateTime.now
    val target = now.withDayOfWeek(1).withTime(6,0,0,0).plusWeeks(1)

    Minutes.minutesBetween(now, target).getMinutes
  }

  /**
    * Obsługuje wysyłanie wiadomości newslwttera
    */
  private def sendNewsletters: Unit = for {
    addresses <- newsletterDAO.list
    articles <- articleDAO.newsletterList
  } yield {
    if(articles.length == 12)
      addresses.groupBy(_.lang).foreach { case (lang, addressesByLang) =>
        lang match {

          case Language.PL =>
            mailerService.sendNewsletterPL(addressesByLang.map(_.email), articles.take(6))

          case _ =>
            mailerService.sendNewsletterUS(addressesByLang.map(_.email), articles.slice(6, 12))

        }
      }
  }

  /**
    * Wysyła wiadomość newslettera
    */
  system.scheduler.scheduleWithFixedDelay(minutesTillMondayMorning.minutes, 7.days) { () =>
    sendNewsletters
  }
}
