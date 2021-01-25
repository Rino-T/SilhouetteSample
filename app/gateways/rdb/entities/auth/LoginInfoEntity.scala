package gateways.rdb.entities.auth

import com.mohiva.play.silhouette.api.LoginInfo
import slick.jdbc.JdbcProfile

private[gateways] case class LoginInfoEntity(
    id: Option[Long],
    providerId: String,
    providerKey: String
) {
  def toLoginInfo: LoginInfo = LoginInfo(providerId, providerKey)
}
private[gateways] object LoginInfoEntity extends ((Option[Long], String, String) => LoginInfoEntity) {
  def fromLoginInfo(loginInfo: LoginInfo): LoginInfoEntity =
    LoginInfoEntity(None, loginInfo.providerID, loginInfo.providerKey)
}

private[gateways] trait LoginInfoTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class LoginInfoTable(tag: Tag) extends Table[LoginInfoEntity](tag, "login_info") {

    val id          = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    val providerId  = column[String]("provider_id")
    val providerKey = column[String]("provider_key")

    def * = (id, providerId, providerKey).<>(LoginInfoEntity.tupled, LoginInfoEntity.unapply)
  }

  val loginInfoTable = TableQuery[LoginInfoTable]

  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoTable, LoginInfoEntity, Seq] = {
    loginInfoTable
      .filter(_.providerId === loginInfo.providerID)
      .filter(_.providerKey === loginInfo.providerKey)
  }
}
