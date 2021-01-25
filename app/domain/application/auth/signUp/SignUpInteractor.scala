package domain.application.auth.signUp

import cats.implicits.catsStdInstancesForFuture
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import domain.application.mail.MailService
import domain.application.{ConflictedError, InfraErrorOps, OptionTOps, SystemError}
import domain.models.auth.AuthTokenRepository
import domain.models.user.{Email, UserName, UserRepository}
import domain.usecases.auth.signUp.SignUpOutput.{UserAlreadyExists, UserCreated}
import domain.usecases.auth.signUp.{SignUpInput, SignUpOutput, SingUpUseCase}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SignUpInteractor @Inject() (
    authTokenRepository: AuthTokenRepository,
    passwordHasherRegistry: PasswordHasherRegistry,
    mailService: MailService,
    userRepository: UserRepository
)(implicit ec: ExecutionContext)
    extends SingUpUseCase {

  override def handle(input: SignUpInput): Future[SignUpOutput] = {
    val loginInfo = LoginInfo(CredentialsProvider.ID, input.signUpData.email)
    val result = for {
      _ <- userRepository.retrieveT(loginInfo) ifExists { user => ConflictedError(user) }
      authInfo = passwordHasherRegistry.current.hash(input.signUpData.password)
      user <- userRepository.create(
        loginInfo,
        Email(input.signUpData.email),
        UserName(input.signUpData.name)
      ) ifFailureThen SystemError
      _         <- authTokenRepository.addAuthenticate(user.id, loginInfo, authInfo) ifFailureThen SystemError
      authToken <- authTokenRepository.create(user.id) ifFailureThen SystemError
    } yield {
      val activationUrl = input.activationUrlProvider(authToken.id)
      mailService.sendActivateAccountEmail(input.signUpData.email, activationUrl)

      user
    }

    result.value.map {
      case Right(user) => UserCreated(user)
      case Left(serviceError) =>
        serviceError match {
          case _: ConflictedError => UserAlreadyExists
          case e: SystemError     => throw e.cause
          case _                  => throw new UnsupportedOperationException
        }
    }
  }
}
