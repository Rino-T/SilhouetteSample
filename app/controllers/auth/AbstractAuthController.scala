package controllers.auth

import auth.env.JWTEnv
import com.mohiva.play.silhouette.api.Authenticator.Implicits.RichDateTime
import com.mohiva.play.silhouette.api.services.AuthenticatorResult
import com.mohiva.play.silhouette.api.util.Clock
import com.mohiva.play.silhouette.api.{LoginEvent, LoginInfo, Silhouette}
import domain.models.user.User
import net.ceedubs.ficus.Ficus._
import play.api.Configuration
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{InjectedController, Request}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

abstract class AbstractAuthController(
    silhouette: Silhouette[JWTEnv],
    configuration: Configuration,
    clock: Clock
)(implicit ec: ExecutionContext)
    extends InjectedController
    with I18nSupport {

  protected def authenticateUser(
      user: User,
      loginInfo: LoginInfo,
      rememberMe: Boolean
  )(implicit request: Request[_]): Future[AuthenticatorResult] = {
    val config = configuration.underlying
    silhouette.env.authenticatorService.create(loginInfo) map {
      case authenticator if rememberMe =>
        authenticator.copy(
          // この演算「+」は com.mohiva.play.silhouette.api.Authenticator.Implicits.RichDateTime のもの
          expirationDateTime =
            clock.now + config.as[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorExpiry"),
          idleTimeout = config.getAs[FiniteDuration]("silhouette.authenticator.rememberMe.authenticatorIdleTimeout")
        )
      case authenticator => authenticator
    } flatMap { authenticator =>
      silhouette.env.eventBus.publish(LoginEvent(user, request))
      silhouette.env.authenticatorService.init(authenticator) flatMap { token =>
        silhouette.env.authenticatorService.embed(
          token, {
            Ok(
              Json.obj(
                "id"    -> user.id.value,
                "token" -> token,
                "name"  -> user.name.map(_.value),
                "role"  -> user.role.name.value
              )
            )
          }
        )
      }
    }
  }
}
