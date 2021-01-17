package domain.usecases.auth.signUp

import domain.models.auth.CredentialsSignUpData
import domain.models.user.User
import domain.usecases.core.{AsyncUseCase, Input, Output}

import java.util.UUID

abstract class SingUpUseCase extends AsyncUseCase[SignUpInput, SignUpOutput]

case class SignUpInput(
    signUpData: CredentialsSignUpData,
    remoteAddress: String,
    activationUrlProvider: UUID => String
) extends Input[SignUpOutput]

sealed abstract class SignUpOutput extends Output

object SignUpOutput {
  case class UserCreated(user: User) extends SignUpOutput
  case object UserAlreadyExists      extends SignUpOutput
}
