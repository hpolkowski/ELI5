package utils

import scala.concurrent.{ExecutionContext, Future}

object FutureUtils {

  def flatten[B](x: Option[Future[B]])(implicit ec: ExecutionContext): Future[Option[B]] =
    x match {
      case Some(f) => f.map(Some(_))
      case None => Future.successful(None)
    }

  def flattenList[B](x: List[Future[Option[B]]])(implicit ec: ExecutionContext): Future[List[B]] = Future.sequence(x).map(_.flatten)

  def flattenOption[B](x: Option[Future[Option[B]]])(implicit ec: ExecutionContext): Future[Option[B]] = flatten(x).map(_.flatten)

  def flattenListOfOptions[B](x: Future[List[Future[Option[B]]]])(implicit ec: ExecutionContext): Future[List[B]] = x.flatMap(flattenList)

  def flatFuture[B](x: Future[Option[Future[B]]])(implicit ec: ExecutionContext): Future[Option[B]] = x.flatMap(flatten)

  def flatFutureOption[B](x: Future[Option[Future[Option[B]]]])(implicit ec: ExecutionContext): Future[Option[B]] = x.flatMap(flatten).map(_.flatten)

  def doubleFlatFuture[B](x: Future[Option[Future[Option[Future[B]]]]])(implicit ec: ExecutionContext): Future[Option[B]] =
    flatFuture(flatFuture(x).map(_.flatten))

  def doubleFlatFutureOption[B](x: Future[Option[Future[Option[Future[Option[B]]]]]])(implicit ec: ExecutionContext): Future[Option[B]] =
    flatFutureOption(flatFutureOption(x))
}
