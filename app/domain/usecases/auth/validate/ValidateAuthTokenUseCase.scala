package domain.usecases.auth.validate

import domain.models.auth.AuthToken
import domain.usecases.core.{AsyncUseCase, Input, Output}

import java.util.UUID

abstract class ValidateAuthTokenUseCase extends AsyncUseCase[ValidateAuthTokenInput, ValidateAuthTokenOutput]

case class ValidateAuthTokenInput(token: UUID) extends Input[ValidateAuthTokenOutput]

sealed abstract class ValidateAuthTokenOutput extends Output

object ValidateAuthTokenOutput {
  case class ValidateSuccess(authToken: AuthToken) extends ValidateAuthTokenOutput

  case object ActivationTokenInvalid extends ValidateAuthTokenOutput
}
