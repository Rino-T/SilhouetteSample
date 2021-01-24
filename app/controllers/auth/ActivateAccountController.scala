package controllers.auth

import auth.env.JWTEnv
import com.mohiva.play.silhouette.api.Silhouette
import domain.usecases.auth.activate.{ActivateAccountInput, ActivateAccountOutput, ActivateAccountUseCase}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ActivateAccountController @Inject() (
    cc: ControllerComponents,
    silhouette: Silhouette[JWTEnv],
    activateAccountUseCase: ActivateAccountUseCase
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def activate(token: UUID): Action[AnyContent] = silhouette.UnsecuredAction.async {
    val input = ActivateAccountInput(token)
    activateAccountUseCase.handle(input) map {
      case ActivateAccountOutput.ActivateAccountSuccess => Redirect("/signIn?message=emailVerified")
      case ActivateAccountOutput.ValidateTokenFailed    => Redirect("/error?message=activationTokenInvalid")
    }
  }
}
