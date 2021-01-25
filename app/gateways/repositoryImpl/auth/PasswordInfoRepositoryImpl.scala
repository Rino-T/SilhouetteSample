package gateways.repositoryImpl.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import gateways.rdb.entities.auth.{LoginInfoTableDefinition, PasswordInfoEntity, PasswordInfoTableDefinition}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

class PasswordInfoRepositoryImpl @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext, val classTag: ClassTag[PasswordInfo])
    extends DelegableAuthInfoDAO[PasswordInfo]
    with HasDatabaseConfigProvider[JdbcProfile]
    with PasswordInfoTableDefinition
    with LoginInfoTableDefinition {

  import profile.api._

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    val query = for {
      info         <- loginInfoQuery(loginInfo)
      passwordInfo <- passwordInfoTable.filter(_.loginInfoId === info.id)
    } yield passwordInfo

    db.run(query.result.headOption)
      .map(_.map { passwordInfo =>
        PasswordInfo(passwordInfo.hasher, passwordInfo.password, passwordInfo.salt)
      })
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val transaction = for {
      info <- loginInfoQuery(loginInfo).result.head
      _    <- passwordInfoTable += PasswordInfoEntity.from(authInfo, info)
    } yield ()

    db.run(transaction.transactionally).map(_ => authInfo)
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = ???

  override def remove(loginInfo: LoginInfo): Future[Unit] = ???
}
