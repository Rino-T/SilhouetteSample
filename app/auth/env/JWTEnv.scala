package auth.env

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import domain.models.user.User

trait JWTEnv extends Env {
  override type I = User
  override type A = JWTAuthenticator
}
