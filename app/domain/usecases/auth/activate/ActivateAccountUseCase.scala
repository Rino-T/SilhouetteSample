package domain.usecases.auth.activate

import domain.usecases.core.{AsyncUseCase, Input, Output}

import java.util.UUID

abstract class ActivateAccountUseCase extends AsyncUseCase[ActivateAccountInput, ActivateAccountOutput]

case class ActivateAccountInput(token: UUID) extends Input[ActivateAccountOutput]

sealed abstract class ActivateAccountOutput extends Output

object ActivateAccountOutput {
  case object ActivateAccountSuccess extends ActivateAccountOutput

  case object ValidateTokenFailed extends ActivateAccountOutput
}
