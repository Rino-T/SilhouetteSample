package gateways.rdb.entities.auth

import com.mohiva.play.silhouette.api.util.PasswordInfo
import slick.jdbc.JdbcProfile

private[gateways] case class PasswordInfoEntity(
    hasher: String,
    password: String,
    salt: Option[String],
    loginInfoId: Long
)

object PasswordInfoEntity extends ((String, String, Option[String], Long) => PasswordInfoEntity) {
  def from(passwordInfo: PasswordInfo, loginInfo: LoginInfoEntity): PasswordInfoEntity = {
    require(loginInfo.id.isDefined)
    PasswordInfoEntity(
      passwordInfo.hasher,
      passwordInfo.password,
      passwordInfo.salt,
      loginInfo.id.get
    )
  }
}

private[gateways] trait PasswordInfoTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class PasswordInfoTable(tag: Tag) extends Table[PasswordInfoEntity](tag, "password_info") {
    val hasher      = column[String]("hasher")
    val password    = column[String]("password")
    val salt        = column[Option[String]]("salt")
    val loginInfoId = column[Long]("login_info_id")

    def * = (hasher, password, salt, loginInfoId).<>(PasswordInfoEntity.tupled, PasswordInfoEntity.unapply)
  }

  val passwordInfoTable = TableQuery[PasswordInfoTable]
}
