package models

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

import java.util.UUID

case class User(
    id: UUID,
    loginInfo: LoginInfo,
    name: Option[String],
    email: Option[String]
) extends Identity
