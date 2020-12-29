package auth.env

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import domain.models.user.User

trait CookieEnv extends Env {
  override type I = User
  override type A = CookieAuthenticator
}
