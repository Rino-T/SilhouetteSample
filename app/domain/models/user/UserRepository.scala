package domain.models.user

import cats.data.{EitherT, OptionT}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService

import scala.concurrent.Future

trait UserRepository extends IdentityService[User] {
  def find(id: UserId): Future[Option[User]]

  def find(id: UserId, providerId: String): OptionT[Future, (User, LoginInfo)]

  def retrieveT(loginInfo: LoginInfo): OptionT[Future, User]

  def create(loginInfo: LoginInfo, email: Email, name: UserName): EitherT[Future, Throwable, User]
}
