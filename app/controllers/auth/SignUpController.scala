package controllers.auth

import auth.env.JWTEnv
import com.mohiva.play.silhouette.api.{SignUpEvent, Silhouette}
import controllers.viewmodels.request.auth.signUp.{SignUpRequest, SignUpRequestValidator}
import domain.usecases.auth.signUp.{SignUpInput, SignUpOutput, SingUpUseCase}
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SignUpController @Inject() (
    cc: ControllerComponents,
    silhouette: Silhouette[JWTEnv],
    signUpUseCase: SingUpUseCase
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with SignUpRequestValidator {

  def signUp: Action[SignUpRequest] = silhouette.UnsecuredAction.async(signUpRequestValidate) { implicit request =>
    val activationUrlProvider: UUID => String =
      authTokenId => routes.ActivateAccountController.activate(authTokenId).absoluteURL()

    val input = SignUpInput(request.body.toCredentialsSignUpData, request.remoteAddress, activationUrlProvider)
    signUpUseCase.handle(input) map {
      case SignUpOutput.UserCreated(user) =>
        silhouette.env.eventBus.publish(SignUpEvent(user, request))
        Created
      case SignUpOutput.UserAlreadyExists => Conflict
    }
  }
}
