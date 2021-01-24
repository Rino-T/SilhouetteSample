package domain.models.auth

import cats.data.{EitherT, OptionT}
import com.mohiva.play.silhouette.api.{AuthInfo, LoginInfo}
import domain.models.user.UserId

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}

trait AuthTokenRepository {
  def find(id: UUID): OptionT[Future, AuthToken]

  def addAuthenticate[T <: AuthInfo](
      userId: UserId,
      loginInfo: LoginInfo,
      authInfo: T
  ): EitherT[Future, Throwable, Unit]

  def create(userId: UserId, expiry: FiniteDuration = 5 minutes): EitherT[Future, Throwable, AuthToken]
}
