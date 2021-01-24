package gateways.rdb.entities.auth

import slick.jdbc.JdbcProfile

import java.util.UUID

private[gateways] case class UserLoginInfoEntity(
    userId: UUID,
    loginInfoId: Long
)

private[gateways] trait UserLoginInfoTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class UserLoginInfoTable(tag: Tag) extends Table[UserLoginInfoEntity](tag, "user_login_info") {

    val userId      = column[UUID]("user_id")
    val loginInfoId = column[Long]("login_info_id")

    def * = (userId, loginInfoId).<>(UserLoginInfoEntity.tupled, UserLoginInfoEntity.unapply)
  }

  val userLoginInfoTable = TableQuery[UserLoginInfoTable]
}
