package gateways.repositoryImpl.auth

import domain.models.auth.{AuthToken, AuthTokenRepository}
import gateways.rdb.entities.TokenTableDefinition
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthTokenRepositoryImpl @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext)
    extends AuthTokenRepository
    with HasDatabaseConfigProvider[JdbcProfile]
    with TokenTableDefinition {

  import profile.api._

  override def find(id: UUID): Future[Option[AuthToken]] = {
    val query = tokenTable.filter(_.id === id).result.headOption.map(_.map(_.toAuthToken))
    db.run(query)
  }
}
