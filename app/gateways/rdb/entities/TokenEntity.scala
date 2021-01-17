package gateways.rdb.entities

import domain.models.auth.AuthToken
import slick.jdbc.JdbcProfile

import java.time.{Instant, ZoneId}
import java.util.UUID

case class TokenEntity(
    id: UUID,
    userId: UUID,
    private val expiryTimestamp: java.sql.Timestamp
) {
  val expiry: Instant = expiryTimestamp.toInstant

  def toAuthToken: AuthToken = AuthToken(id, userId, expiry.atZone(ZoneId.systemDefault()))
}

trait TokenTableDefinition {
  protected val profile: JdbcProfile

  import profile.api._

  class TokenTable(tag: Tag) extends Table[TokenEntity](tag, "token") {

    val id     = column[UUID]("id", O.PrimaryKey)
    val userId = column[UUID]("user_id")
    val expiry = column[java.sql.Timestamp]("expiry")

    def * = (id, userId, expiry).<>(TokenEntity.tupled, TokenEntity.unapply)
  }

  val tokenTable = TableQuery[TokenTable]
}
