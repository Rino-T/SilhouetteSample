package gateways.repositoryImpl.user

import com.mohiva.play.silhouette.api.LoginInfo
import domain.models.user.{User, UserId, UserRepository}
import play.api.db.slick.DatabaseConfigProvider

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext)
    extends UserRepository {
  override def find(id: UserId): Future[Option[User]] = ???

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = ???
}
