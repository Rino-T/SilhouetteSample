package controllers.auth

import auth.env.JWTEnv
import com.mohiva.play.silhouette.api.Silhouette
import controllers.viewmodels.request.auth.signUp.{SignUpRequest, SignUpRequestValidator}
import play.api.mvc.{AbstractController, Action, ControllerComponents}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SignUpController @Inject() (
    cc: ControllerComponents,
    silhouette: Silhouette[JWTEnv]
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with SignUpRequestValidator {

  def submit: Action[SignUpRequest] = silhouette.UnsecuredAction.async(signUpRequestValidate) { implicit request =>
    val activationUrlProvider: UUID => String = { authTokenId => "" }

    Future.successful(Ok)
  }
}
