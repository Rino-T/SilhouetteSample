package domain.models.user

import com.mohiva.play.silhouette.api.services.IdentityService

import scala.concurrent.Future

trait UserRepository extends IdentityService[User] {
  def find(id: UserId): Future[Option[User]]
}
