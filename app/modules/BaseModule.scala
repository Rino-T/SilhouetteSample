package modules

import com.google.inject.AbstractModule
import domain.application.auth.activate.ActivateAccountInteractor
import domain.application.mail.MailService
import domain.models.auth.AuthTokenRepository
import domain.models.user.UserRepository
import domain.usecases.auth.activate.ActivateAccountUseCase
import gateways.repositoryImpl.auth.AuthTokenRepositoryImpl
import gateways.repositoryImpl.user.UserRepositoryImpl
import gateways.services.mail.AmazonSESMailServiceImpl
import net.codingwell.scalaguice.ScalaModule

class BaseModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActivateAccountUseCase].to[ActivateAccountInteractor]

    bind[UserRepository].to[UserRepositoryImpl]
    bind[AuthTokenRepository].to[AuthTokenRepositoryImpl]

    bind[MailService].to[AmazonSESMailServiceImpl]
  }
}
