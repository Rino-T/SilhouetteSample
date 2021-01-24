package domain.models.auth

import domain.models.user.UserId

import java.time.ZonedDateTime
import java.util.UUID

case class AuthToken(
    id: UUID,
    userID: UserId,
    expiry: ZonedDateTime
)
