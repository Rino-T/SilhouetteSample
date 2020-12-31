package gateways.rdb.entities

import slick.jdbc.JdbcProfile

import java.util.UUID

case class UserEntity(
    id: UUID,
    name: Option[String],
    email: Option[String],
    roleId: Int,
    activated: Boolean
)

trait UserTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class UserTable(tag: Tag) extends Table[UserEntity](tag, "user") {
    val id        = column[UUID]("id", O.PrimaryKey)
    val name      = column[Option[String]]("name")
    val email     = column[Option[String]]("email")
    val roleId    = column[Int]("role_id")
    val activated = column[Boolean]("activated")

    def * = (id, name, email, roleId, activated).<>(UserEntity.tupled, UserEntity.unapply)
  }

  val userTable = TableQuery[UserTable]
}
