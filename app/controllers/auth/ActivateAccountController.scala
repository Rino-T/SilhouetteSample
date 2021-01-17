package controllers.auth

import auth.env.JWTEnv
import com.mohiva.play.silhouette.api.Silhouette
import controllers.routes
import domain.usecases.auth.validate.{ValidateAuthTokenInput, ValidateAuthTokenOutput, ValidateAuthTokenUseCase}
import play.api.mvc.{AbstractController, Call, ControllerComponents}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ActivateAccountController @Inject() (
    cc: ControllerComponents,
    silhouette: Silhouette[JWTEnv],
    validateAuthTokenUseCase: ValidateAuthTokenUseCase
)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def activate(token: UUID) = silhouette.UnsecuredAction.async {
    val input = ValidateAuthTokenInput(token)
    validateAuthTokenUseCase.handle(input) flatMap {
      case ValidateAuthTokenOutput.ValidateSuccess(authToken) => ???
      case ValidateAuthTokenOutput.ActivationTokenInvalid =>
        Future.successful(Redirect("/error?message=activationTokenInvalid"))
    }
  }
}
