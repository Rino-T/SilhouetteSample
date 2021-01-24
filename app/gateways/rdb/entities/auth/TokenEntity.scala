package gateways.rdb.entities.auth

import domain.models.auth.AuthToken
import domain.models.user.UserId
import slick.jdbc.JdbcProfile

import java.time.{Instant, ZoneId}
import java.util.UUID

private[gateways] case class TokenEntity(
    id: UUID,
    userId: UUID,
    private val expiryTimestamp: java.sql.Timestamp
) {
  val expiry: Instant = expiryTimestamp.toInstant

  def toAuthToken: AuthToken = AuthToken(id, UserId(userId), expiry.atZone(ZoneId.systemDefault()))
}

private[gateways] trait TokenTableDefinition {
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
