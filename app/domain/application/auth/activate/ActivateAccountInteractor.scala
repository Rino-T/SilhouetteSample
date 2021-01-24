package domain.application.auth.activate

import cats.implicits.catsStdInstancesForFuture
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import domain.application.user.UserService
import domain.application.{InfraErrorOps, NotFoundError, OptionTOps, SystemError}
import domain.models.auth.AuthTokenRepository
import domain.models.user.UserRepository
import domain.usecases.auth.activate.ActivateAccountOutput.{ActivateAccountSuccess, ValidateTokenFailed}
import domain.usecases.auth.activate.{ActivateAccountInput, ActivateAccountOutput, ActivateAccountUseCase}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ActivateAccountInteractor @Inject() (
    authTokenRepository: AuthTokenRepository,
    userRepository: UserRepository,
    userService: UserService
)(implicit ec: ExecutionContext)
    extends ActivateAccountUseCase {
  override def handle(input: ActivateAccountInput): Future[ActivateAccountOutput] = {
    val result = for {
      authToken <- authTokenRepository.find(input.token) ifNotExists NotFoundError("AuthToken", input.token)
      userLoginInfo <- userRepository
        .find(authToken.userID, CredentialsProvider.ID) ifNotExists NotFoundError("User", authToken.userID)
      activateResult <- userService.setEmailActivated(userLoginInfo._1) ifFailureThen SystemError
    } yield activateResult

    result.value.map {
      case Right(_) => ActivateAccountSuccess
      case Left(_)  => ValidateTokenFailed
    }
  }
}
