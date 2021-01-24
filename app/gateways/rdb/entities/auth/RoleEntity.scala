package gateways.rdb.entities.auth

import domain.models.user.{Role, RoleId, RoleName}
import slick.jdbc.JdbcProfile

private[gateways] case class RoleEntity(
    id: Int,
    name: String
) {
  def toRole: Role = Role(RoleId(id), RoleName(name))
}

private[gateways] trait RoleTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class RoleTable(tag: Tag) extends Table[RoleEntity](tag, "role") {

    val id   = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")

    def * = (id, name).<>(RoleEntity.tupled, RoleEntity.unapply)
  }

  val roleTable = TableQuery[RoleTable]
}
