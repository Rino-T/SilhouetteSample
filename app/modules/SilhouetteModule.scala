package modules

import auth.env.JWTEnv
import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.util.{Clock, FingerprintGenerator, IDGenerator, PasswordInfo}
import com.mohiva.play.silhouette.api.{EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import net.codingwell.scalaguice.ScalaModule
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext.Implicits.global

class SilhouetteModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[Silhouette[JWTEnv]].to[SilhouetteProvider[JWTEnv]]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(new EventBus)
    bind[Clock].toInstance(Clock())
  }

  @Provides
  def providePasswordInfo(dbConfig: DatabaseConfigProvider): DelegableAuthInfoDAO[PasswordInfo] = {}
}
