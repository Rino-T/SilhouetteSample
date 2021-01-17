package modules

import com.google.inject.AbstractModule
import domain.application.auth.validate.ValidateAuthTokenInteractor
import domain.models.auth.AuthTokenRepository
import domain.usecases.auth.validate.ValidateAuthTokenUseCase
import gateways.repositoryImpl.auth.AuthTokenRepositoryImpl
import net.codingwell.scalaguice.ScalaModule

class BaseModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ValidateAuthTokenUseCase].to[ValidateAuthTokenInteractor]

    bind[AuthTokenRepository].to[AuthTokenRepositoryImpl]
  }
}
