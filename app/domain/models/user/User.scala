package domain.models.user

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

import java.util.UUID

case class User(
    id: UserId,
    loginInfo: LoginInfo,
    name: Option[UserName],
    email: Option[Email]
) extends Identity

case class UserId(value: UUID)

case class UserName(value: String) extends AnyVal

case class Email(value: String) extends AnyVal
