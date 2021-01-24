package gateways.repositoryImpl.auth

import cats.data.{EitherT, OptionT}
import com.mohiva.play.silhouette.api.{AuthInfo, LoginInfo}
import domain.models.auth.{AuthToken, AuthTokenRepository}
import domain.models.user.UserId
import gateways.rdb.entities.auth.TokenTableDefinition
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

class AuthTokenRepositoryImpl @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext)
    extends AuthTokenRepository
    with HasDatabaseConfigProvider[JdbcProfile]
    with TokenTableDefinition {

  import profile.api._

  override def find(id: UUID): OptionT[Future, AuthToken] = {
    val query = tokenTable.filter(_.id === id).result.headOption.map(_.map(_.toAuthToken))
    OptionT(db.run(query))
  }

  override def addAuthenticate[T <: AuthInfo](
      userId: UserId,
      loginInfo: LoginInfo,
      authInfo: T
  ): EitherT[Future, Throwable, Unit] = ???

  override def create(userId: UserId, expiry: FiniteDuration): EitherT[Future, Throwable, AuthToken] = ???
}
