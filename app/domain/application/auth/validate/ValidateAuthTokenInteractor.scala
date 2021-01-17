package domain.application.auth.validate

import domain.models.auth.AuthTokenRepository
import domain.usecases.auth.validate.{ValidateAuthTokenInput, ValidateAuthTokenOutput, ValidateAuthTokenUseCase}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ValidateAuthTokenInteractor @Inject() (
    authTokenRepository: AuthTokenRepository
)(implicit ec: ExecutionContext)
    extends ValidateAuthTokenUseCase {
  override def handle(input: ValidateAuthTokenInput): Future[ValidateAuthTokenOutput] = {
    authTokenRepository.find(input.token) map {
      case Some(token) => ValidateAuthTokenOutput.ValidateSuccess(token)
      case None        => ValidateAuthTokenOutput.ActivationTokenInvalid
    }
  }
}
