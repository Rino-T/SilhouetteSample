package domain.application.user

import cats.data.EitherT
import domain.models.user.User

import scala.concurrent.Future

trait UserService {
  def setEmailActivated(user: User): EitherT[Future, Throwable, Unit]
}
