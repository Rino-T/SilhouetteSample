package domain

import cats.data.{EitherT, OptionT}
import cats.implicits.catsStdInstancesForFuture

import scala.concurrent.{ExecutionContext, Future}

package object application {
  type ErrorCode = String

  implicit class OptionTOps[T](maybeValue: OptionT[Future, T]) {
    def ifNotExists(f: => ServiceError)(implicit ec: ExecutionContext): EitherT[Future, ServiceError, T] = {
      maybeValue.toRight(f)
    }

    def ifExists(f: T => ServiceError)(implicit ec: ExecutionContext): EitherT[Future, ServiceError, Unit] = {
      val result = maybeValue.value.map {
        case Some(value) => Left(f(value))
        case None        => Right(())
      }
      EitherT(result)
    }
  }

  implicit class InfraErrorOps[T](value: EitherT[Future, Throwable, T]) {
    def ifFailureThen(f: Throwable => ServiceError)(implicit ec: ExecutionContext): EitherT[Future, ServiceError, T] = {
      value.leftMap(f)
    }
  }
}
