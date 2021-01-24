package gateways.repositoryImpl.user

import cats.data.{EitherT, OptionT}
import com.mohiva.play.silhouette.api.LoginInfo
import domain.models.user.{Email, User, UserId, UserName, UserRepository}
import gateways.rdb.entities.user.UserTableDefinition
import gateways.rdb.entities.auth.{LoginInfoTableDefinition, RoleTableDefinition, UserLoginInfoTableDefinition}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext)
    extends UserRepository
    with HasDatabaseConfigProvider[JdbcProfile]
    with UserTableDefinition
    with LoginInfoTableDefinition
    with UserLoginInfoTableDefinition
    with RoleTableDefinition {

  import profile.api._

  override def find(id: UserId): Future[Option[User]] = {
    val query = for {
      user <- userTable.filter(_.id === id.value)
      role <- roleTable.filter(_.id === user.roleId)
    } yield (user, role)

    db.run(query.result.headOption).map(_.map { case (user, role) => user.toUser(role.toRole) })
  }

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = {
    val query = for {
      loginInfo <- loginInfoTable
        .filter(_.providerId === loginInfo.providerID)
        .filter(_.providerKey === loginInfo.providerKey)
      userLoginInfo <- userLoginInfoTable.filter(_.loginInfoId === loginInfo.id)
      user          <- userTable.filter(_.id === userLoginInfo.userId)
      role          <- roleTable.filter(_.id === user.roleId)
    } yield (user, role)

    db.run(query.result.headOption).map(_.map { case (user, role) => user.toUser(role.toRole) })
  }

  override def retrieveT(loginInfo: LoginInfo): OptionT[Future, User] = OptionT(retrieve(loginInfo))

  override def find(id: UserId, providerId: String): OptionT[Future, (User, LoginInfo)] = {
    val query = for {
      ((_, loginInfo), user) <- userLoginInfoTable
        .filter(_.userId === id.value)
        .join(loginInfoTable)
        .on(_.loginInfoId === _.id)
        .join(userTable)
        .on(_._1.userId === _.id)
      role <- roleTable.filter(_.id === user.roleId)
      if loginInfo.providerId === providerId
    } yield (user, loginInfo, role)

    val result = db
      .run(query.result.headOption)
      .map(_.map { case (user, loginInfo, role) => (user.toUser(role.toRole), loginInfo.toLoginInfo) })

    OptionT(result)
  }

  override def create(loginInfo: LoginInfo, email: Email, name: UserName): EitherT[Future, Throwable, User] = ???
}
